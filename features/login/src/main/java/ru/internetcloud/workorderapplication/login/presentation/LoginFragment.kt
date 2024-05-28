package ru.internetcloud.workorderapplication.login.presentation

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider
import ru.internetcloud.workorderapplication.common.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.common.presentation.dialog.QuestionDialogFragment
import ru.internetcloud.workorderapplication.common.presentation.util.launchAndCollectIn
import ru.internetcloud.workorderapplication.login.R
import ru.internetcloud.workorderapplication.login.databinding.FragmentLoginBinding
import ru.internetcloud.workorderapplication.login.presentation.navigation.LoginDirections
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login), FragmentResultListener {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var buildConfigFieldsProvider: BuildConfigFieldsProvider

    @Inject
    lateinit var navigationApi: NavigationApi<LoginDirections>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupClickListeners()
        setupFragmentResultListeners()
        interceptExit() // перехват нажатия кнопки "Back" (задаем вопрос "Do you want to exit the app?")
        observeViewModel()
    }

    override fun onStart() {
        super.onStart()

        // doOnTextChanged или doAfterTextChanged нужно навешивать здесь, а не в onCreateView или onViewCreated,
        // т.к. там еще не восстановлено EditText и слушатели будут "дергаться" лишний раз
        // когда ОС Андроид сама восстановит состояние EditText
        setupOnTextChangedListeners()
    }

    private fun setupClickListeners() {
        binding.enterButton.setOnClickListener {
            onEnterButtonPressed()
        }

        // обработчик на кнопку в клавиатуре android:imeOptions="actionDone"
        binding.passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener onEnterButtonPressed()
            }
            return@setOnEditorActionListener false
        }

        binding.cancelButton.setOnClickListener {
            onExitApplication()
        }
    }

    private fun setupUi() {
        binding.versionTextView.text = getString(R.string.version, buildConfigFieldsProvider.get().versionName)
    }

    private fun onEnterButtonPressed(): Boolean {
        viewModel.handleEvent(LoginEvent.OnSignIn)
        return false // чтобы клавиатура скрылась с экрана
    }

    private fun setupOnTextChangedListeners() {
        binding.serverEditText.doAfterTextChanged { text ->
            viewModel.handleEvent(LoginEvent.OnServerChange(server = text.toString()))
        }
        binding.loginEditText.doAfterTextChanged { text ->
            viewModel.handleEvent(LoginEvent.OnLoginChange(login = text.toString()))
        }
        binding.passwordEditText.doAfterTextChanged { text ->
            viewModel.handleEvent(LoginEvent.OnPasswordChange(password = text.toString()))
        }
    }

    private fun observeViewModel() {
        viewModel.state.launchAndCollectIn(viewLifecycleOwner) { currentState ->
            binding.enterProgressBar.isVisible = currentState.entering
            binding.enterButton.isEnabled = !currentState.entering
            binding.cancelButton.isEnabled = !currentState.entering
            binding.serverEditText.isEnabled = !currentState.entering
            binding.loginEditText.isEnabled = !currentState.entering
            binding.passwordEditText.isEnabled = !currentState.entering

            // чтобы "doOnTextChanged" или "doAfterTextChanged" не дергались и не зацикливалось приложение
            if (!currentState.loginParams.server.equals(binding.serverEditText.text.toString())) {
                binding.serverEditText.setText(currentState.loginParams.server)
            }
            if (!currentState.loginParams.login.equals(binding.loginEditText.text.toString())) {
                binding.loginEditText.setText(currentState.loginParams.login)
            }
            if (!currentState.loginParams.password.equals(binding.passwordEditText.text.toString())) {
                binding.passwordEditText.setText(currentState.loginParams.password)
            }

            binding.serverTextInputLayout.error = if (currentState.errorInputServer) {
                getString(R.string.error_input_server)
            } else {
                null
            }
            binding.loginTextInputLayout.error = if (currentState.errorInputLogin) {
                getString(R.string.error_input_login)
            } else {
                null
            }
            binding.passwordTextInputLayout.error = if (currentState.errorInputPassword) {
                getString(R.string.error_input_password)
            } else {
                null
            }

            if (currentState.canContinue) {
                // подписка на завершение экрана:
                // запустить фрагмент, где будет синхронизация данных из 1С
                navigationApi.navigate(LoginDirections.ToDataSynchronization)
            }

            if (currentState.canContinueDemoMode) {
                // демо-режим - переход в список Заказ-нарядов:
                // запустить фрагмент, где будет показан список демо-заказ-нарядов
                navigationApi.navigate(LoginDirections.ToWorkOrders)
            }
        }

        viewModel.screenEventFlow.launchAndCollectIn(viewLifecycleOwner) { event ->
            when (event) {
                is LoginSideEffectEvent.ShowMessage -> {
                    MessageDialogFragment.newInstance(event.message)
                        .show(childFragmentManager, null)
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
