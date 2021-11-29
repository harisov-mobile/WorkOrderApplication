package ru.internetcloud.workorderapplication.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import ru.internetcloud.workorderapplication.R

class WorkOrderListFragment : Fragment() {

    private lateinit var viewModel: WorkOrderListViewModel
    private lateinit var workOrderRecyclerView: RecyclerView
    private lateinit var workOrderListAdapter: WorkOrderListAdapter

    companion object {
        fun newInstance(): WorkOrderListFragment {
            return WorkOrderListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_work_order_list, container, false)

        setupWorkOrderRecyclerView(view)

        return view
    }

    private fun setupWorkOrderRecyclerView(view: View) {
        workOrderRecyclerView = view.findViewById<RecyclerView>(R.id.work_order_recycler_view)
        workOrderListAdapter = WorkOrderListAdapter()
        workOrderRecyclerView.adapter = workOrderListAdapter
        workOrderListAdapter.onWorkOrderClickListener = {
            Toast.makeText(context, "клик ${it.number}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderListViewModel::class.java)
        viewModel.workOrderListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                Log.i("Rustam", it.toString())
                workOrderListAdapter.submitList(it)
            }
        )
    }
}
