package com.edvinas.balkaitis.party.servers.fragment

import com.edvinas.balkaitis.party.servers.list.ServersAdapter
import com.edvinas.balkaitis.party.servers.list.ServersViewHolderFactory
import dagger.Module
import dagger.Provides

@Module
abstract class ServersModule {
    @Module
    companion object {
        @JvmStatic @Provides
        fun provideServersAdapter(): ServersAdapter = ServersAdapter(ServersViewHolderFactory())
    }
}
