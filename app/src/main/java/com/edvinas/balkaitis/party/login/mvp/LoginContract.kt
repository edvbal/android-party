package com.edvinas.balkaitis.party.login.mvp

import com.edvinas.balkaitis.party.data.api.servers.Server
import com.edvinas.balkaitis.party.utils.mvp.BasePresenter

interface LoginContract {
    interface View {
        fun showError(message: String)
        fun showLoadingView()
        fun showServers(servers: List<Server>)
        fun hideLoadingView()
    }

    interface Presenter : BasePresenter<View> {
        fun onLoginClicked(username: String, password: String)
    }
}
