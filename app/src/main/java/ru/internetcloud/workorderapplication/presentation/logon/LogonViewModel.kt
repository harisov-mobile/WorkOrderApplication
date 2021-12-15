package ru.internetcloud.workorderapplication.presentation.logon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase

private const val BEGIN_SIZE = 4

class LogonViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository = AuthRepositoryImpl.get() // требуется инъекция зависимостей!!!

    // ссылки на экземпляры классов Юзе-Кейсов, которые будут использоваться в Вью-Модели:
    private val setAuthParametersUseCase = SetAuthParametersUseCase(repository)
    private val checkAuthParametersUseCase = CheckAuthParametersUseCase(repository)

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _errorInputServer = MutableLiveData<Boolean>()
    val errorInputServer: LiveData<Boolean>
        get() = _errorInputServer

    private val _errorInputLogin = MutableLiveData<Boolean>()
    val errorInputLogin: LiveData<Boolean>
        get() = _errorInputLogin

    private val _errorInputPassword = MutableLiveData<Boolean>()
    val errorInputPassword: LiveData<Boolean>
        get() = _errorInputPassword

    private val _errorAuthorization = MutableLiveData<Boolean>()
    val errorAuthorization: LiveData<Boolean>
        get() = _errorAuthorization

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun signin(inputServer: String?, inputLogin: String?, inputPassword: String?) {
        var server = parseText(inputServer)
        val login = parseText(inputLogin)
        val password = parseText(inputPassword)

        val areFieldsValid = validateInput(server, login, password)

        if (areFieldsValid) {
            viewModelScope.launch {

                server = server.lowercase()

                if (server.length >= BEGIN_SIZE) {
                    val firstFourLetters = server.substring(0, BEGIN_SIZE)
                    if (!firstFourLetters.equals("http")) {
                        server = "http://" + server
                    }
                } else {
                    server = "http://" + server
                }

                setAuthParametersUseCase.setAuthParameters(server, login, password)

                // сделать проверку пароля!!!!
                val authResult = checkAuthParametersUseCase.checkAuthorization()
                if (authResult.isAuthorized) {
                    _canContinue.value = true
                } else {
                    _errorMessage.value = authResult.errorMessage
                    _errorAuthorization.value = true
                }
            }
        }
    }

    private fun parseText(input: String?): String {
        return input?.trim() ?: ""
    }

    private fun validateInput(server: String, login: String, password: String): Boolean {
        var result = true
        if (server.isBlank()) {
            _errorInputServer.value = true
            result = false
        }

        if (login.isBlank()) {
            _errorInputLogin.value = true
            result = false
        }

        if (password.isBlank()) {
            _errorInputPassword.value = true
            result = false
        }

        return result
    }

    fun resetErrorInputServer() {
        _errorInputServer.value = false
    }

    fun resetErrorInputLogin() {
        _errorInputLogin.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputPassword.value = false
    }

    fun resetCanContinue() {
        _canContinue.value = false
    }
}
