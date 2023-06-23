package com.manoffocus.mfdistricts.components.mftabpager.mftabnewsectionfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manoffocus.mfdistricts.databinding.MfSectionItemBinding
import com.manoffocus.mfdistricts.utils.Utils
import java.text.SimpleDateFormat
import java.util.Calendar

class MFTabNewsAdapter(val news: List<MFNew>): RecyclerView.Adapter<MFTabNewsAdapter.MFTabNewsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MFTabNewsViewHolder {
        val binding = MfSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MFTabNewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    override fun onBindViewHolder(holder: MFTabNewsViewHolder, position: Int) {
        val item = news[position]
        holder.bind(item)
    }

    inner class MFTabNewsViewHolder(val binding: MfSectionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(new: MFNew){
            val day = Utils.getDay(new.from_datetime)
            val month_name = Utils.getPartialMonthName(new.from_datetime)
            binding.mfSectionDay.externalSetText("$day")
            binding.mfSectionMonth.externalSetText("$month_name")
            binding.mfSectionTitle.externalSetText(new.title)
            binding.mfSectionDateSubtitle.externalSetText(Utils.getFormattedDate(new.from_datetime))
        }
    }

}