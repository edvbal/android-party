package com.edvinas.balkaitis.party.servers.fragment

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ServersViewModelTest {
    @get:Rule val rule = InstantTaskExecutorRule()

    @Mock private lateinit var tokenRepository: TokenRepository
    @Mock private lateinit var serversRepository: ServersRepository

    private val testScheduler = TestScheduler()

    @Test
    fun onLogoutClicked_initially_postsLoadingStateVisible() {
        given(serversRepository.getServers()).willReturn(Single.never())
        given(serversRepository.deleteServers()).willReturn(Completable.never())
        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)

        viewModel.onLogoutClicked()

        assertEquals(View.VISIBLE, viewModel.observeLoadingState().value)
    }

    @Test
    fun onLogoutClicked_onComplete_postsLoadingStateGoneAndShowLoginEventsAndRemovesToken() {
        given(serversRepository.getServers()).willReturn(Single.never())
        given(serversRepository.deleteServers()).willReturn(Completable.complete())
        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)

        viewModel.onLogoutClicked()
        testScheduler.triggerActions()

        assertEquals(View.GONE, viewModel.observeLoadingState().value)
        assertNotNull(viewModel.observeShowLoginEvent().value)
        verify(tokenRepository).removeToken()
    }

    @Test
    fun onLogoutClicked_onError_postsLoadingStateGone() {
        given(serversRepository.getServers()).willReturn(Single.never())
        given(serversRepository.deleteServers()).willReturn(Completable.error(Throwable()))
        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)

        viewModel.onLogoutClicked()
        testScheduler.triggerActions()

        assertEquals(View.GONE, viewModel.observeLoadingState().value)
    }

    @Test
    fun init_initially_postsLoadingStateVisible() {
        given(serversRepository.getServers()).willReturn(Single.never())

        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)

        assertEquals(View.VISIBLE, viewModel.observeLoadingState().value)
    }

    @Test
    fun init_onSuccess_postsLoadingStateGoneAndServersReceivedEvents() {
        val expectedServers = listOf(mock(ServerEntity::class.java))
        given(serversRepository.getServers()).willReturn(Single.just(expectedServers))

        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)
        testScheduler.triggerActions()

        assertEquals(expectedServers, viewModel.serversReceivedEvent().value)
    }

    @Test
    fun init_onError_postsLoadingStateGoneAndErrorEvents() {
        val throwable = Throwable(ERROR_MESSAGE)
        given(serversRepository.getServers()).willReturn(Single.error(throwable))

        val viewModel = ServersViewModel(testScheduler, tokenRepository, serversRepository)
        testScheduler.triggerActions()

        assertEquals(View.GONE, viewModel.observeLoadingState().value)
        assertEquals(ERROR_MESSAGE, viewModel.observeErrorEvent().value)
    }

    companion object {
        private const val ERROR_MESSAGE = "ups"
    }
}
