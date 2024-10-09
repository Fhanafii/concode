import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fhanafi.mybottomnavigation.data.response.ListEventsItem
import com.fhanafi.mybottomnavigation.databinding.ItemEventBinding

class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        // Use View Binding to inflate the layout for each item
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Get the event at the current position
        val event = getItem(position)
        holder.bind(event) // Bind the event data to the ViewHolder
    }

    // ViewHolder class using View Binding
    class EventViewHolder(private val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind the event data to the views
        fun bind(event: ListEventsItem) {
            binding.tvEventTitle.text = event.name

            // Load the image using Glide
            Glide.with(binding.root.context)
                .load(event.mediaCover) // Use the image URL from your data model
                .into(binding.imgEventPhoto) // Make sure you have an ImageView in your layout
        }
    }

    // Companion object for DiffUtil
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                // Compare items based on their content
                return oldItem.name == newItem.name && oldItem.imageLogo == newItem.imageLogo
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                // Compare the full content
                return oldItem == newItem
            }
        }
    }
}
