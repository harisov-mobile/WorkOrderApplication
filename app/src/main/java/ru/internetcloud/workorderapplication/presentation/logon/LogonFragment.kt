package ru.internetcloud.workorderapplication.presentation.logon

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.internetcloud.workorderapplication.R
import ru.internetcloud.workorderapplication.databinding.FragmentLogonBinding
import ru.internetcloud.workorderapplication.domain.common.AuthorizationPreferences
import ru.internetcloud.workorderapplication.presentation.dialog.MessageDialogFragment

class LogonFragment : Fragment() {

    // интерфейс обратного вызова
    interface Callbacks {
        fun onLaunchDataSynchronization()
    }

    private var hostActivity: Callbacks? = null

    private lateinit var viewModel: LogonViewModel

    private var _binding: FragmentLogonBinding? = null
    private val binding: FragmentLogonBinding
        get() = _binding ?: throw RuntimeException("Error FragmentWorkOrderBinding is NULL")

    companion object {
        fun newInstance(): LogonFragment {
            return LogonFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hostActivity = context as Callbacks
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLogonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LogonViewModel::class.java)

        binding.enterButton.setOnClickListener {
            binding.enterButton.isEnabled = false
            viewModel.signin(
                binding.serverEditText.text?.toString(),
                binding.loginEditText.text?.toString(),
                binding.passwordEditText.text?.toString()
            )
        }

        binding.cancelButton.setOnClickListener {
            activity?.onBackPressed() // это аналог finish для фрагмента
        }

        savedInstanceState ?: let {
            initTextInputEditText()
        }

        observeViewModel()
    }

    override fun onStart() {
        super.onStart()

        // TextWatcher нужно навешивать здесь, а не в onCreate или onCreateView, т.к. там еще не восстановлено
        // EditText и слушатели будут "дергаться" лишний раз
        binding.serverEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputServer()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })

        binding.loginEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputLogin()
            }

            override fun afterTextChanged(p0: Editable?) {
                //
            }
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputPassword()
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

    override fun onDetach() {
        super.onDetach()
        hostActivity = null
    }

    private fun observeViewModel() {
        // подписка на ошибки
        viewModel.errorInputServer.observe(viewLifecycleOwner) {
            val message = if (it) {
                binding.enterButton.isEnabled = true
                getString(R.string.error_input_server)
            } else {
                null
            }
            binding.serverTextInputLayout.error = message
        }

        viewModel.errorInputLogin.observe(viewLifecycleOwner) {
            val message = if (it) {
                binding.enterButton.isEnabled = true
                getString(R.string.error_input_login)
            } else {
                null
            }
            binding.loginTextInputLayout.error = message
        }

        viewModel.errorInputPassword.observe(viewLifecycleOwner) {
            val message = if (it) {
                binding.enterButton.isEnabled = true
                getString(R.string.error_input_password)
            } else {
                null
            }
            binding.passwordTextInputLayout.error = message
        }

        viewModel.errorAuthorization.observe(viewLifecycleOwner) {
            // показать AlertDialog об ошибке авторизации!!!
            // Toast.makeText(context, viewModel.errorMessage.value, Toast.LENGTH_LONG).show()
            val errorMessage = viewModel.errorMessage.value ?: ""
            MessageDialogFragment
                .newInstance(errorMessage)
                .show(childFragmentManager, null)
            binding.enterButton.isEnabled = true
        }

        // подписка на завершение экрана:
        viewModel.canContinue.observe(viewLifecycleOwner) {
            if (it) {
                context?.let { currentContext ->
                    val server = binding.serverEditText.text?.toString()
                    server?.let { serverAddress ->
                        AuthorizationPreferences.setStoredServer(currentContext.applicationContext, server)
                    }
                    val login = binding.loginEditText.text?.toString()
                    login?.let { loginName ->
                        AuthorizationPreferences.setStoredLogin(currentContext.applicationContext, loginName)
                    }
                }
                viewModel.resetCanContinue()
                hostActivity?.onLaunchDataSynchronization() // запустить фрагмент, где будет сихнронизация данных из 1С
            }
        }
    }

    private fun initTextInputEditText() {
        context?.let {
            // presentation - слой напрямую полез в домайн - слой! разобраться...
            binding.serverEditText.setText(AuthorizationPreferences.getStoredServer(it.applicationContext))
            binding.loginEditText.setText(AuthorizationPreferences.getStoredLogin(it.applicationContext))
        }
    }
}
