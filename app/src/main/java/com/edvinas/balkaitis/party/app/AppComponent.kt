package com.edvinas.balkaitis.party.app

import com.edvinas.balkaitis.party.data.api.ApiModule
import com.edvinas.balkaitis.party.data.database.DatabaseModule
import com.edvinas.balkaitis.party.data.repository.RepositoryModule
import com.edvinas.balkaitis.party.utils.mvvm.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppContributorsModule::class,
            RepositoryModule::class,
            ViewModelModule::class,
            DatabaseModule::class,
            ApiModule::class,
            AppModule::class
        ]
)
interface AppComponent : AndroidInjector<PartyApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<PartyApplication>()
}
