package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.presentation.ViewModelFactory
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment
import javax.inject.Inject

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

    private val REQUEST_EXIT_QUESTION_KEY = "exit_question_key"
    private val ARG_ANSWER = "answer"

    companion object {
        private const val NOT_FOUND_POSITION = -1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onExitWorkOrderList()
            }
        })

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_work_order_list, container, false)

        addFloatingActionButton = view.findViewById(R.id.add_fab)
        addFloatingActionButton.setOnClickListener {
            viewModel.selectedWorkOrder = null
            hostActivity?.onAddWorkOrder()
        }

        setupWorkOrderRecyclerView(view)
        setupClickListener()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(WorkOrderListViewModel::class.java)
        viewModel.workOrderListLiveData.observe(
            viewLifecycleOwner,
            { list ->
                workOrderListAdapter.submitList(list)

                viewModel.selectedWorkOrder ?: let {
                    val scrollPosition = workOrderListAdapter.itemCount - 1
                    workOrderRecyclerView.scrollToPosition(scrollPosition)
                }
            }
        )

        childFragmentManager.setFragmentResultListener(REQUEST_EXIT_QUESTION_KEY, viewLifecycleOwner, this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.work_order_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
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

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    private fun setupWorkOrderRecyclerView(view: View) {
        workOrderRecyclerView = view.findViewById<RecyclerView>(R.id.work_order_recycler_view)
        workOrderListAdapter = WorkOrderListAdapter()
        workOrderRecyclerView.adapter = workOrderListAdapter
    }

    private fun setupClickListener() {
        workOrderListAdapter.onWorkOrderClickListener = { workOrder ->
            viewModel.selectedWorkOrder = workOrder
            hostActivity?.onEditWorkOrder(workOrder.id)
        }
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
        }
    }

    private fun getPosition(searchedWorkOrder: WorkOrder?, workOrderList: List<WorkOrder>): Int {
        var currentPosition = NOT_FOUND_POSITION
        searchedWorkOrder?.let {
            var isFound = false
            for (currentWorkOrder in workOrderList) {
                currentPosition++
                if (currentWorkOrder.id == searchedWorkOrder.id) {
                    isFound = true
                    break
                }
            }
            if (!isFound) {
                currentPosition = NOT_FOUND_POSITION
            }
        }
        return currentPosition
    }
}
