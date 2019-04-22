package com.edvinas.balkaitis.party.servers.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.edvinas.balkaitis.party.R
import com.edvinas.balkaitis.party.base.BaseDaggerFragment
import com.edvinas.balkaitis.party.data.database.ServerEntity
import com.edvinas.balkaitis.party.servers.list.ServersAdapter
import kotlinx.android.synthetic.main.fragment_servers.*
import javax.inject.Inject

class ServersFragment : BaseDaggerFragment() {
    @Inject
    lateinit var adapter: ServersAdapter

    private lateinit var viewModel: ServersViewModel

    override fun getLayoutId() = R.layout.fragment_servers

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseRecycler()
        iconLogout.setOnClickListener { viewModel.onLogoutClicked() }
        viewModel = getViewModel(ServersViewModel::class)
        viewModel.observeErrorEvent().onResult(::showError)
        viewModel.observeLoadingState().onResult(::setLoading)
        viewModel.serversReceivedEvent().onResult(::populateServers)
        viewModel.observeShowLoginEvent().onResult { showLogin() }
    }

    private fun initialiseRecycler() {
        serversList.addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        serversList.setHasFixedSize(true)
        serversList.adapter = adapter
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun showLogin() {
        val generalErrorMessage = getString(R.string.general_error_something_wrong)
//        activity?.replaceFragment(LoginFragment.newInstance())
//                ?: Toast.makeText(requireContext(), generalErrorMessage, Toast.LENGTH_LONG).show()
    }

    private fun setLoading(isLoading: Int) {
        progressBar.visibility = isLoading
    }

    private fun populateServers(servers: List<ServerEntity>) {
        adapter.setAll(servers)
    }

    companion object {
        fun newInstance() = ServersFragment()
    }
}
