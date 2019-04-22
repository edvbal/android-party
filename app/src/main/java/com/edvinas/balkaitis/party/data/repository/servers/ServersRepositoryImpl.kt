package com.edvinas.balkaitis.party.data.repository.servers

import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.data.database.ServersDao
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.utils.network.NetworkChecker
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

class ServersRepositoryImpl(
        private val scheduler: Scheduler,
        private val serversDao: ServersDao,
        private val serversService: ServersService,
        private val networkChecker: NetworkChecker,
        private val tokenRepository: TokenRepository
) : ServersRepository {
    override fun updateServersDbFromApi(): Completable {
        return serversService.getServers("Bearer ${tokenRepository.getToken()}")
                .subscribeOn(scheduler)
                .flatMapCompletable { servers ->
                    val entities = servers.map { server -> ServerEntity(server.country, server.distance.toInt()) }
                    Completable.fromCallable { serversDao.saveAll(entities) }
                }
    }

    override fun getServers(): Single<List<ServerEntity>> {
        if (networkChecker.hasNetwork()) {
            return serversService.getServers("Bearer ${tokenRepository.getToken()}")
                    .map { servers ->
                        servers.map { server ->
                            ServerEntity(server.country, server.distance.toInt())
                        }
                    }
        }
        return serversDao.getAll().subscribeOn(scheduler)
    }
}
