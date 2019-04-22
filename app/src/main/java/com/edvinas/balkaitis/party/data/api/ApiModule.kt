package com.edvinas.balkaitis.party.data.api

import android.content.Context
import com.edvinas.balkaitis.party.BuildConfig
import com.edvinas.balkaitis.party.data.api.login.LoginService
import com.edvinas.balkaitis.party.data.api.servers.ServersService
import com.edvinas.balkaitis.party.utils.network.NetworkChecker
import com.edvinas.balkaitis.party.utils.network.NetworkCheckerImpl
import com.edvinas.balkaitis.party.utils.schedulers.Io
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
abstract class ApiModule {
    @Module
    companion object {
        @JvmStatic
        @Singleton
        @Provides
        fun provideRetrofit(client: OkHttpClient, @Io scheduler: Scheduler): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(scheduler))
                    .build()
        }

        @JvmStatic
        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
        }

        @JvmStatic
        @Provides
        fun provideLoginService(retrofit: Retrofit): LoginService {
            return retrofit.create(LoginService::class.java)
        }

        @JvmStatic
        @Provides
        fun provideServersService(retrofit: Retrofit): ServersService {
            return retrofit.create(ServersService::class.java)
        }

        @JvmStatic
        @Singleton
        @Provides
        fun provideNetworkChecker(context: Context): NetworkChecker = NetworkCheckerImpl(context)
    }
}
