package com.edvinas.balkaitis.party.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [ServerEntity::class], exportSchema = false)
abstract class ServersDatabase : RoomDatabase() {
    abstract fun serversDao(): ServersDao
}
