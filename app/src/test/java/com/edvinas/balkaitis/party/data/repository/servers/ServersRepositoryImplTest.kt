package com.edvinas.balkaitis.party.data.repository.servers

import com.edvinas.balkaitis.party.data.api.servers.Server
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.data.database.ServersDao
import com.edvinas.balkaitis.party.data.database.ServersDatabase
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import com.edvinas.balkaitis.party.utils.network.NetworkChecker
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verifyZeroInteractions
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ServersRepositoryImplTest {
    @Mock private lateinit var serversDao: ServersDao
    @Mock private lateinit var database: ServersDatabase
    @Mock private lateinit var serversService: ServersService
    @Mock private lateinit var networkChecker: NetworkChecker
    @Mock private lateinit var tokenRepository: TokenRepository

    private lateinit var repository: ServersRepositoryImpl

    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        repository = ServersRepositoryImpl(
            testScheduler,
            serversDao,
            serversService,
            networkChecker,
            tokenRepository,
            database
        )
    }

    @Test
    fun deleteServers_clearsAllTables() {
        repository.deleteServers()
            .test()
            .assertNoErrors()
        testScheduler.triggerActions()

        verify(database).clearAllTables()
    }

    @Test
    fun getServers_whenHasNoNetwork_getsServersFromDao() {
        val expectedServers = listOf(mock(ServerEntity::class.java))
        given(networkChecker.hasNetwork()).willReturn(false)
        given(serversDao.getAll()).willReturn(Single.just(expectedServers))

        val observer = repository.getServers().test()
        testScheduler.triggerActions()

        observer.assertValue(expectedServers)
        observer.assertComplete()
        observer.assertNoErrors()
    }

    @Test
    fun updateServersDbFromApi_whenGetServersFails_throwsAndDoesNotSaveEntitiesToDatabase() {
        val throwable = Throwable()
        given(tokenRepository.getToken()).willReturn(TOKEN)
        given(serversService.getServers("Bearer $TOKEN")).willReturn(Single.error(throwable))

        val observer = repository.updateServersDbFromApi().test()
        testScheduler.triggerActions()

        observer.assertError(throwable)
        verifyZeroInteractions(serversDao)
    }

    @Test
    fun updateServersDbFromApi_whenGetServersSucceeds_savesServersToDatabase() {
        val expectedServers = listOf(ServerEntity(COUNTRY, DISTANCE.toInt()))
        val serversFromApi = listOf(Server(COUNTRY, DISTANCE))
        given(tokenRepository.getToken()).willReturn(TOKEN)
        given(serversService.getServers("Bearer $TOKEN")).willReturn(Single.just(serversFromApi))

        val observer = repository.updateServersDbFromApi().test()
        testScheduler.triggerActions()

        observer.assertNoErrors()
            .assertComplete()
        verify(serversDao).saveAll(expectedServers)
    }

    @Test
    fun getServers_whenHasNetwork_getsServersFromService() {
        val expectedServers = listOf(ServerEntity(COUNTRY, DISTANCE.toInt()))
        val serversFromApi = listOf(Server(COUNTRY, DISTANCE))
        given(networkChecker.hasNetwork()).willReturn(true)
        given(tokenRepository.getToken()).willReturn(TOKEN)
        given(serversService.getServers("Bearer $TOKEN")).willReturn(Single.just(serversFromApi))

        val observer = repository.getServers().test()
        testScheduler.triggerActions()

        observer.assertValue(expectedServers)
        observer.assertComplete()
        observer.assertNoErrors()
    }

    companion object {
        private const val DISTANCE = "0"
        private const val TOKEN = "token"
        private const val COUNTRY = "Lithunia"
    }
}
