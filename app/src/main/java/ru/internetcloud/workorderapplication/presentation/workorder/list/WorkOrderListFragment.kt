package ru.internetcloud.workorderapplication.presentation.workorder.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.internetcloud.workorderapplication.R

class WorkOrderListFragment : Fragment() {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onAddWorkOrder()

        fun onEditWorkOrder(workOrderId: String)
    }

    private var hostActivity: Callbacks? = null

    private lateinit var viewModel: WorkOrderListViewModel
    private lateinit var workOrderRecyclerView: RecyclerView
    private lateinit var workOrderListAdapter: WorkOrderListAdapter
    private lateinit var addFloatingActionButton: FloatingActionButton

    companion object {
        fun newInstance(): WorkOrderListFragment {
            return WorkOrderListFragment()
        }
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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        hostActivity = context as Callbacks
    }

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    private fun setupWorkOrderRecyclerView(view: View) {
        workOrderRecyclerView = view.findViewById<RecyclerView>(R.id.work_order_recycler_view)
        workOrderListAdapter = WorkOrderListAdapter()
        workOrderRecyclerView.adapter = workOrderListAdapter
        workOrderListAdapter.onWorkOrderClickListener = {
            Toast.makeText(context, "клик ${it.number}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupClickListener() {
//        workOrderListAdapter.onWorkOrderClickListener = { workOrder ->
//            hostActivity?.onEditWorkOrder(workOrder.id)
//        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderListViewModel::class.java)
//        viewModel.workOrderListLiveData.observe(
//            viewLifecycleOwner,
//            Observer {
//                workOrderListAdapter.submitList(it)
//            }
//        )
    }
}
