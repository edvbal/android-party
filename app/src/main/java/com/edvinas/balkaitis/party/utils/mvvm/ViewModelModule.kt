package com.edvinas.balkaitis.party.utils.mvvm

import androidx.lifecycle.ViewModel
import com.edvinas.balkaitis.party.data.repository.servers.ServersRepository
import com.edvinas.balkaitis.party.login.Authenticator
import com.edvinas.balkaitis.party.login.LoginViewModel
import com.edvinas.balkaitis.party.utils.schedulers.Main
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import io.reactivex.Scheduler
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideViewModelFactory(
                viewModelsMap: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
        ): ViewModelFactory = ViewModelFactory(viewModelsMap)

        @JvmStatic
        @Provides
        fun provideLoginViewModel(
                authenticator: Authenticator,
                repository: ServersRepository,
                @Main mainScheduler: Scheduler
        ): LoginViewModel {
            return LoginViewModel(mainScheduler, authenticator, repository)
        }
    }
}
