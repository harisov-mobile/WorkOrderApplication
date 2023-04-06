package ru.internetcloud.workorderapplication.presentation.logon

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.LoadMockDataUseCase

// TODO Переделать многочисленные LiveData на один State

class LogonViewModel @Inject constructor(
    private val setAuthParametersUseCase: SetAuthParametersUseCase,
    private val checkAuthParametersUseCase: CheckAuthParametersUseCase,
    private val loadMockDataUseCase: LoadMockDataUseCase
) : ViewModel() {

    private val _canContinue = MutableLiveData<Boolean>()
    val canContinue: LiveData<Boolean>
        get() = _canContinue

    private val _demoMode = MutableLiveData<Boolean>()
    val demoMode: LiveData<Boolean>
        get() = _demoMode

    private val _canContinueDemoMode = MutableLiveData<Boolean>()
    val canContinueDemoMode: LiveData<Boolean>
        get() = _canContinueDemoMode

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

                // демо-режим:
                if (server.equals(DEMO_SERVER) && login.equals(DEMO_LOGIN) && password.equals(DEMO_PASSWORD)) {
                    setAuthParametersUseCase.setAuthParameters(server, login, password)
                    _demoMode.value = true
                } else {
                    if (server.length >= BEGIN_SIZE) {
                        val firstFourLetters = server.substring(0, BEGIN_SIZE)
                        if (!firstFourLetters.equals("http")) {
                            server = HTTP_PREFIX + server
                        }
                    } else {
                        server = HTTP_PREFIX + server
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

    fun loadDemoData() {
        viewModelScope.launch {

            loadMockDataUseCase.loadMockData()

            _canContinueDemoMode.value = true
        }
    }

    companion object {
        private const val BEGIN_SIZE = 4
        private const val HTTP_PREFIX = "https://"
        private const val DEMO_SERVER = "demo"
        private const val DEMO_LOGIN = "demo"
        private const val DEMO_PASSWORD = "1"
    }
}
