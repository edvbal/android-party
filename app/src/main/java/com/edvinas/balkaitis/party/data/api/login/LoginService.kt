package com.edvinas.balkaitis.party.data.api.login

import com.edvinas.balkaitis.party.data.api.login.LoginBody
import com.edvinas.balkaitis.party.data.api.login.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("tokens")
    fun login(@Body tokenRequest: LoginBody): Single<LoginResponse>
}
