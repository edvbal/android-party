package com.edvinas.balkaitis.party.login

import com.edvinas.balkaitis.party.data.api.login.LoginBody
import com.edvinas.balkaitis.party.data.api.login.LoginService
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import io.reactivex.Single

class AuthenticatorImpl(
        private val loginService: LoginService,
        private val tokenRepository: TokenRepository
) : Authenticator {
    override fun authenticate(username: String, password: String): Single<String> {
        return loginService.login(LoginBody(username, password))
                .map { response ->
                    tokenRepository.saveToken(response.token)
                    response.token
                }
    }
}
