package com.edvinas.balkaitis.party.login

import io.reactivex.Single

interface Authenticator {
    fun authenticate(username: String, password: String): Single<String>
}
