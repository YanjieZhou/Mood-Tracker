package com.chelseatroy.canary.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chelseatroy.canary.R

class MoodEntryAdapter(context: Context, moodEntries: ArrayList<MoodEntry>) :
    RecyclerView.Adapter<MoodEntryAdapter.MoodEntryViewHolder>() {
    var context: Context
    var moodEntries: ArrayList<MoodEntry>

    init {
        this.context = context
        this.moodEntries = moodEntries
    }

    inner class MoodEntryViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var moodImage: ImageView
        var loggedAtTextView: TextView
        var pastimesTextView: TextView

        init {
            moodImage = itemView.findViewById(R.id.entry_mood)
            loggedAtTextView = itemView.findViewById(R.id.entry_date_time)
            pastimesTextView = itemView.findViewById(R.id.entry_pastimes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodEntryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val moodEntryListItem: View = inflater
            .inflate(R.layout.item_list_mood_entry, parent, false)
        return MoodEntryViewHolder(moodEntryListItem)
    }

    override fun getItemCount(): Int {
        return moodEntries.size
    }

    override fun onBindViewHolder(holder: MoodEntryViewHolder, position: Int) {
        holder.moodImage.setBackgroundResource(selectImageFor(moodEntries[position].mood))
        holder.loggedAtTextView.text = MoodEntry.getFormattedLogTime(moodEntries[position].loggedAt)
        holder.pastimesTextView.text = MoodEntry.formatForView(moodEntries[position].recentPastimes)
    }

    private fun selectImageFor(mood: Mood): Int {
        when (mood) {
            Mood.UPSET -> return R.drawable.upset
            Mood.DOWN -> return R.drawable.down
            Mood.NEUTRAL -> return R.drawable.neutral
            Mood.COPING -> return R.drawable.coping
            Mood.ELATED -> return R.drawable.elated
        }
    }
}
