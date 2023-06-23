package com.manoffocus.mfdistricts.components.mftabpager.mftabeventsectionfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.manoffocus.mfdistricts.databinding.MfSectionItemBinding
import com.manoffocus.mfdistricts.utils.Utils
import java.text.SimpleDateFormat
import java.util.Calendar

class MFTabEventsAdapter(val events: List<MFEvent>): RecyclerView.Adapter<MFTabEventsAdapter.MFTabEventsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MFTabEventsViewHolder {
        val binding = MfSectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MFTabEventsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    override fun onBindViewHolder(holder: MFTabEventsViewHolder, position: Int) {
        val item = events[position]
        holder.bind(item)
    }

    inner class MFTabEventsViewHolder(val binding: MfSectionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: MFEvent){
            val day = Utils.getDay(event.from_datetime)
            val month_name = Utils.getPartialMonthName(event.from_datetime)
            binding.mfSectionDay.externalSetText("$day")
            binding.mfSectionMonth.externalSetText("$month_name")
            binding.mfSectionTitle.externalSetText(event.title)
            binding.mfSectionDateSubtitle.externalSetText(Utils.getFormattedDate(event.from_datetime))
        }
    }

}