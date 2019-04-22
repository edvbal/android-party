package com.edvinas.balkaitis.party.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.utils.mvvm.RxViewModel
import com.edvinas.balkaitis.party.utils.mvvm.SingleLiveEvent
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class LoginViewModel(
        private val mainScheduler: Scheduler,
        private val authenticator: Authenticator,
        private val serversRepository: ServersRepository
) : RxViewModel() {
    private val fetchStartEvent = MutableLiveData<Unit>()
    private val loginCompleteEvent = MutableLiveData<Unit>()
    private val loadingStartEvent = MutableLiveData<Unit>()
    private val hideKeyboardEvent = MutableLiveData<Unit>()
    private val loadingStopEvent = MutableLiveData<Unit>()
    private val errorEvent = SingleLiveEvent<String>()

    fun observeHideKeyboardEvent(): LiveData<Unit> = hideKeyboardEvent
    fun observeLoginCompleteEvent(): LiveData<Unit> = loginCompleteEvent
    fun observeErrorEvent(): LiveData<String> = errorEvent
    fun observeLoadingStartEvent(): LiveData<Unit> = loadingStartEvent
    fun observeLoadingStopEvent(): LiveData<Unit> = loadingStopEvent
    fun observeFetchStartEvent(): LiveData<Unit> = fetchStartEvent

    fun onLoginClicked(username: String, password: String) {
        hideKeyboardEvent.postValue(Unit)
        authenticator.authenticate(username, password)
                .observeOn(mainScheduler)
                .doOnSuccess { fetchStartEvent.postValue(Unit) }
                .flatMapCompletable { serversRepository.updateServersDbFromApi() }
                .observeOn(mainScheduler)
                .doOnSubscribe { loadingStartEvent.postValue(Unit) }
                .subscribe(::onServersDownloaded, ::onError)
                .addTo(subscription)
    }

    private fun onServersDownloaded() {
        loginCompleteEvent.postValue(Unit)
    }

    private fun onError(throwable: Throwable) {
        errorEvent.postValue(throwable.localizedMessage)
        loadingStopEvent.postValue(Unit)
        Timber.e(throwable)
    }
}
