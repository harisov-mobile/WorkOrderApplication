package ru.internetcloud.workorderapplication.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.di.AssistedSavedStateViewModelFactory
import ru.internetcloud.workorderapplication.domain.repository.AuthorizationPreferencesRepository
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.LoadMockDataUseCase

class LoginViewModel @AssistedInject constructor(
    private val setAuthParametersUseCase: SetAuthParametersUseCase,
    private val checkAuthParametersUseCase: CheckAuthParametersUseCase,
    private val loadMockDataUseCase: LoadMockDataUseCase,
    private val authorizationPreferencesRepository: AuthorizationPreferencesRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<LoginViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): LoginViewModel
    }

    private val _state = savedStateHandle.getLiveData(
        KEY_LOGIN_STATE,
        UiLoginState(
            server = authorizationPreferencesRepository.getStoredServer(),
            login = authorizationPreferencesRepository.getStoredLogin(),
            password = DEFAULT_STRING_VALUE
        )
    )

    val state: LiveData<UiLoginState> // ToDo сделать на Flow
        get() = _state

    fun signin() {
        state.value?.let { currentState ->
            var server = parseText(currentState.server)
            val login = parseText(currentState.login)
            val password = parseText(currentState.password)

            val areFieldsValid = validateInput(server, login, password)

            if (areFieldsValid) {
                // сохранить имя сервера и логин в SharedPreferences (хеш сохраняется в другом месте при удачном входе):
                saveToSharedPreferences(server = server, login = login)

                viewModelScope.launch {
                    server = server.lowercase()

                    // демо-режим:
                    if (server.equals(DEMO_SERVER) && login.equals(DEMO_LOGIN) && password.equals(DEMO_PASSWORD)) {
                        setAuthParametersUseCase.setAuthParameters(server, login, password)
                        loadDemoData()
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

                        // проверка пароля:
                        val authResult = checkAuthParametersUseCase.checkAuthorization()
                        if (authResult.isAuthorized) {
                            _state.value = _state.value?.copy(canContinue = true) ?: UiLoginState(canContinue = true)
                        } else {
                            _state.value = _state.value?.copy(
                                errorAuthorization = true,
                                errorMessage = authResult.errorMessage
                            ) ?: UiLoginState(
                                errorAuthorization = true,
                                errorMessage = authResult.errorMessage
                            )
                        }
                    }
                }
            }
        }
    }

    private fun saveToSharedPreferences(server: String, login: String) {
        authorizationPreferencesRepository.setStoredServer(server)
        authorizationPreferencesRepository.setStoredLogin(login)
    }

    private fun parseText(input: String?): String {
        return input?.trim() ?: DEFAULT_STRING_VALUE
    }

    private fun validateInput(server: String, login: String, password: String): Boolean {
        var result = true
        if (server.isBlank()) {
            _state.value = _state.value?.copy(errorInputServer = true) ?: UiLoginState(errorInputServer = true)
            result = false
        }

        if (login.isBlank()) {
            _state.value = _state.value?.copy(errorInputLogin = true) ?: UiLoginState(errorInputLogin = true)
            result = false
        }

        if (password.isBlank()) {
            _state.value = _state.value?.copy(errorInputPassword = true) ?: UiLoginState(errorInputPassword = true)
            result = false
        }

        return result
    }

    fun loadDemoData() {
        viewModelScope.launch {
            loadMockDataUseCase.loadMockData()
            _state.value = _state.value?.copy(canContinueDemoMode = true) ?: UiLoginState(canContinueDemoMode = true)
        }
    }

    fun setServer(server: String) {
        _state.value = _state.value?.copy(server = server, errorInputServer = false) ?: UiLoginState(server = server)
    }

    fun setLogin(login: String) {
        _state.value = _state.value?.copy(login = login, errorInputLogin = false) ?: UiLoginState(login = login)
    }

    fun setPassword(password: String) {
        _state.value = _state.value?.copy(
            password = password,
            errorInputPassword = false
        ) ?: UiLoginState(password = password)
    }

    fun resetErrorAuthorization() {
        _state.value = _state.value?.copy(
            errorAuthorization = false,
            errorMessage = DEFAULT_STRING_VALUE
        ) ?: UiLoginState(
            errorAuthorization = false,
            errorMessage = DEFAULT_STRING_VALUE
        )
    }

    companion object {
        private const val BEGIN_SIZE = 4
        private const val HTTP_PREFIX = "https://"
        private const val DEMO_SERVER = "demo"
        private const val DEMO_LOGIN = "demo"
        private const val DEMO_PASSWORD = "1"
        private const val KEY_LOGIN_STATE = "key_login_state"
        private const val DEFAULT_STRING_VALUE = ""
    }
}