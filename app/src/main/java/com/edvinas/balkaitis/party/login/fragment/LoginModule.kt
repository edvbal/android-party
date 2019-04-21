package com.edvinas.balkaitis.party.login.fragment

import com.edvinas.balkaitis.party.login.mvp.LoginContract
import com.edvinas.balkaitis.party.login.mvp.LoginPresenter
import com.edvinas.balkaitis.party.data.api.login.LoginService
import com.edvinas.balkaitis.party.data.repository.TokenRepository
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.utils.schedulers.Main
import com.edvinas.balkaitis.party.utils.scopes.FragmentScope
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
abstract class LoginModule {
    @Module
    companion object {
        @FragmentScope @JvmStatic @Provides
        fun providePresenter(
            loginService: LoginService,
            tokenRepository: TokenRepository,
            serversService: ServersService,
            @Main mainScheduler: Scheduler
        ): LoginContract.Presenter {
            return LoginPresenter(mainScheduler, loginService, tokenRepository, serversService)
        }
    }
}
