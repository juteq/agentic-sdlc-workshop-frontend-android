class AmenityAvailabilityAdapter : ListAdapter<TimeSlot, AmenityAvailabilityAdapter.TimeSlotViewHolder>(TimeSlotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
        private val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
        private val availabilityTextView: TextView = itemView.findViewById(R.id.availabilityTextView)

        fun bind(timeSlot: TimeSlot) {
            startTimeTextView.text = timeSlot.start
            endTimeTextView.text = timeSlot.end
            availabilityTextView.text = if (timeSlot.isAvailable) "Available" else "Unavailable"
        }
    }

    class TimeSlotDiffCallback : DiffUtil.ItemCallback<TimeSlot>() {
        override fun areItemsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem.start == newItem.start && oldItem.end == newItem.end
        }

        override fun areContentsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem == newItem
        }
    }
}
