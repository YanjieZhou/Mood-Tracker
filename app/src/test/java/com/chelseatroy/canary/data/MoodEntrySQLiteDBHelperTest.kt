package com.chelseatroy.canary.data

import org.junit.Assert.assertEquals
import org.junit.Test

class MoodEntrySQLiteDBHelperTest {
    @Test
    fun creationQuery_assemblesMoodEntryTable() {
        val expectedQuery = "CREATE TABLE mood_entry (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "mood TEXT, " +
                "logged_at INTEGER, " +
                "notes TEXT, " +
                "pastimes TEXT);"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.createMoodEntriesTableQuery)
    }

    @Test
    fun deletionQuery_removesMoodEntryTable() {
        val expectedQuery = "DROP TABLE IF EXISTS mood_entry"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.dropMoodEntriesTableQuery)
    }

    @Test
    fun creationQuery_assemblesPastimesTable() {
        val expectedQuery = "create table pastimes(" +
                "pastime_name TEXT PRIMARY KEY);"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.createPastimesTableQuery)
    }

    @Test
    fun deletionQuery_removesPastimesTable() {
        val expectedQuery = "drop table if exists pastimes"
        assertEquals(expectedQuery, MoodEntrySQLiteDBHelper.dropPastimesTableQuery)
    }

}