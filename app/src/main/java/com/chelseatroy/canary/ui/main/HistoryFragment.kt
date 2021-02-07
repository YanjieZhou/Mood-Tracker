package com.chelseatroy.canary.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.MoodEntry
import com.chelseatroy.canary.data.MoodEntryAdapter
import com.chelseatroy.canary.data.MoodEntrySQLiteDBHelper


class HistoryFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: MoodEntryAdapter

    lateinit var databaseHelper: MoodEntrySQLiteDBHelper
    lateinit var moodEntries: ArrayList<MoodEntry>

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_history, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()

        // Because of the !! non-null assertion, the app is going to crash if it can't find this id.
        // I am OK with this because if that happens, the developer will catch that when they run
        // the app to look at the list (so the likelihood that this would go uncaught is very low).
        recyclerView = view?.findViewById(R.id.mood_entry_list)!!

        databaseHelper = MoodEntrySQLiteDBHelper(activity)
        moodEntries = ArrayList<MoodEntry>()

        fetchMoodData()

        recyclerViewAdapter = MoodEntryAdapter(activity?.applicationContext!!, moodEntries)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext!!)

        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)!!
        swipeRefreshLayout.setOnRefreshListener {
            fetchMoodData()
            recyclerViewAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }

    }

    fun fetchMoodData() {
        val cursor = databaseHelper.listMoodEntries()

        val fromMoodColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_MOOD)
        val fromNotesColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_NOTES)
        val fromLoggedAtColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_LOGGED_AT)
        val fromPastimesColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_PASTIMES)

        if(cursor.getCount() == 0) {
            Log.i("NO MOOD ENTRIES", "Fetched data and found none.")
        } else {
            Log.i("MOOD ENTRIES FETCHED!", "Fetched data and found mood entries.")
            moodEntries.clear()

            while (cursor.moveToNext()) {
                val nextMood = MoodEntry(
                    Mood.valueOf(cursor.getString(fromMoodColumn)),
                    cursor.getLong(fromLoggedAtColumn),
                    cursor.getString(fromNotesColumn),
                    cursor.getString(fromPastimesColumn)
                )
                moodEntries.add(nextMood)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(sectionNumber: Int): HistoryFragment {
            return HistoryFragment()
        }
    }
}