package com.chelseatroy.canary.data

import com.github.mikephil.charting.data.PieEntry

class MoodEntryPieAnalysis() {
    fun getActivitiesFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        val map = hashMapOf<String, Int>()
        moodEntries.forEach {moodEntry ->
        moodEntry.recentPastimes.forEach {pastime ->
            if (!map.containsKey(pastime)) map[pastime] = 0
            map[pastime] = map[pastime]!! + 1
        }}
        map.forEach {e->
        pieSections.add(PieEntry(e.value.toFloat(), e.key))
        }
        return pieSections
    }

    fun getMoodsFrom(moodEntries: List<MoodEntry>): ArrayList<PieEntry> {
        val pieSections = ArrayList<PieEntry>()
        val map = hashMapOf<String, Int>()
        moodEntries.forEach {moodEntry ->
            if (!map.containsKey(moodEntry.mood.name)) map[moodEntry.mood.name] = 0
            map[moodEntry.mood.name] = map[moodEntry.mood.name]!! + 1
        }
        map.forEach {e->
            pieSections.add(PieEntry(e.value.toFloat(), e.key))
        }
        return pieSections
    }
}