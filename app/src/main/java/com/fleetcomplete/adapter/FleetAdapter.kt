package com.fleetcomplete.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fleetcomplete.R
import com.fleetcomplete.clicklisteners.RecyclerViewClickListener
import com.fleetcomplete.databinding.FleetLayoutBinding
//import com.fleetcomplete.databinding.FleetLayoutBinding
import com.fleetcomplete.models.Response

/*
recyclerview adapter class.
used data binding for xml.
binding adapter class used for data age calculation.
driver name is checked with null

 */
class FleetAdapter(
    private val fleet: List<Response>,
    private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<FleetAdapter.MoviesViewHolder>(){
    override fun getItemCount() = fleet.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fleet_layout,
                parent,
                false
            )
        )
    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.fleetLayoutBinding.rv =fleet[position]
        val kk=fleet[position].plate
        holder.fleetLayoutBinding.cardHome.setOnClickListener {
            listener.onRecyclerViewItemClick( fleet[position],position) //for navigating to map fragments.
        }
    }
    inner class MoviesViewHolder(
        val fleetLayoutBinding:FleetLayoutBinding  //data binding for viewholder class
    ) : RecyclerView.ViewHolder(fleetLayoutBinding.root)

}