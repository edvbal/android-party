package com.edvinas.balkaitis.party.login.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import com.edvinas.balkaitis.party.R
import com.edvinas.balkaitis.party.base.BaseDaggerFragment
import com.edvinas.balkaitis.party.login.LoginViewModel
import com.edvinas.balkaitis.party.utils.extensions.hideKeyboard
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseDaggerFragment() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(LoginViewModel::class)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeFetchStartEvent().onResult { showFetchingMessage() }
        viewModel.observeLoadingStartEvent().onResult { showLoadingView() }
        viewModel.observeLoadingStopEvent().onResult { hideLoadingView() }
        viewModel.observeHideKeyboardEvent().onResult { hideKeyboard() }
        viewModel.observeLoginCompleteEvent().onResult { showServers() }
        viewModel.observeErrorEvent().onResult(::showError)
        loginButton.setOnClickListener {
            viewModel.onLoginClicked(usernameInput.text.toString(), passwordInput.text.toString())
        }
    }

    override fun getLayoutId() = R.layout.fragment_login

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showLoadingView() {
        val loadingConstraintSet = ConstraintSet().apply {
            clone(requireContext(), R.layout.fragment_login_loading)
        }
        loadingConstraintSet.applyTo(root_layout)

        val animation = R.anim.rotate_animation
        loadingImage.startAnimation(AnimationUtils.loadAnimation(requireContext(), animation))
    }

    private fun showFetchingMessage() {
        fetchingListLabel.visibility = View.VISIBLE
    }

    private fun hideLoadingView() {
        loadingImage.clearAnimation()

        val initialLoginConstraintSet = ConstraintSet().apply {
            clone(requireContext(), R.layout.fragment_login)
        }
        initialLoginConstraintSet.applyTo(root_layout)
    }

    private fun showServers() {
        val generalErrorMessage = getString(R.string.general_error_something_wrong)
        findNavController().navigate(R.id.actionServersList)

//        activity?.replaceFragment(ServersFragment.newInstance())
//                ?: Toast.makeText(requireContext(), generalErrorMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}
