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
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.presentation.dialog.QuestionDialogFragment

class WorkOrderListFragment : Fragment(), FragmentResultListener {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onAddWorkOrder()

        fun onEditWorkOrder(workOrderId: String)

        fun onLaunchDataSynchronization()
    }

    private var hostActivity: Callbacks? = null

    private lateinit var viewModel: WorkOrderListViewModel
    private lateinit var workOrderRecyclerView: RecyclerView
    private lateinit var workOrderListAdapter: WorkOrderListAdapter
    private lateinit var addFloatingActionButton: FloatingActionButton

    private val REQUEST_EXIT_QUESTION_KEY = "exit_question_key"
    private val ARG_ANSWER = "answer"

    companion object {
        fun newInstance(): WorkOrderListFragment {
            return WorkOrderListFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

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
            hostActivity?.onAddWorkOrder()
        }

        setupWorkOrderRecyclerView(view)
        setupClickListener()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderListViewModel::class.java)
        viewModel.workOrderListLiveData.observe(
            viewLifecycleOwner,
            {
                workOrderListAdapter.submitList(it)
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
                hostActivity?.onLaunchDataSynchronization() // запустить фрагмент, где будет сихнронизация данных из 1С
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
}
