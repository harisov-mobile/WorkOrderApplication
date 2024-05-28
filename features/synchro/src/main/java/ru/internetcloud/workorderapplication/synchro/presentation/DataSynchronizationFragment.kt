package ru.internetcloud.workorderapplication.synchro.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider
import ru.internetcloud.workorderapplication.common.domain.common.UpdateState
import ru.internetcloud.workorderapplication.common.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.synchro.R
import ru.internetcloud.workorderapplication.synchro.databinding.FragmentDataSynchronizationBinding
import ru.internetcloud.workorderapplication.synchro.presentation.navigation.SynchroDirections
import javax.inject.Inject

@AndroidEntryPoint
class DataSynchronizationFragment : Fragment(R.layout.fragment_data_synchronization), FragmentResultListener {

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
        setupFragmentResultListeners()
        interceptExit() // перехват нажатия кнопки "Back" (задаем вопрос "Do you want to exit the app?")
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
                                ru.internetcloud.workorderapplication.core.brandbook.R.color.dark_green
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
                                ru.internetcloud.workorderapplication.core.brandbook.R.color.dark_yellow
                            )
                        )

                        binding.errorMessageTextView.text = state.exception.message.toString()
                    }
                }
            }
        }
    }

    private fun interceptExit() {
        // перехват нажатия кнопки "Back"
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onExitApplication()
                }
            }
        )
    }

    private fun onExitApplication() {
        QuestionDialogFragment
            .newInstance(
                getString(ru.internetcloud.workorderapplication.common.R.string.exit_from_app_question,
                    getString(ru.internetcloud.workorderapplication.common.R.string.app_name)),
                REQUEST_KEY_EXIT_QUESTION,
                ARG_NAME_EXIT_QUESTION
            )
            .show(childFragmentManager, REQUEST_KEY_EXIT_QUESTION)
    }

    private fun setupFragmentResultListeners() {
        // чтобы получать от дочерних диалоговых фрагментов информацию
        childFragmentManager.setFragmentResultListener(REQUEST_KEY_EXIT_QUESTION, viewLifecycleOwner, this)
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            // ответ на вопрос: "Выйти из приложения?"
            REQUEST_KEY_EXIT_QUESTION -> {
                val exit: Boolean = result.getBoolean(ARG_NAME_EXIT_QUESTION, false)
                if (exit) {
                    activity?.finish()
                }
            }
        }
    }

    companion object {
        // эти константы нужны для диалогового окна - "Выйти из приложения?"
        private val REQUEST_KEY_EXIT_QUESTION = "request_key_exit_question"
        private val ARG_NAME_EXIT_QUESTION = "arg_name_exit_question"
    }
}
