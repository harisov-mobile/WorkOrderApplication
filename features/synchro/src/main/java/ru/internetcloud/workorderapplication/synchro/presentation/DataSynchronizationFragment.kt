package ru.internetcloud.workorderapplication.synchro.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider
import ru.internetcloud.workorderapplication.common.domain.common.UpdateState
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.synchro.R
import ru.internetcloud.workorderapplication.synchro.databinding.FragmentDataSynchronizationBinding
import ru.internetcloud.workorderapplication.synchro.presentation.navigation.SynchroDirections

@AndroidEntryPoint
class DataSynchronizationFragment : Fragment(R.layout.fragment_data_synchronization) {

    private val binding by viewBinding(FragmentDataSynchronizationBinding::bind)
    private val viewModel by viewModels<DataSynchronizationFragmentViewModel>()

    @Inject
    lateinit var buildConfigFieldsProvider: BuildConfigFieldsProvider

    @Inject
    lateinit var navigationApi: NavigationApi<SynchroDirections>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupUi() {
        binding.versionTextView.text = getString(R.string.version, buildConfigFieldsProvider.get().versionName)
    }

    private fun setupClickListeners() {
        binding.okButton.setOnClickListener {
            // запустить фрагмент, где будет список заказ-нарядов
            navigationApi.navigate(SynchroDirections.ToWorkOrders)
        }

        binding.exitButton.setOnClickListener {
            activity?.onBackPressed() // это finish - закрыть приложение
        }
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
