package com.edvinas.balkaitis.party.data.repository.servers

import com.edvinas.balkaitis.party.data.database.ServerEntity
import io.reactivex.Completable
import io.reactivex.Single

interface ServersRepository {
    fun updateServersDbFromApi(): Completable
    fun getServers(): Single<List<ServerEntity>>
    fun deleteServers()
}
