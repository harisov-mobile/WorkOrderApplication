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
import javax.inject.Inject
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.databinding.FragmentDataSynchronizationBinding
import ru.internetcloud.workorderapplication.presentation.ViewModelFactory

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

        savedInstanceState ?: let {
            viewModel.synchonizeData()
        }
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

        viewModel.updateState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UpdateState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is UpdateState.Success -> {
                    context?.let { currentContext ->
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.okButton.visibility = View.VISIBLE

                        binding.synchroResultTextView.text = getString(R.string.success_synchronization)
                        binding.synchroResultTextView.setTextColor(
                            ContextCompat.getColor(
                                currentContext,
                                R.color.dark_green
                            )
                        )
                    }

                    if (state.modifiedWorkOrderNumber > 0) {
                        binding.uploadResultTextView.text =
                            getString(R.string.success_upload_work_orders, state.modifiedWorkOrderNumber.toString())
                    }
                }

                is UpdateState.Error -> {
                    context?.let { currentContext ->
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.exitButton.visibility = View.VISIBLE

                        binding.synchroResultTextView.text = getString(R.string.fail_synchronization)
                        binding.synchroResultTextView.setTextColor(Color.RED)

                        binding.errorMessageTextView.text = state.exception.message.toString()
                    }
                }

                is UpdateState.ContinueWithoutSynchro -> {
                    context?.let { currentContext ->
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.okButton.visibility = View.VISIBLE

                        binding.synchroResultTextView.text = getString(R.string.success_autonomus)
                        binding.synchroResultTextView.setTextColor(
                            ContextCompat.getColor(
                                currentContext,
                                R.color.dark_yellow
                            )
                        )

                        binding.errorMessageTextView.text = state.exception.message.toString()
                    }
                }
            }
        }
    }
}
