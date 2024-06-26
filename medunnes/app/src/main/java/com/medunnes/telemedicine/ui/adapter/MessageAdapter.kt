package com.medunnes.telemedicine.ui.adapter

import android.text.format.DateUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.medunnes.telemedicine.R
import com.medunnes.telemedicine.data.model.Message
import com.medunnes.telemedicine.databinding.ItemMessageBinding

class MessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentUserEmail: String?
) : FirebaseRecyclerAdapter<Message, MessageAdapter.MessageViewHolder>(options) {
    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.text
            setDialogPosition(message.email.toString())
            if (message.timestamp != null) {
                val time = DateUtils.getRelativeTimeSpanString(message.timestamp)
                binding.tvTime.text = time.toString()
            }
        }

        private fun setDialogPosition(email: String) {
            if (currentUserEmail == email) {
                binding.root.gravity = Gravity.START
                binding.cvMessage1Dialog.setCardBackgroundColor(binding.root.resources.getColor(R.color.app_color))
            } else {
                binding.root.gravity = Gravity.END
                binding.cvMessage1Dialog.setCardBackgroundColor(binding.root.resources.getColor(R.color.secondary_color))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)

        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: Message) {
        holder.bind(model)
    }
}