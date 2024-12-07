package umi.app.vantech.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umi.app.vantech.data.Plant
import umi.app.vantech.databinding.ItemSlideTanamanBinding

class PlantAdapter(private val plants: List<Plant>) : RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    inner class PlantViewHolder(val binding: ItemSlideTanamanBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val binding = ItemSlideTanamanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plants[position]
        holder.binding.imgTanaman.setImageResource(plant.imgPlant)
    }

    override fun getItemCount(): Int = plants.size
}