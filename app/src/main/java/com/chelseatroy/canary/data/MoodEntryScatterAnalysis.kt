package com.chelseatroy.canary.data

class MoodEntryScatterAnalysis() {
    fun getYPositionFor(moodEntry: MoodEntry): Float {
        var height = 0f
        when (moodEntry.mood) {
            Mood.UPSET -> height = 1f
            Mood.DOWN -> height = 2f
            Mood.NEUTRAL -> height = 3f
            Mood.COPING -> height = 4f
            Mood.ELATED -> height = 5f
        }
        return height
    }

    fun getXPositionsFor(moodEntries: List<MoodEntry>): List<Float> {
        val latestPoint = moodEntries.last().loggedAt
        val earliestPoint = moodEntries.first().loggedAt
        val diff = latestPoint - earliestPoint

        if (diff == 0L) {
            return arrayListOf(0f) // If we don't do this, a singleton list with 0L in it becomes [NaN] for some reason
        } else {
            return moodEntries.map { (it.loggedAt.toFloat() - earliestPoint) / diff } as ArrayList<Float>
        }
    }

    fun commentOn(moodEntries: List<MoodEntry>): String {
        return "This text box is pretty useless so far. It would be great if this text box contained some useful information " +
                "about the trends in my mood, wouldn't it?"
    }
}