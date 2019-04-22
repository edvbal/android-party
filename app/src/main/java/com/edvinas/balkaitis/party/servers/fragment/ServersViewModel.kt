package com.edvinas.balkaitis.party.servers.fragment

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.utils.mvvm.RxViewModel
import com.edvinas.balkaitis.party.utils.mvvm.SingleLiveEvent
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class ServersViewModel(
        private val mainScheduler: Scheduler,
        private val tokenRepository: TokenRepository,
        private val serversRepository: ServersRepository
) : RxViewModel() {
    private val loadingState = MutableLiveData<Int>()

    private val errorEvent = SingleLiveEvent<String>()
    private val showLoginEvent = MutableLiveData<Unit>()
    private val serversReceivedEvent = MutableLiveData<List<ServerEntity>>()

    fun observeLoadingState() = loadingState

    fun observeErrorEvent(): LiveData<String> = errorEvent
    fun observeShowLoginEvent(): LiveData<Unit> = showLoginEvent
    fun serversReceivedEvent(): LiveData<List<ServerEntity>> = serversReceivedEvent

    init {
        serversRepository.getServers()
                .observeOn(mainScheduler)
                .doOnSubscribe { loadingState.postValue(View.VISIBLE) }
                .doOnSuccess { loadingState.postValue(View.GONE) }
                .doOnError { loadingState.postValue(View.GONE) }
                .subscribe(::onServersReceived, ::onServersDownloadFailed)
                .addTo(subscription)
    }

    private fun onServersReceived(servers: List<ServerEntity>) {
        serversReceivedEvent.postValue(servers)
    }

    private fun onServersDownloadFailed(throwable: Throwable) {
        Timber.e(throwable)
        errorEvent.postValue(throwable.localizedMessage)
    }

    fun onLogoutClicked() {
        serversRepository.deleteServers()
                .observeOn(mainScheduler)
                .doOnSubscribe { loadingState.postValue(View.VISIBLE) }
                .doOnComplete { loadingState.postValue(View.GONE) }
                .doOnError { loadingState.postValue(View.GONE) }
                .subscribe(::onDeleteServersSuccess, Timber::e)
                .addTo(subscription)
    }

    private fun onDeleteServersSuccess() {
        tokenRepository.removeToken()
        showLoginEvent.postValue(Unit)
    }
}
