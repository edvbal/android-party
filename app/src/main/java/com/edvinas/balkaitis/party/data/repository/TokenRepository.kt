package com.edvinas.balkaitis.party.data.repository

interface TokenRepository {
    fun saveToken(token: String)

    fun getToken(): String

    fun removeToken()
}
