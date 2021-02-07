package com.chelseatroy.canary.ui.main

import org.junit.Assert.*
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.Mood
import com.chelseatroy.canary.data.MoodEntry
import com.chelseatroy.canary.data.MoodEntryPieAnalysis
import com.github.mikephil.charting.data.PieEntry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TagsFragmentTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun fragmentLaunches() {
        val scenario = launchFragment<TagsFragment>()
        scenario.onFragment { fragment ->
            val tagsLabel = fragment.view!!.findViewById<TextView>(R.id.tags_label)
            assertEquals("Moods & Activities Statistics", tagsLabel.text)
        }
    }

    @Test
    fun testGetActivities() {
        val pieAnalysis = MoodEntryPieAnalysis()
        val moodEntry = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1, "", "EXERCISE, MEDITATION, SOCIALIZING")
        )
        val res = pieAnalysis.getActivitiesFrom(moodEntry)

        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(1f, "EXERCISE"))
        pieSections.add(PieEntry(1f, "MEDITATION"))
        pieSections.add(PieEntry(1f, "SOCIALIZING"))

        for (i in 0..2) {
            assertEquals(pieSections[i].describeContents(), res[i].describeContents())
        }
    }

    @Test
    fun testGetMoods() {
        val pieAnalysis = MoodEntryPieAnalysis()
        val moodEntry = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1, "", "EXERCISE, MEDITATION, SOCIALIZING")
        )
        val res = pieAnalysis.getMoodsFrom(moodEntry)

        val pieSections = ArrayList<PieEntry>()
        pieSections.add(PieEntry(1f, "ELATED"))


        assertEquals(pieSections[0].describeContents(), res[0].describeContents())
    }
}
