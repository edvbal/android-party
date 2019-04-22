package com.edvinas.balkaitis.party.servers.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.edvinas.balkaitis.party.data.database.ServerEntity

class ServersAdapter(
        private val factory: ServersViewHolderFactory
) : RecyclerView.Adapter<ServersViewHolder>() {

    private val servers = mutableListOf<ServerEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServersViewHolder {
        return factory.create(parent)
    }

    override fun getItemCount() = servers.size

    override fun onBindViewHolder(holder: ServersViewHolder, position: Int) {
        holder.bind(servers[position])
    }

    fun setAll(servers: List<ServerEntity>) {
        this.servers.clear()
        this.servers.addAll(servers)
        notifyDataSetChanged()
    }
}
