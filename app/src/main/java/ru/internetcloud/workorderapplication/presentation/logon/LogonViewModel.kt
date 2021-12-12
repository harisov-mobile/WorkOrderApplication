package ru.internetcloud.workorderapplication.presentation.logon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LogonViewModel(private val app: Application) : AndroidViewModel(app) {

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

    init {

    }

    fun login(inputServer: String?, inputLogin: String?, inputPassword: String?) {
        val server = parseText(inputServer)
        val loginName = parseText(inputLogin)
        val password = parseText(inputPassword)

        val areFieldsValid = validateInput(server, loginName, password)

        if (areFieldsValid) {
            viewModelScope.launch {
                // сделать проверку пароля!!!!
                _canContinue.value = true
            }
        }
    }

    private fun parseText(input: String?): String {
        return input?.trim() ?: ""
    }

    private fun validateInput(server: String, loginName: String, password: String): Boolean {
        var result = true
        if (server.isBlank()) {
            _errorInputServer.value = true
            result = false
        }

        if (loginName.isBlank()) {
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
