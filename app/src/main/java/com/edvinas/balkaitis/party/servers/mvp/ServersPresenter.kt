package com.edvinas.balkaitis.party.servers.mvp

import android.view.View
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.utils.mvp.ViewPresenter
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class ServersPresenter(
        private val mainScheduler: Scheduler,
        private val tokenRepository: TokenRepository,
        private val serversRepository: ServersRepository
) : ServersContract.Presenter, ViewPresenter<ServersContract.View>() {
    override fun onLogoutClicked() {
        serversRepository.deleteServers()
                .subscribe({}, Timber::e)
                .addTo(subscription)
        tokenRepository.removeToken()
        onView { showLogin() }
    }

    override fun onCreated() {
        onView { initialiseList() }

        serversRepository.getServers()
                .observeOn(mainScheduler)
                .doOnSubscribe { onView { setLoading(View.VISIBLE) } }
                .doOnSuccess { onView { setLoading(View.GONE) } }
                .doOnError { onView { setLoading(View.GONE) } }
                .subscribe(::onServersReceived, ::onServersDownloadFailed)
                .addTo(subscription)
    }

    private fun onServersReceived(servers: List<ServerEntity>) {
        onView { populateServers(servers) }
    }

    private fun onServersDownloadFailed(throwable: Throwable) {
        Timber.e(throwable)
        onView { showError(throwable.localizedMessage) }
    }
}
