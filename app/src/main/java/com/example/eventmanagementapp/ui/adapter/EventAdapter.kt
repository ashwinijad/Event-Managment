package com.example.eventmanagementapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanagementapp.R
import com.example.eventmanagementapp.data.local.db.EventEntity
import com.example.eventmanagementapp.databinding.ItemEventListBinding
import com.example.eventmanagementapp.ui.edit.EditEventActivity
import com.example.eventmanagementapp.utils.Constants
import java.util.Locale


class EventAdapter(private var mEvents: MutableList<EventEntity>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var filteredNameList: MutableList<EventEntity>? = null

    init {
        // Initializes the filtered list with the full list of events.
        filteredNameList = mEvents
    }

    /**
     * Binds the data from the event entity to the view holder.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventEntity = filteredNameList?.get(position)
        eventEntity?.let { holder.bind(it) }
    }

    /**
     * Returns the number of items in the filtered list.
     */
    override fun getItemCount(): Int = filteredNameList?.size ?: 0

    /**
     * Creates a new view holder for the item view.
     */
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val binding = ItemEventListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    inner class ViewHolder(private var binding: ItemEventListBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds event entity data to the view elements.
         */
        fun bind(eventEntity: EventEntity) {
            binding.apply {
                tvEventName.text = eventEntity.name
                tvDate.text = eventEntity.date
                tvTime.text = eventEntity.time
                tvLocation.text = if (eventEntity.location?.isBlank() == true)
                    tvLocation.context.getString(R.string.no_location_added)
                else
                    eventEntity.location

                root.setOnClickListener {
                    // Starts EditEventActivity when the item is clicked.
                    val intent = Intent(binding.root.context, EditEventActivity::class.java)
                    intent.putExtra(Constants.EVENT_ID_KEY, eventEntity.id)
                    binding.root.context.startActivity(intent)
                }
            }
        }
    }

    /**
     * Returns a filter used for filtering the list of events based on the user's input.
     */
    @Suppress("UNCHECKED_CAST")
    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charSequenceString = constraint.toString()
                if (charSequenceString.isEmpty()) {
                    filteredNameList = mEvents
                } else {
                    val filteredList: MutableList<EventEntity> = ArrayList()
                    for (event in mEvents) {
                        if (event.name?.lowercase()
                                ?.contains(charSequenceString.lowercase(Locale.getDefault())) == true
                        ) {
                            filteredList.add(event)
                        }
                    }
                    filteredNameList = filteredList
                }
                val results = FilterResults()
                results.values = filteredNameList
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredNameList = results.values as? MutableList<EventEntity>
                notifyDataSetChanged() // Notifies the adapter to refresh the view.
            }
        }
    }
}
