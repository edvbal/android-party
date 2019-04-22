package com.edvinas.balkaitis.party

import android.os.Bundle
import androidx.navigation.findNavController
import com.edvinas.balkaitis.party.base.BaseDaggerActivity
import com.edvinas.balkaitis.party.data.repository.token.TokenRepository
import javax.inject.Inject

class MainActivity : BaseDaggerActivity() {
    @Inject lateinit var tokenRepository: TokenRepository

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (savedInstanceState == null) {
//            if (tokenRepository.getToken().isEmpty()) {
//                replaceFragment(LoginFragment.newInstance())
//            } else {
//                replaceFragment(ServersFragment.newInstance())
//            }
//        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()
}
