package com.edvinas.balkaitis.party.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    @Module
    companion object {
        @JvmStatic @Singleton @Provides
        fun provideServersDatabase(context: Context): ServersDatabase {
            return Room.databaseBuilder(context, ServersDatabase::class.java, "servers database")
                    .fallbackToDestructiveMigration()
                    .build()
        }

        @JvmStatic @Singleton @Provides
        fun provideServersDao(database: ServersDatabase): ServersDao = database.serversDao()
    }
}
