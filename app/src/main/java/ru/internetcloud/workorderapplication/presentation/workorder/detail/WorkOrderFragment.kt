package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.UUID
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.common.ScreenMode

class WorkOrderFragment : Fragment() {
    private lateinit var numberEditText: EditText
    private lateinit var dateEditText: EditText

    private lateinit var viewModel: WorkOrderViewModel

    private var screenMode: ScreenMode? = null
    private var workOrderId: UUID? = null

    companion object {

        const val ARG_SCREEN_MODE = "screen_mode"
        const val ARG_WORK_ORDER_ID = "work_order_id"

        fun newInstanceAddWorkOrder(): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCREEN_MODE, ScreenMode.ADD)
            instance.arguments = args
            return instance
        }

        fun newInstanceEditWorkOrder(workOrderId: UUID): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCREEN_MODE, ScreenMode.EDIT)
            args.putSerializable(ARG_WORK_ORDER_ID, workOrderId)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_work_order, container, false)

        initViews(view)

        checkArgs()

        viewModel = ViewModelProvider(this).get(WorkOrderViewModel::class.java)

        when (screenMode) {
            ScreenMode.EDIT -> launchEditMode()
            ScreenMode.ADD -> launchAddMode()
        }

        return view
    }

    private fun initViews(view: View) {
        numberEditText = view.findViewById<EditText>(R.id.number_edit_text)
        dateEditText = view.findViewById<EditText>(R.id.date_edit_text)
    }

    private fun checkArgs() {
        val mode = arguments?.getSerializable(ARG_SCREEN_MODE) ?: throw IllegalStateException("Param mode is absent")
        screenMode = mode as ScreenMode
        if (screenMode == ScreenMode.EDIT) {
            workOrderId = arguments?.getSerializable(ARG_WORK_ORDER_ID) as UUID
        }
    }

    private fun launchEditMode() {

    }

    private fun launchAddMode() {

    }
}
