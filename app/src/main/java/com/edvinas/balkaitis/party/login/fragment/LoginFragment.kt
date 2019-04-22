package com.edvinas.balkaitis.party.login.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.edvinas.balkaitis.party.R
import com.edvinas.balkaitis.party.base.BaseDaggerFragment
import com.edvinas.balkaitis.party.login.LoginViewModel
import com.edvinas.balkaitis.party.login.mvp.LoginContract
import com.edvinas.balkaitis.party.servers.fragment.ServersFragment
import com.edvinas.balkaitis.party.utils.extensions.hideKeyboard
import com.edvinas.balkaitis.party.utils.extensions.replaceFragment
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment : BaseDaggerFragment(), LoginContract.View {
//    @Inject
//    lateinit var presenter: LoginContract.Presenter

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(LoginViewModel::class)
//        presenter.takeView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            //            presenter.onLoginClicked(
//                    usernameInput.text.toString(),
//                    passwordInput.text.toString()
//            )
        }
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun getLayoutId() = R.layout.fragment_login

    override fun showLoadingView() {
        val loadingConstraintSet = ConstraintSet().apply {
            clone(requireContext(), R.layout.fragment_login_loading)
        }
        loadingConstraintSet.applyTo(root_layout)

        val animation = R.anim.rotate_animation
        loadingImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), animation))
    }

    override fun showFetchingMessage() {
        fetchingListLabel.visibility = View.VISIBLE
    }

    override fun closeKeyboard() {
        this.hideKeyboard()
    }

    override fun hideLoadingView() {
        loadingImage.clearAnimation()

        val initialLoginConstraintSet = ConstraintSet().apply {
            clone(requireContext(), R.layout.fragment_login)
        }
        initialLoginConstraintSet.applyTo(root_layout)
    }

    override fun showServers() {
        val generalErrorMessage = getString(R.string.general_error_something_wrong)
        activity?.replaceFragment(ServersFragment.newInstance())
                ?: Toast.makeText(requireContext(), generalErrorMessage, Toast.LENGTH_LONG).show()
    }

//    override fun onDestroy() {
//        presenter.dropView()
//        super.onDestroy()
//    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
