package com.edvinas.balkaitis.party.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.edvinas.balkaitis.party.data.api.servers.Server

@Entity(tableName = "servers")
data class ServerEntity(@PrimaryKey val country: String, val distance: Int) {
    companion object {
        fun from(server: Server) = ServerEntity(server.country, server.distance.toInt())
    }
}
