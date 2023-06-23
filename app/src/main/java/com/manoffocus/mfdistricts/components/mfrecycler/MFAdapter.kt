package com.manoffocus.mfdistricts.components.mfrecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.manoffocus.mfdistricts.components.interfaces.onItemClickAdapter
import com.manoffocus.mfdistricts.databinding.MfRecyclerItemBinding

class MFAdapter(val pois: List<MFPoiItem>, val onItemClickAdapter: onItemClickAdapter): RecyclerView.Adapter<MFAdapter.MFViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MFViewHolder {
        val binding = MfRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MFViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    override fun onBindViewHolder(holder: MFViewHolder, position: Int) {
        val item = pois[position]
        holder.bind(item)
    }

    inner class MFViewHolder(val binding: MfRecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(poi: MFPoiItem){
            binding.mfRecyclerImg.setImageUrl(poi.imageUrl)
            binding.mfRecyclerTitle.externalSetText(poi.name)
            binding.mfRecyclerLikesCount.externalSetChipValueText("${poi.likes}")
            binding.mfRecyclerLikesCount.externalSetChipIconByInt(poi.icon)
            binding.mfRecyclerLikesCount.externalSetValueTextColor(Color.BLACK)
            binding.mfRecyclerLikesCount.setOnClickListener {
                onItemClickAdapter.onClickItem(poi.id)
            }
            binding.mfRecyclerTitle.setOnClickListener {
                onItemClickAdapter.onClickItem(poi.id)
            }
            binding.mfRecyclerImg.setOnClickListener {
                onItemClickAdapter.onClickItem(poi.id)
            }
            binding.root.setOnClickListener {
                onItemClickAdapter.onClickItem(poi.id)
            }
        }
    }

}