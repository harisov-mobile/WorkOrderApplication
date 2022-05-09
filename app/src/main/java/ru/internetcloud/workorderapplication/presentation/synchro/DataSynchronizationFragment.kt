package ru.internetcloud.workorderapplication.presentation.synchro

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.databinding.FragmentDataSynchronizationBinding
import ru.internetcloud.workorderapplication.domain.common.OperationMode
import ru.internetcloud.workorderapplication.presentation.ViewModelFactory
import javax.inject.Inject

class DataSynchronizationFragment : Fragment() {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onLaunchWorkOrderList()
    }

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    private var hostActivity: Callbacks? = null
    private lateinit var viewModel: DataSynchronizationFragmentViewModel

    private var _binding: FragmentDataSynchronizationBinding? = null
    private val binding: FragmentDataSynchronizationBinding
        get() = _binding ?: throw RuntimeException("Error DataSynchronizationFragmentBinding is NULL")

    companion object {
        fun newInstance(): DataSynchronizationFragment {
            return DataSynchronizationFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as Callbacks

        // даггер:
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDataSynchronizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DataSynchronizationFragmentViewModel::class.java)

        binding.versionTextView.text = getString(R.string.version, BuildConfig.VERSION_NAME)

        binding.okButton.setOnClickListener {
            hostActivity?.onLaunchWorkOrderList() // запустить фрагмент, где будет список заказ-нарядов
        }

        binding.exitButton.setOnClickListener {
            activity?.onBackPressed() // это finish - закрыть приложение
        }

        observeViewModel()

        viewModel.synchonizeData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    private fun observeViewModel() {
        // подписка на ошибку:
        viewModel.errorSynchronization.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.exitButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.fail_synchronization)
                    binding.synchroResultTextView.setTextColor(Color.RED)
                }
            }
        }

        viewModel.currentSituation.observe(viewLifecycleOwner) { currentOperationMode ->
            context?.let { currentContext ->
                binding.currentSituationTextView.text = getOperationText(currentOperationMode)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorText ->
            context?.let { currentContext ->
                binding.errorMessageTextView.text = errorText
            }
        }

        viewModel.uploadResult.observe(viewLifecycleOwner) { result ->
            context?.let { currentContext ->
                if (result.isSuccess) {
                    if (result.amountOfModifiedWorkOrders == 0) {
                        binding.uploadResultTextView.text = getString(R.string.no_work_orders_to_upload)
                    } else {
                        binding.uploadResultTextView.text = getString(R.string.success_upload_work_orders)
                    }
                } else {
                    binding.uploadResultTextView.text = getString(R.string.fail_upload_work_orders) + " " + result.errorMessage
                }
            }
        }

        // подписка на завершение экрана:
        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.okButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.success_synchronization)
                    context?.let { currentContext ->
                        binding.synchroResultTextView.setTextColor(ContextCompat.getColor(currentContext, R.color.dark_green))
                    }
                }
            }
        }

        viewModel.canContinueWithoutSynchro.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    binding.progressBar.visibility = View.GONE
                    binding.okButton.visibility = View.VISIBLE

                    binding.synchroResultTextView.text = getString(R.string.success_autonomus)
                    context?.let { currentContext ->
                        binding.synchroResultTextView.setTextColor(ContextCompat.getColor(currentContext, R.color.dark_yellow))
                    }
                }
            }
        }
    }

    private fun getOperationText(operationMode: OperationMode): String {
        return when (operationMode) {
            OperationMode.NOTHING -> ""
            OperationMode.GET_CAR_JOB_LIST -> getString(R.string.get_car_job_list)
            OperationMode.PREPARE_CAR_JOB_LIST -> getString(R.string.prepare_car_job_list)
            OperationMode.GET_DEPARTMENT_LIST -> getString(R.string.get_department_list)
            OperationMode.PREPARE_DEPARTMENT_LIST -> getString(R.string.prepare_department_list)
            OperationMode.GET_EMPLOYEE_LIST -> getString(R.string.get_employee_list)
            OperationMode.PREPARE_EMPLOYEE_LIST -> getString(R.string.prepare_employee_list)
            OperationMode.GET_PARTNER_LIST -> getString(R.string.get_partner_list)
            OperationMode.PREPARE_PARTNER_LIST -> getString(R.string.prepare_partner_list)
            OperationMode.GET_CAR_LIST -> getString(R.string.get_car_list)
            OperationMode.PREPARE_CAR_LIST -> getString(R.string.prepare_car_list)
            OperationMode.GET_WORKING_HOUR_LIST -> getString(R.string.get_working_hour_list)
            OperationMode.PREPARE_WORKING_HOUR_LIST -> getString(R.string.prepare_working_hour_list)
            OperationMode.GET_DEFAULT_WORK_ORDER_SETTINGS -> getString(R.string.get_default_wo_settings_list)
            OperationMode.LOAD_WORK_ORDERS -> getString(R.string.load_work_orders)
            OperationMode.LOAD_REPAIR_TYPES -> getString(R.string.load_repair_types)
            OperationMode.UPLOAD_WORK_ORDERS -> getString(R.string.upload_work_orders)
            OperationMode.GET_CAR_MODEL_LIST -> getString(R.string.get_car_model_list)
            OperationMode.PREPARE_CAR_MODEL_LIST -> getString(R.string.prepare_car_model_list)
        }
    }
}
