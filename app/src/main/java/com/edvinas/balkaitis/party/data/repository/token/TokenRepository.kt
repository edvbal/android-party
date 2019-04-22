package com.edvinas.balkaitis.party.data.repository.token

interface TokenRepository {
    fun saveToken(token: String)

    fun getToken(): String

    fun removeToken()
}
