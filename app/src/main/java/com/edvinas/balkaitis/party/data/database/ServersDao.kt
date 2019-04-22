package com.edvinas.balkaitis.party.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single

@Dao
interface ServersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(server: List<ServerEntity>)


    @Query("SELECT * FROM servers")
    fun getAll(): Single<List<ServerEntity>>
}
