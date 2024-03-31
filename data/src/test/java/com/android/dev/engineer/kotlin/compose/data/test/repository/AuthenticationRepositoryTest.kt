package com.android.dev.engineer.kotlin.compose.data.test.repository

import com.android.dev.engineer.kotlin.compose.data.BuildConfig
import com.android.dev.engineer.kotlin.compose.data.api.authentication.AuthenticationRepository
import com.android.dev.engineer.kotlin.compose.data.api.authentication.AuthenticationRepositoryImpl
import com.android.dev.engineer.kotlin.compose.data.domain.network.NewSession
import com.android.dev.engineer.kotlin.compose.data.domain.network.Session
import com.android.dev.engineer.kotlin.compose.data.extension.MoshiAdapterExt.fromJson
import com.android.dev.engineer.kotlin.compose.data.extension.MoshiAdapterExt.toJson
import com.android.dev.engineer.kotlin.compose.data.extension.toTheMovieApi
import com.android.dev.engineer.kotlin.compose.data.fake.domain.SessionFake.createNewSession
import com.android.dev.engineer.kotlin.compose.data.fake.domain.SessionFake.createSession
import com.android.dev.engineer.kotlin.compose.data.fake.use_case.ApiErrorHandlingUseCaseFake
import com.android.dev.engineer.kotlin.compose.data.interceptor.ApiKeyInterceptor.Companion.API_KEY_NAME
import com.android.dev.engineer.kotlin.compose.data.util.FileUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthenticationRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var apiErrorHandlingUseCase: ApiErrorHandlingUseCaseFake

    @Before
    fun setUp() {
        apiErrorHandlingUseCase = ApiErrorHandlingUseCaseFake()
        mockWebServer = MockWebServer()
        mockWebServer.start()
        authenticationRepository = AuthenticationRepositoryImpl(mockWebServer.toTheMovieApi(apiErrorHandlingUseCase))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `verify get new session url`() = runTest {
        val bodyResponse = createNewSession().toJson()
        val mockResponse = MockResponse().setBody(bodyResponse)
        mockWebServer.enqueue(response = mockResponse)
        authenticationRepository.getNewSession(session = createSession())

        val request = mockWebServer.takeRequest()
        val requestUrl = request.requestUrl!!
        assertEquals("POST", request.method)
        assertEquals("/authentication/session/new", requestUrl.encodedPath)
        assertEquals(BuildConfig.API_KEY, requestUrl.queryParameter(name = API_KEY_NAME))
    }

    @Test
    fun `verify get new session request`() = runTest {
        val bodyResponse = createNewSession().toJson()
        val expectedSession = FileUtil.getContent(file = "json/session.json").fromJson<Session>()
        val mockResponse = MockResponse().setBody(body = bodyResponse)
        mockWebServer.enqueue(response = mockResponse)
        authenticationRepository.getNewSession(session = expectedSession)
        val session = mockWebServer.takeRequest().body.readUtf8().fromJson<Session>()
        assertEquals(expectedSession, session)
    }

    @Test
    fun `verify get new session response`() = runTest {
        val bodyResponse = FileUtil.getContent(file = "json/new_session.json")
        val expectedNewSession = bodyResponse.fromJson<NewSession>()
        val mockResponse = MockResponse().setBody(body = bodyResponse)
        mockWebServer.enqueue(response = mockResponse)
        val newSession = authenticationRepository.getNewSession(session = createSession())
        assertEquals(expectedNewSession, newSession)
    }
}