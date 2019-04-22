package com.edvinas.balkaitis.party.login.mvp

import com.edvinas.balkaitis.party.data.api.servers.Server
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.login.Authenticator
import com.edvinas.balkaitis.party.utils.mvp.ViewPresenter
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class LoginPresenter(
    private val mainScheduler: Scheduler,
    private val authenticator: Authenticator,
    private val serversService: ServersService
) : LoginContract.Presenter, ViewPresenter<LoginContract.View>() {

    override fun onLoginClicked(username: String, password: String) {
        onView { closeKeyboard() }
        authenticator.authenticate(username, password)
            .observeOn(mainScheduler)
            .doOnSuccess { onView { showFetchingMessage() } }
            .flatMap { token -> serversService.getServers("Bearer $token") }
            .observeOn(mainScheduler)
            .doOnSubscribe { onView { showLoadingView() } }
            .subscribe(::onServersDownloaded, ::onError)
            .addTo(subscription)
    }

    private fun onServersDownloaded(servers: List<Server>) {
        onView { showServers(servers) }
    }

    private fun onError(throwable: Throwable) {
        onView { showError(throwable.localizedMessage) }
        onView { hideLoadingView() }
        Timber.e(throwable)
    }
}
