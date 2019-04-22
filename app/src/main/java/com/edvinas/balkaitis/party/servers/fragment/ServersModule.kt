package com.edvinas.balkaitis.party.servers.fragment

import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.servers.list.ServersAdapter
import com.edvinas.balkaitis.party.servers.list.ServersViewHolderFactory
import com.edvinas.balkaitis.party.servers.mvp.ServersContract
import com.edvinas.balkaitis.party.servers.mvp.ServersPresenter
import com.edvinas.balkaitis.party.utils.schedulers.Main
import com.edvinas.balkaitis.party.utils.scopes.FragmentScope
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler

@Module
abstract class ServersModule {
    @Module
    companion object {
        @FragmentScope @JvmStatic @Provides
        fun providePresenter(
                tokenRepository: TokenRepository,
                @Main mainScheduler: Scheduler,
                repository: ServersRepository
        ): ServersContract.Presenter {
            return ServersPresenter(mainScheduler, tokenRepository, repository)
        }

        @JvmStatic @Provides
        fun provideServersAdapter(): ServersAdapter = ServersAdapter(ServersViewHolderFactory())
    }
}
