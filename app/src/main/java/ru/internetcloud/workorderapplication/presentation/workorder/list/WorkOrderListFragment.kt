package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.presentation.ViewModelFactory
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.presentation.workorder.search.SearchWorkOrderFragment

class WorkOrderListFragment : Fragment(), FragmentResultListener {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onAddWorkOrder()

        fun onEditWorkOrder(workOrderId: String)

        fun onLaunchDataSynchronization()
    }

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var hostActivity: Callbacks? = null

    private lateinit var viewModel: WorkOrderListViewModel
    private lateinit var workOrderRecyclerView: RecyclerView
    private lateinit var workOrderListAdapter: WorkOrderListAdapter
    private lateinit var addFloatingActionButton: FloatingActionButton
    private lateinit var filterDescriptionTextView: TextView

    companion object {

        private val REQUEST_EXIT_QUESTION_KEY = "exit_question_key"
        private val ARG_ANSWER = "answer"

        private val REQUEST_SEARCH_WO_DATA_PICKER_KEY = "request_search_wo_data_picker_key"
        private val ARG_SEARCH_WO_DATA = "search_wo_data_picker"

        fun newInstance(): WorkOrderListFragment {
            return WorkOrderListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // даггер:
        component.inject(this)

        hostActivity = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_work_order_list, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkOrderListViewModel::class.java)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onExitWorkOrderList()
            }
        })

        setHasOptionsMenu(true)

        filterDescriptionTextView = view.findViewById(R.id.filter_description_text_view)

        addFloatingActionButton = view.findViewById(R.id.add_fab)
        addFloatingActionButton.setOnClickListener {
            // TODO при добавлении нового заказ-наряда надо спозиционироваться на нем,
            // сейчас же просто в конец списка перемещаюсь.
            viewModel.selectedWorkOrder = null
            hostActivity?.onAddWorkOrder()
        }

        setupWorkOrderRecyclerView(view)
        observeViewModel()
        setupFilterDescription()

        childFragmentManager.setFragmentResultListener(REQUEST_EXIT_QUESTION_KEY, viewLifecycleOwner, this)
        childFragmentManager.setFragmentResultListener(REQUEST_SEARCH_WO_DATA_PICKER_KEY, viewLifecycleOwner, this)
    }

    private fun observeViewModel() {
        viewModel.workOrderListLiveData.observe(
            viewLifecycleOwner,
            { list ->
                workOrderListAdapter.submitList(list) {
                    // когда DiffCallback сделает свою работу, тогда itemCount будет выдавать правильное значение
                    // и можно позиционироваться:
                    viewModel.selectedWorkOrder ?: let {
                        val scrollPosition = workOrderListAdapter.itemCount - 1
                        workOrderRecyclerView.scrollToPosition(scrollPosition)
                    }
                }
            }
        )
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
                hostActivity?.onLaunchDataSynchronization() // запустить фрагмент, где будет синхронизация данных из 1С
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

    override fun onDetach() {
        super.onDetach()

        hostActivity = null
    }

    private fun setupWorkOrderRecyclerView(view: View) {
        workOrderRecyclerView = view.findViewById<RecyclerView>(R.id.work_order_recycler_view)

        val workOrderListListener = object : WorkOrderListListener {
            override fun onItemClick(workOrder: WorkOrder) {
                hostActivity?.onEditWorkOrder(workOrder.id)
            }
        }

        workOrderListAdapter = WorkOrderListAdapter(workOrderListListener)
        workOrderRecyclerView.adapter = workOrderListAdapter
        // вместо app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" который был в XML
        workOrderRecyclerView.layoutManager = LinearLayoutManager(context)
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

                        setupFilterDescription()
                        viewModel.loadWorkOrderList()
                        observeViewModel()
                    }
                }
            }
        }
    }

    private fun setupFilterDescription() {
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
}
