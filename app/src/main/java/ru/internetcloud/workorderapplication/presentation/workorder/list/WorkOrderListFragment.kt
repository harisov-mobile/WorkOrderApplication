package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import javax.inject.Inject
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrderListBinding
import ru.internetcloud.workorderapplication.di.ViewModelFactory
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.presentation.workorder.search.SearchWorkOrderFragment

class WorkOrderListFragment : Fragment(R.layout.fragment_work_order_list), FragmentResultListener {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onAddWorkOrder(requestKeyNewOrderAdded: String, argNameNewOrderAdded: String)
        fun onEditWorkOrder(workOrderId: String)
        fun onLaunchDataSynchronization()
    }

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private val binding by viewBinding(FragmentWorkOrderListBinding::bind)

    private lateinit var viewModel: WorkOrderListViewModel
    private var workOrderListAdapter: WorkOrderListAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // даггер:
        component.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.workOrderRecyclerView.adapter = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkOrderListViewModel::class.java)

        setHasOptionsMenu(true)
        setupClickListeners()
        setupWorkOrderRecyclerView(view)
        setupFilterDescription()
        setupResultListeners()
        observeViewModel()
    }

    private fun setupResultListeners() {
        childFragmentManager.setFragmentResultListener(REQUEST_EXIT_QUESTION_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_SEARCH_WO_DATA_PICKER_KEY, viewLifecycleOwner, this)

        setFragmentResultListener(REQUEST_KEY_NEW_ORDER_ID_PARAM) { key, bundle ->
            viewModel.newWorkOrderId = bundle.getString(ARG_NAME_NEW_ORDER_ID_PARAM)
        }
    }

    private fun setupClickListeners() {
        binding.addFab.setOnClickListener {
            // при добавлении нового заказ-наряда надо спозиционироваться на нем
            viewModel.selectedWorkOrder = null
            (requireActivity() as Callbacks).onAddWorkOrder(REQUEST_KEY_NEW_ORDER_ID_PARAM, ARG_NAME_NEW_ORDER_ID_PARAM)
        }

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onExitWorkOrderList()
                }
            }
        )
    }

    private fun observeViewModel() {
        viewModel.workOrderListLiveData.observe(viewLifecycleOwner) { list ->
            workOrderListAdapter?.submitList(list) {
                // когда DiffCallback сделает свою работу, тогда itemCount будет выдавать правильное значение
                // и можно позиционироваться:
                if (viewModel.scrollDownto) {
                    binding.workOrderRecyclerView.scrollToPosition(list.size - 1)
                    viewModel.scrollDownto = false
                } else {
                    viewModel.newWorkOrderId?.let { newId ->
                        workOrderListAdapter?.let { listAdapter ->
                            val scrollPosition = viewModel.getPosition(newId)
                            scrollPosition?.let {
                                // при добавлении нового заказ-наряда надо спозиционироваться на нем
                                binding.workOrderRecyclerView.scrollToPosition(scrollPosition)
                                viewModel.newWorkOrderId = null
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.work_order_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_menu_item -> {
                onSearch()
                return true
            }

            R.id.exit_menu_item -> {
                onExitWorkOrderList()
                return true
            }

            R.id.synchronize_data_menu_item -> {
                viewModel.scrollDownto = true
                // запустить фрагмент, где будет синхронизация данных из 1С
                (requireActivity() as Callbacks).onLaunchDataSynchronization()
                return true
            }

            R.id.settings_menu_item -> {
                MessageDialogFragment.newInstance(
                    getString(R.string.under_constraction)
                )
                    .show(childFragmentManager, null)
                return true
            }

            R.id.about_menu_item -> {
                // запустить фрагмент, где будет о программе
                MessageDialogFragment.newInstance(
                    getString(R.string.about_application, getString(R.string.app_name), BuildConfig.VERSION_NAME)
                )
                    .show(childFragmentManager, null)
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun onSearch() {
        SearchWorkOrderFragment
            .newInstance(
                viewModel.searchWorkOrderData,
                REQUEST_SEARCH_WO_DATA_PICKER_KEY,
                ARG_SEARCH_WO_DATA
            )
            .show(childFragmentManager, REQUEST_SEARCH_WO_DATA_PICKER_KEY)
    }

    private fun setupWorkOrderRecyclerView(view: View) {
        if (workOrderListAdapter == null) {
            val workOrderListListener = object : WorkOrderListListener {
                override fun onItemClick(workOrder: WorkOrder) {
                    (requireActivity() as Callbacks).onEditWorkOrder(workOrder.id)
                }
            }
            workOrderListAdapter = WorkOrderListAdapter(workOrderListListener)
        }

        binding.workOrderRecyclerView.adapter = workOrderListAdapter
        // вместо app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" который был в XML
        binding.workOrderRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.workOrderRecyclerView.setHasFixedSize(true)
    }

    private fun onExitWorkOrderList() {
        QuestionDialogFragment
            .newInstance(
                getString(R.string.exit_from_app_question, getString(R.string.app_name)),
                REQUEST_EXIT_QUESTION_KEY,
                ARG_ANSWER
            )
            .show(childFragmentManager, REQUEST_EXIT_QUESTION_KEY)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            REQUEST_EXIT_QUESTION_KEY -> {
                val exit: Boolean = result.getBoolean(ARG_ANSWER, false)
                if (exit) {
                    activity?.finish()
                }
            }

            REQUEST_SEARCH_WO_DATA_PICKER_KEY -> {
                val searchData: SearchWorkOrderData? = result.getParcelable(ARG_SEARCH_WO_DATA)
                searchData?.let { data ->
                    if (data != viewModel.searchWorkOrderData) {
                        viewModel.searchWorkOrderData = data
                        viewModel.selectedWorkOrder = null
                        viewModel.scrollDownto = true
                        setupFilterDescription()
                        viewModel.loadWorkOrderList()
                        observeViewModel()
                    }
                }
            }
        }
    }

    private fun setupFilterDescription() {
        with(binding) {
            if (viewModel.searchWorkOrderDataIsEmpty()) {
                // пользователь сбросил отбор
                filterDescriptionTextView.text = ""
                filterDescriptionTextView.visibility = View.GONE
            } else {
                // пользователь установил отбор
                filterDescriptionTextView.text = getFilterDescription(viewModel.searchWorkOrderData)
                filterDescriptionTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun getFilterDescription(searchWorkOrderData: SearchWorkOrderData): String {
        var description = ""

        if (searchWorkOrderData.numberText.isNotEmpty()) {
            description = description + getString(R.string.number_hint) +
                " " + getString(R.string.contains) + " " + searchWorkOrderData.numberText + "; "
        }

        if (searchWorkOrderData.partnerText.isNotEmpty()) {
            description = description + getString(R.string.partner_hint) +
                " " + getString(R.string.contains) + " " + searchWorkOrderData.partnerText + "; "
        }

        if (searchWorkOrderData.carText.isNotEmpty()) {
            description = description + getString(R.string.car_hint) +
                " " + getString(R.string.contains) + " " + searchWorkOrderData.carText + "; "
        }

        if (searchWorkOrderData.performerText.isNotEmpty()) {
            description = description + getString(R.string.performer_hint) +
                " " + getString(R.string.contains) + " " + searchWorkOrderData.performerText + "; "
        }

        if (searchWorkOrderData.departmentText.isNotEmpty()) {
            description = description + getString(R.string.department_hint) +
                " " + getString(R.string.contains) + " " + searchWorkOrderData.departmentText + "; "
        }

        searchWorkOrderData.dateFrom?.let { fromDate ->
            description = description + getString(R.string.date_from_hint) +
                " " + DateConverter.getDateString(fromDate) + "; "
        }

        searchWorkOrderData.dateTo?.let { toDate ->
            description = description + getString(R.string.date_to_hint) +
                " " + DateConverter.getDateString(toDate) + "; "
        }

        if (description.isNotEmpty()) {
            description = getString(R.string.filter_fragment_title) + ": " + description
        }

        return description
    }

    companion object {
        private const val REQUEST_KEY_NEW_ORDER_ID_PARAM = "request_key_new_order_id_param"
        private const val ARG_NAME_NEW_ORDER_ID_PARAM = "arg_name_new_order_id_param"

        private const val REQUEST_EXIT_QUESTION_KEY = "exit_question_key"
        private const val ARG_ANSWER = "answer"

        private const val REQUEST_SEARCH_WO_DATA_PICKER_KEY = "request_search_wo_data_picker_key"
        private const val ARG_SEARCH_WO_DATA = "search_wo_data_picker"

        fun newInstance(): WorkOrderListFragment {
            return WorkOrderListFragment()
        }
    }
}
