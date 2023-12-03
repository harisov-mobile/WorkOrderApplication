package ru.internetcloud.workorderapplication.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.repository.AuthorizationPreferencesRepository
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.CheckAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.logonoperation.SetAuthParametersUseCase
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.LoadMockDataUseCase

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val setAuthParametersUseCase: SetAuthParametersUseCase,
    private val checkAuthParametersUseCase: CheckAuthParametersUseCase,
    private val loadMockDataUseCase: LoadMockDataUseCase,
    private val authorizationPreferencesRepository: AuthorizationPreferencesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: StateFlow<UiLoginState> = savedStateHandle.getStateFlow(
        KEY_LOGIN_STATE,
        UiLoginState(
            loginParams = LoginParams(
                server = authorizationPreferencesRepository.getStoredServer(),
                login = authorizationPreferencesRepository.getStoredLogin(),
                password = DEFAULT_STRING_VALUE
            )
        )
    )

    private val screenEventChannel = Channel<LoginSideEffectEvent>(Channel.BUFFERED)
    val screenEventFlow = screenEventChannel.receiveAsFlow()

    private fun signIn() {
        // отобразим ПрогрессБар в кнопке "Enter"
        savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(entering = true)

        var server = parseText(state.value.loginParams.server)
        val login = parseText(state.value.loginParams.login)
        val password = parseText(state.value.loginParams.password)

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
                        savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(
                            canContinue = true,
                            entering = true // специально оставляю заблокированными чтобы не моргали кнопки при переходе
                        )
                    } else {
                        // одноразовый показ сообщения во фрагменте:
                        screenEventChannel.trySend(LoginSideEffectEvent.ShowMessage(message = authResult.errorMessage))
                        // скроем ПрогрессБар в кнопке "Enter"
                        savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(entering = false)
                    }
                }
            }
        } else {
            // скроем ПрогрессБар в кнопке "Enter"
            savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(entering = false)
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
            savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(errorInputServer = true)
            result = false
        }

        if (login.isBlank()) {
            savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(errorInputLogin = true)
            result = false
        }

        if (password.isBlank()) {
            savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(errorInputPassword = true)
            result = false
        }

        return result
    }

    private fun loadDemoData() {
        viewModelScope.launch {
            loadMockDataUseCase.loadMockData()
            savedStateHandle[KEY_LOGIN_STATE] = state.value.copy(canContinueDemoMode = true)
        }
    }

    fun handleEvent(event: LoginEvent) {
        val oldState = state.value

        when (event) {
            is LoginEvent.OnServerChange -> {
                savedStateHandle[KEY_LOGIN_STATE] = oldState.copy(
                    loginParams = oldState.loginParams.copy(
                        server = event.server
                    ),
                    errorInputServer = false
                )
            }

            is LoginEvent.OnLoginChange -> {
                savedStateHandle[KEY_LOGIN_STATE] = oldState.copy(
                    loginParams = oldState.loginParams.copy(
                        login = event.login
                    ),
                    errorInputLogin = false
                )
            }

            is LoginEvent.OnPasswordChange -> {
                savedStateHandle[KEY_LOGIN_STATE] = oldState.copy(
                    loginParams = oldState.loginParams.copy(
                        password = event.password
                    ),
                    errorInputPassword = false
                )
            }

            LoginEvent.OnSignIn -> {
                signIn()
            }
        }
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
