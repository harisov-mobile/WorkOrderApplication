package ru.internetcloud.workorderapplication.presentation.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.databinding.FragmentLogonBinding
import ru.internetcloud.workorderapplication.di.InjectingSavedStateViewModelFactory
import ru.internetcloud.workorderapplication.di.ViewModelFactory
import ru.internetcloud.workorderapplication.domain.repository.AuthorizationPreferencesRepository
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment
import ru.internetcloud.workorderapplication.presentation.util.launchAndCollectIn

class LoginFragment : Fragment() {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onLaunchDataSynchronization()

        fun onLaunchWorkOrderList()
    }

    private val component by lazy {
        (requireActivity().application as WorkOrderApp).component
    }

    @Inject
    lateinit var defaultViewModelFactory: dagger.Lazy<InjectingSavedStateViewModelFactory>

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        defaultViewModelFactory.get().create(this, arguments)

    private val viewModel: LoginViewModel by viewModels()

    // даггер:
    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var authorizationPreferencesRepository: AuthorizationPreferencesRepository

    private var _binding: FragmentLogonBinding? = null
    private val binding: FragmentLogonBinding
        get() = _binding ?: error("FragmentWorkOrderBinding is null")

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // даггер:
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLogonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupUi()
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
            activity?.onBackPressed() // это аналог finish для фрагмента, потом замени на findNavController().popBackStack
        }
    }

    private fun setupUi() {
        binding.versionTextView.text = getString(R.string.version, BuildConfig.VERSION_NAME)
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
                (requireActivity() as Callbacks).onLaunchDataSynchronization()
            }

            if (currentState.canContinueDemoMode) {
                // демо-режим - переход в список Заказ-нарядов:
                // запустить фрагмент, где будет показан список демо-заказ-нарядов
                (requireActivity() as Callbacks).onLaunchWorkOrderList()
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

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }
}
