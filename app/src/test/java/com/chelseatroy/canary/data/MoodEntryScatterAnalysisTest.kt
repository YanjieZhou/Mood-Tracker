package com.chelseatroy.canary.data

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MoodEntryScatterAnalysisTest {
    lateinit var systemUnderTest: MoodEntryScatterAnalysis

    @Before
    fun setUp() {
        systemUnderTest = MoodEntryScatterAnalysis()
    }

    @Test
    fun arragesMoodsVertically_basedOnMoodHappiness() {
        assertEquals(1f, systemUnderTest.getYPositionFor(MoodEntry(Mood.UPSET)))
        assertEquals(2f, systemUnderTest.getYPositionFor(MoodEntry(Mood.DOWN)))
        assertEquals(3f, systemUnderTest.getYPositionFor(MoodEntry(Mood.NEUTRAL)))
        assertEquals(4f, systemUnderTest.getYPositionFor(MoodEntry(Mood.COPING)))
        assertEquals(5f, systemUnderTest.getYPositionFor(MoodEntry(Mood.ELATED)))
    }

    @Test
    fun arrangesMoodsHorizontally_proportionalToWhenTheyWereLogged() {
        val regularlySpacedMoodEntries = arrayListOf<MoodEntry>(
            MoodEntry(Mood.ELATED, 1L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 2L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 3L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 4L, "Note", "EATING"),
            MoodEntry(Mood.ELATED, 5L, "Note", "EATING")
        )

        assertEquals(arrayListOf(0.0f, 0.25f, 0.5f, 0.75f, 1.0f), systemUnderTest.getXPositionsFor(regularlySpacedMoodEntries))
    }

    @Test
    fun comments_whenThereArentManyMoodEntries_encourageMoreMoodLogging() {
        fail("If the person hasn't logged their mood much, " +
                "it wouldn't be very accurate to try to do any analysis." +
                "So choose a minimum number of entries you think you need to make an analysis " +
                "and produce a message encouraging more logging if there are fewer entries than that.")
    }

    @Test
    fun comments_whenMoodIsImproving_mentionThatMoodIsImproving() {
        fail("If the person's mood, in general, is better lately than it was earlier in the week " +
                "produce a message mentioning it. " +
                "You get to decide how you want to measure this.")
    }

    @Test
    fun comments_whenMoodIsDeclining_mentionThatMoodIsDeclining() {
        fail("If the person's mood, in general, is not as good lately as it was earlier in the week " +
                "produce a message mentioning it. " +
                "You get to decide how you want to measure this.")
    }

    @Test
    fun comments_whenThereIsOneOutlierMood_mentionAnyNotesFromThatMood() {
        fail("If the person's mood, in general, is stable, and there is ONE outlier, " +
                "produce a message containing the notes for that mood." +
                "You get to decide how you want to go about this.")
    }


}