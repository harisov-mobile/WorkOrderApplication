package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import java.util.UUID
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.domain.common.ScreenMode

class WorkOrderFragment : Fragment() {
    private lateinit var numberTextInputLayout: TextInputLayout
    private lateinit var dateTextInputLayout: TextInputLayout
    private lateinit var mileageTextInputLayout: TextInputLayout
    private lateinit var numberEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var mileageEditText: EditText
    private lateinit var saveButton: Button

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

        checkArgs()

        initViews(view)

        viewModel = ViewModelProvider(this).get(WorkOrderViewModel::class.java)

        launchCorrectMode()

        observeViewModel()

        return view
    }

    private fun observeViewModel() {
        // подписка на ошибки
        viewModel.errorInputNumber.observe(viewLifecycleOwner) {
            val message = if (it) {
                //Toast.makeText(context, getString(R.string.error_input_name), Toast.LENGTH_SHORT).show()
                getString(R.string.error_input_name)
            } else {
                null
            }
            numberTextInputLayout.error = message
        }

        // подписка на успешное завершение сохранения
        viewModel.canFinish.observe(viewLifecycleOwner) {
            Toast.makeText(context, getString(R.string.success_saved), Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchCorrectMode() {
        when (screenMode) {
            ScreenMode.EDIT -> launchEditMode()
            ScreenMode.ADD -> launchAddMode()
        }
    }

    private fun initViews(view: View) {
        numberTextInputLayout = view.findViewById<TextInputLayout>(R.id.number_text_input_layout)
        dateTextInputLayout = view.findViewById<TextInputLayout>(R.id.date_text_input_layout)
        mileageTextInputLayout = view.findViewById<TextInputLayout>(R.id.mileage_text_input_layout)

        numberEditText = view.findViewById<EditText>(R.id.number_edit_text)
        dateEditText = view.findViewById<EditText>(R.id.date_edit_text)
        mileageEditText = view.findViewById<EditText>(R.id.mileage_edit_text)
        saveButton = view.findViewById<Button>(R.id.save_button)
    }

    private fun checkArgs() {
        val mode = arguments?.getSerializable(ARG_SCREEN_MODE) ?: throw IllegalStateException("Param mode is absent")
        screenMode = mode as ScreenMode
        if (screenMode == ScreenMode.EDIT) {
            workOrderId = arguments?.getSerializable(ARG_WORK_ORDER_ID) as UUID
        }
    }

    private fun launchEditMode() {
        workOrderId?.let {
            viewModel.loadWorkOrder(it)
        }

        viewModel.workOrder.observe(viewLifecycleOwner) {
            order ->
            numberEditText.setText(order.number)
            dateEditText.setText(order.date.toString())
        }

        saveButton.setOnClickListener {
            viewModel.updateWorkOrder(numberEditText.text?.toString())
        }
    }

    private fun launchAddMode() {
        saveButton.setOnClickListener {
            viewModel.addWorkOrder(numberEditText.text?.toString())
        }
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        numberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputNumber()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })
    }
}
