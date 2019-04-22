package com.edvinas.balkaitis.party.data.repository

import android.content.Context
import android.preference.PreferenceManager
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.data.database.ServersDao
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepositoryImpl
import com.edvinas.balkaitis.party.data.repository.token.PreferencesTokenRepository
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.utils.network.NetworkChecker
import com.edvinas.balkaitis.party.utils.schedulers.Io
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import javax.inject.Singleton

@Module
abstract class RepositoryModule {
    @Module
    companion object {
        @JvmStatic @Provides
        fun provideTokenRepository(context: Context): TokenRepository {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return PreferencesTokenRepository(sharedPreferences)
        }

        @JvmStatic @Singleton @Provides
        fun provideServersRepository(
                @Io scheduler: Scheduler,
                dao: ServersDao,
                service: ServersService,
                networkChecker: NetworkChecker,
                tokenRepository: TokenRepository
        ): ServersRepository {
            return ServersRepositoryImpl(scheduler, dao, service, networkChecker, tokenRepository)
        }
    }
}
