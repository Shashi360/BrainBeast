package com.education.brainbeast.ui.education.ui.menujoinvideoconference

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.brainbeast.databinding.ItemScheduledMeetingBinding

class ScheduledMeetingAdapter(private val meetings: List<ScheduledMeeting>) :
    RecyclerView.Adapter<ScheduledMeetingAdapter.MeetingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val binding =
            ItemScheduledMeetingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeetingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = meetings[position]
        holder.bind(meeting)
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    inner class MeetingViewHolder(private val binding: ItemScheduledMeetingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meeting: ScheduledMeeting) {
            binding.apply {
                roomCodeTextView.text = meeting.roomCode
                dateTextView.text = meeting.date
                timeTextView.text = meeting.time

                // Handle item click here if needed
                root.setOnClickListener {
                    // Handle item click
                }
            }
        }
    }
}
