package com.edvinas.balkaitis.party.login.mvp

import com.edvinas.balkaitis.party.data.api.login.LoginBody
import com.edvinas.balkaitis.party.data.api.login.LoginService
import com.edvinas.balkaitis.party.data.repository.TokenRepository
import com.edvinas.balkaitis.party.data.api.servers.Server
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.utils.mvp.ViewPresenter
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class LoginPresenter(
    private val mainScheduler: Scheduler,
    private val loginService: LoginService,
    private val tokenRepository: TokenRepository,
    private val serversService: ServersService
) : LoginContract.Presenter, ViewPresenter<LoginContract.View>() {

    override fun onLoginClicked(username: String, password: String) {
        loginService.login(LoginBody(username, password))
            .observeOn(mainScheduler)
            .doOnSuccess { response ->
                tokenRepository.saveToken(response.token)
                onView { showLoadingView() }
            }
            .flatMap { response -> serversService.getServers("Bearer ${response.token}") }
            .observeOn(mainScheduler)
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
