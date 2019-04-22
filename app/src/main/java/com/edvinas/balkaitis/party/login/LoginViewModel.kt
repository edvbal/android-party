package com.edvinas.balkaitis.party.login

import androidx.lifecycle.ViewModel
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import io.reactivex.Scheduler

class LoginViewModel(
        private val mainScheduler: Scheduler,
        private val authenticator: Authenticator,
        private val serversRepository: ServersRepository
) : ViewModel()
