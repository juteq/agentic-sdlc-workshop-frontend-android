package com.livly.booking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.livly.booking.databinding.ItemTimeSlotBinding
import com.livly.booking.data.remote.model.TimeSlot

class AmenityAvailabilityAdapter : ListAdapter<TimeSlot, AmenityAvailabilityAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTimeSlotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val slot = getItem(position)
        holder.bind(slot)
    }

    class ViewHolder(private val binding: ItemTimeSlotBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(slot: TimeSlot) {
            binding.startTimeTextView.text = slot.start
            binding.endTimeTextView.text = slot.end
            binding.availabilityTextView.text = if (slot.isAvailable) "Available" else "Not Available"
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<TimeSlot>() {
        override fun areItemsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem.start == newItem.start && oldItem.end == newItem.end
        }

        override fun areContentsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem == newItem
        }
    }
}
