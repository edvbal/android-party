package com.edvinas.balkaitis.party.servers.mvp

import com.edvinas.balkaitis.party.data.repository.TokenRepository
import com.edvinas.balkaitis.party.data.api.servers.Server
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.utils.mvp.ViewPresenter
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class ServersPresenter(
    private val mainScheduler: Scheduler,
    private val tokenRepository: TokenRepository,
    private val serversService: ServersService
) : ServersContract.Presenter, ViewPresenter<ServersContract.View>() {
    override fun onLogoutClicked() {
        tokenRepository.removeToken()
        onView { showLogin() }
    }

    override fun onCreated(servers: Array<Server>?) {
        onView { setList() }
        servers?.let { nonNullServers ->
            onView { populateServers(nonNullServers) }
        } ?: serversService.getServers("Bearer ${tokenRepository.getToken()}")
                .observeOn(mainScheduler)
                .subscribe(::onServersReceived, ::onServersDownloadFailed)
                .addTo(subscription)
    }

    private fun onServersReceived(servers: List<Server>) {
        onView { populateServers(servers.toTypedArray()) }
    }

    private fun onServersDownloadFailed(throwable: Throwable) {
        Timber.e(throwable)
        onView { showError(throwable.localizedMessage) }
    }
}
