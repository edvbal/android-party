package com.edvinas.balkaitis.party.servers.mvp

import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.utils.mvp.BasePresenter

interface ServersContract {
    interface View {
        fun populateServers(servers: List<ServerEntity>)
        fun initialiseList()
        fun showLogin()
        fun showError(message: String)
        fun setLoading(isLoading: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun onCreated()
        fun onLogoutClicked()
    }
}
