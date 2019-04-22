package com.edvinas.balkaitis.party.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock private lateinit var authenticator: Authenticator
    @Mock private lateinit var serversRepository: ServersRepository

    private lateinit var viewModel: LoginViewModel

    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        viewModel = LoginViewModel(testScheduler, authenticator, serversRepository)
    }

    @Test
    fun onLoginClicked_postsHideKeyboardEvent() {
        given(authenticator.authenticate("", "")).willReturn(Single.never())

        viewModel.onLoginClicked("", "")

        assertNotNull(viewModel.observeHideKeyboardEvent().value)
    }

    @Test
    fun onLoginClicked_initiallyPostsLoadingStartEvent() {
        given(authenticator.authenticate("", "")).willReturn(Single.never())

        viewModel.onLoginClicked("", "")

        assertNotNull(viewModel.observeLoadingStartEvent().value)
    }

    @Test
    fun onLoginClicked_whenAuthenticationFails_postsErrorAndLoadingStopEvents() {
        val throwable = Throwable(ERROR_MESSAGE)
        given(authenticator.authenticate("", "")).willReturn(Single.error(throwable))

        viewModel.onLoginClicked("", "")
        testScheduler.triggerActions()

        assertEquals(ERROR_MESSAGE, viewModel.observeErrorEvent().value)
        assertNotNull(viewModel.observeLoadingStopEvent().value)
    }

    @Test
    fun onLoginClicked_whenAuthenticationSucceeds_postsFetchStartEvent() {
        given(authenticator.authenticate("", "")).willReturn(Single.just(""))
        given(serversRepository.updateServersDbFromApi()).willReturn(Completable.never())

        viewModel.onLoginClicked("", "")
        testScheduler.triggerActions()

        assertNotNull(viewModel.observeFetchStartEvent().value)
    }

    @Test
    fun onLoginClicked_whenRepositoryUpdatesSuccessfuly_postsLoginCompleteEvent() {
        given(authenticator.authenticate("", "")).willReturn(Single.just(""))
        given(serversRepository.updateServersDbFromApi()).willReturn(Completable.complete())

        viewModel.onLoginClicked("", "")
        testScheduler.triggerActions()

        assertNotNull(viewModel.observeLoginCompleteEvent().value)
    }

    @Test
    fun onLoginClicked_whenRepositoryUpdateFails_postsErrorAndLoadingStopEvents() {
        given(authenticator.authenticate("", "")).willReturn(Single.just(""))
        val throwable = Throwable(ERROR_MESSAGE)
        given(serversRepository.updateServersDbFromApi()).willReturn(Completable.error(throwable))

        viewModel.onLoginClicked("", "")
        testScheduler.triggerActions()

        assertEquals(ERROR_MESSAGE, viewModel.observeErrorEvent().value)
        assertNotNull(viewModel.observeLoadingStopEvent().value)
    }

    companion object {
        private const val ERROR_MESSAGE = "ups"
    }
}
