package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentWorkOrderBinding
import ru.internetcloud.workorderapplication.domain.common.ScreenMode

class WorkOrderFragment : Fragment() {

    private var _binding: FragmentWorkOrderBinding? = null
    private val binding: FragmentWorkOrderBinding
    get() = _binding ?: throw RuntimeException("Error FragmentWorkOrderBinding is NULL")

    private lateinit var viewModel: WorkOrderViewModel

    private var screenMode: ScreenMode? = null
    private var workOrderId: Int? = null

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

        fun newInstanceEditWorkOrder(workOrderId: Int): WorkOrderFragment {
            val instance = WorkOrderFragment()
            val args = Bundle()
            args.putSerializable(ARG_SCREEN_MODE, ScreenMode.EDIT)
            args.putInt(ARG_WORK_ORDER_ID, workOrderId)
            instance.arguments = args
            return instance
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkArgs()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentWorkOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(WorkOrderViewModel::class.java)

        launchCorrectMode()

        viewModel.loadRepairTypes()

        observeViewModel()
    }

    private fun observeViewModel() {
        // подписка на ошибки
        viewModel.errorInputNumber.observe(viewLifecycleOwner) {
            val message = if (it) {
                // Toast.makeText(context, getString(R.string.error_input_name), Toast.LENGTH_SHORT).show()
                getString(R.string.error_input_number)
            } else {
                null
            }
            binding.numberTextInputLayout.error = message
        }

        // подписка на успешное завершение сохранения
        viewModel.canFinish.observe(viewLifecycleOwner) {
            Toast.makeText(context, getString(R.string.success_saved), Toast.LENGTH_SHORT).show()
        }

        viewModel.repairTypes.observe(viewLifecycleOwner) {
            Log.i("rustam", it.toString())
        }
    }

    private fun launchCorrectMode() {
        when (screenMode) {
            ScreenMode.EDIT -> launchEditMode()
            ScreenMode.ADD -> launchAddMode()
        }
    }

    private fun checkArgs() {

        val args = requireArguments()
        if (!args.containsKey(ARG_SCREEN_MODE)) {
            throw RuntimeException("Param mode is absent")
        }

        val mode = args.getSerializable(ARG_SCREEN_MODE) as ScreenMode
        if (mode != ScreenMode.EDIT && mode != ScreenMode.ADD) {
            throw IllegalStateException("Uknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == ScreenMode.EDIT) {
            workOrderId = args.getInt(ARG_WORK_ORDER_ID)
        }
    }

    private fun launchEditMode() {
        workOrderId?.let {
            viewModel.loadWorkOrder(it)
        }

        viewModel.workOrder.observe(viewLifecycleOwner) {
            order ->
            binding.numberEditText.setText(order.number)
            binding.dateEditText.setText(order.date.toString())
        }

        binding.saveButton.setOnClickListener {
            viewModel.updateWorkOrder(binding.numberEditText.text?.toString())
        }
    }

    private fun launchAddMode() {
        binding.saveButton.setOnClickListener {
            viewModel.addWorkOrder(binding.numberEditText.text?.toString())
        }
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        binding.numberEditText.addTextChangedListener(object : TextWatcher {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
