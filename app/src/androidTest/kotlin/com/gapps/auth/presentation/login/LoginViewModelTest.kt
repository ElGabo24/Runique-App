@file:OptIn(ExperimentalFoundationApi::class)

package com.gapps.auth.presentation.login

import androidx.compose.foundation.ExperimentalFoundationApi
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.gapps.auth.data.AuthRepositoryImpl
import com.gapps.auth.data.EmailPatternValidator
import com.gapps.auth.data.LoginRequest
import com.gapps.auth.domain.UserDataValidator
import com.gapps.core.android_test.SessionStorageFake
import com.gapps.core.android_test.TestMockEngine
import com.gapps.core.android_test.loginResponseStub
import com.gapps.core.data.networking.HttpClientFactory
import com.gapps.core.test.MainCoroutineExtension
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteArray
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

class LoginViewModelTest {

    companion object{
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var authRepository: AuthRepositoryImpl
    private lateinit var sessionStorageFake: SessionStorageFake
    private lateinit var mockEngine: TestMockEngine

    @BeforeEach
    fun setup() {
        sessionStorageFake = SessionStorageFake()

        val mockEngineConfig = MockEngineConfig().apply {
            requestHandlers.add{ request ->
                val relativeUrl = request.url.encodedPath
                if(relativeUrl == "/login"){
                    respond(
                        content = ByteReadChannel(
                            text = Json.encodeToString(loginResponseStub)
                        ),
                        headers = headers {
                            set("Content-Type", "application/json")
                        }
                    )
                } else {
                    respond(
                        content = byteArrayOf(),
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }
        }
        mockEngine = TestMockEngine(
            dispatcher = mainCoroutineExtension.testDispatcher,
            mockEngineConfig = mockEngineConfig
        )

        val httpClient = HttpClientFactory(
            sessionStorage = sessionStorageFake
        ).build(mockEngine)
        authRepository = AuthRepositoryImpl(
            httpClient = httpClient,
            sessionStorage = sessionStorageFake
        )

        loginViewModel = LoginViewModel (
            authRepository = authRepository,
            userDataValidator = UserDataValidator(
                patternValidator = EmailPatternValidator()
            )
        )
    }

    @Test
    fun testLogin() = runTest {
        assertThat(loginViewModel.state.canLogin).isFalse()

        loginViewModel.state.email.edit {
            append("gaby04291@gmail.com")
        }
        loginViewModel.state.password.edit {
            append("Roman101@!")
        }

        loginViewModel.onAction(LoginAction.OnLoginClick)

        assertThat(loginViewModel.state.isLoggingIn).isFalse()
        assertThat(loginViewModel.state.email.text.toString()).isEqualTo("gaby04291@gmail.com")
        assertThat(loginViewModel.state.password.text.toString()).isEqualTo("Roman101@!")

        val loginRequest = mockEngine.mockEngine.requestHistory.find {
            it.url.encodedPath == "/login"
        }
        assertThat(loginRequest).isNotNull()
        assertThat(loginRequest!!.headers.contains("x-api-key")).isTrue()

        val loginBody = Json.decodeFromString<LoginRequest>(
            loginRequest.body.toByteArray().decodeToString()
        )
        assertThat(loginBody.email).isEqualTo("gaby04291@gmail.com")
        assertThat(loginBody.password).isEqualTo("Roman101@!")

        val session = sessionStorageFake.get()
        assertThat(session?.userId).isEqualTo(loginResponseStub.userId)
        assertThat(session?.accessToken).isEqualTo(loginResponseStub.accessToken)
        assertThat(session?.refreshToken).isEqualTo(loginResponseStub.refreshToken)


    }

}
