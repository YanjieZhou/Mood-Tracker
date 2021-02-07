package com.chelseatroy.canary.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class MoodEntrySQLiteDBHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    val context = context

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(createMoodEntriesTableQuery)
        sqLiteDatabase.execSQL(createPastimesTableQuery)

        val pastimeValues = ContentValues()
        val pastimes = arrayOf("EXERCISE", "MEDITATION", "SOCIALIZING", "HYDRATING",
            "EATING", "BIRDWATCHING", "READING", "TELEVISION", "SLEEP", "STRETCHING",
            "CARING_FOR_OTHERS", "JOURNALING")
        for (pastime in pastimes) {
            pastimeValues.put(PASTIMES_COLUMN, pastime)
            sqLiteDatabase.insert(PASTIMES_TABLE_NAME, null, pastimeValues)
        }

        val cursor: Cursor = sqLiteDatabase.query(
            MoodEntrySQLiteDBHelper.PASTIMES_TABLE_NAME,
            arrayOf(PASTIMES_COLUMN),
            null,
            null,
            null,
            null,
            PASTIMES_COLUMN
        )
        Log.i("Database created", "Number of pastimes returned: " + cursor.count)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(cloneMoodEntries)
        sqLiteDatabase.execSQL(dropMoodEntriesTableQuery)
        sqLiteDatabase.execSQL(dropPastimesTableQuery)
        onCreate(sqLiteDatabase)
        sqLiteDatabase.execSQL(restoreMoodEntries)
        sqLiteDatabase.execSQL(dropTempTable)
    }

    fun save(moodEntry: MoodEntry) {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).writableDatabase
        val values = ContentValues()

        values.put(MOOD_ENTRY_COLUMN_MOOD, moodEntry.mood.toString())
        values.put(MOOD_ENTRY_COLUMN_LOGGED_AT, moodEntry.loggedAt)
        values.put(MOOD_ENTRY_COLUMN_NOTES, moodEntry.notes)
        values.put(MOOD_ENTRY_COLUMN_PASTIMES, MoodEntry.formatForDatabase(moodEntry.recentPastimes))

        val newRowId = database.insert(MOOD_ENTRY_TABLE_NAME, null, values)

        if (newRowId == (-1).toLong()) {
            Log.wtf("SQLITE INSERTION FAILED", "We don't know why")
        } else {
            Log.i("MOOD ENTRY SAVED!", "Saved in row ${newRowId}: ${moodEntry.toString()}")
        }
    }

    fun save(pastime: String) {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).writableDatabase
        val values = ContentValues()

        values.put(PASTIMES_COLUMN, pastime)
        val newRowId = database.insert(PASTIMES_TABLE_NAME, null, values)

        if (newRowId == (-1).toLong()) {
            Log.wtf("SQLITE INSERTION FAILED", "We don't know why")
        } else {
            Log.i("PASTIME SAVED!", "Saved in row ${newRowId}: ${pastime}")
        }
    }

    fun listMoodEntries(limitToPastWeek: Boolean = false): Cursor {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).readableDatabase

        var filterOnThis: String? = null
        var usingTheseValues: Array<String>? = null
        if (limitToPastWeek) {
            val nowInMilliseconds = Calendar.getInstance().timeInMillis.toInt()
            filterOnThis = LOGGED_WITHIN
            usingTheseValues = arrayOf("${nowInMilliseconds - ONE_WEEK_AGO_IN_MILLISECONDS}")
        }

        val cursor: Cursor = database.query(
            MOOD_ENTRY_TABLE_NAME,
            allMoodColumns,
            filterOnThis,
            usingTheseValues,
            null,
            null,
            MOOD_ENTRY_COLUMN_LOGGED_AT + " DESC"
        )
        Log.i("DATA FETCHED!", "Number of mood entries returned: " + cursor.count)
        return cursor
    }

    fun listPastimes(): Cursor {
        val database: SQLiteDatabase = MoodEntrySQLiteDBHelper(context).readableDatabase

        val cursor: Cursor = database.query(
            MoodEntrySQLiteDBHelper.PASTIMES_TABLE_NAME,
            arrayOf(PASTIMES_COLUMN),
            null,
            null,
            null,
            null,
            PASTIMES_COLUMN
        )
        Log.i("DATA FETCHED!", "Number of pastimes returned: " + cursor.count)
        return cursor
    }

    fun fetchMoodData(limitToPastWeek: Boolean = false): ArrayList<MoodEntry> {
        var moodEntries = ArrayList<MoodEntry>()
        val cursor = listMoodEntries(limitToPastWeek = limitToPastWeek)

        val fromMoodColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_MOOD)
        val fromNotesColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_NOTES)
        val fromLoggedAtColumn =
            cursor.getColumnIndex(MOOD_ENTRY_COLUMN_LOGGED_AT)
        val fromPastimesColumn =
            cursor.getColumnIndex(MoodEntrySQLiteDBHelper.MOOD_ENTRY_COLUMN_PASTIMES)

        if (cursor.getCount() == 0) {
            Log.i("NO MOOD ENTRIES", "Fetched data and found none.")
        } else {
            Log.i("MOOD ENTRIES FETCHED!", "Fetched data and found mood entries.")
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
        return moodEntries
    }

    fun create() {
        onCreate(writableDatabase)
    }

    fun clear() {
        writableDatabase.execSQL("DROP TABLE IF EXISTS $MOOD_ENTRY_TABLE_NAME")
    }

    fun deletePastime(pastime: String) {
        writableDatabase.delete(PASTIMES_TABLE_NAME, "$PASTIMES_COLUMN=?", arrayOf(pastime))
    }

    companion object {
        private const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "canary_database"
        const val MOOD_ENTRY_TABLE_NAME = "mood_entry"
        const val MOOD_ENTRY_COLUMN_ID = "_id"
        const val MOOD_ENTRY_COLUMN_MOOD = "mood"
        const val MOOD_ENTRY_COLUMN_LOGGED_AT = "logged_at"
        const val MOOD_ENTRY_COLUMN_NOTES = "notes"
        const val MOOD_ENTRY_COLUMN_PASTIMES = "pastimes"
        const val PASTIMES_COLUMN = "pastime_name"
        const val PASTIMES_TABLE_NAME = "pastimes"

        val allMoodColumns = arrayOf<String>(
            MOOD_ENTRY_COLUMN_ID,
            MOOD_ENTRY_COLUMN_MOOD,
            MOOD_ENTRY_COLUMN_LOGGED_AT,
            MOOD_ENTRY_COLUMN_PASTIMES,
            MOOD_ENTRY_COLUMN_NOTES
        )

        val createMoodEntriesTableQuery = "CREATE TABLE $MOOD_ENTRY_TABLE_NAME " +
                "(${MOOD_ENTRY_COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${MOOD_ENTRY_COLUMN_MOOD} TEXT, " +
                "${MOOD_ENTRY_COLUMN_LOGGED_AT} INTEGER, " +
                "${MOOD_ENTRY_COLUMN_NOTES} TEXT, " +
                "${MOOD_ENTRY_COLUMN_PASTIMES} TEXT);"

        val dropMoodEntriesTableQuery = "DROP TABLE IF EXISTS $MOOD_ENTRY_TABLE_NAME"

        val createPastimesTableQuery = "create table $PASTIMES_TABLE_NAME" +
                "(${PASTIMES_COLUMN} TEXT PRIMARY KEY);"

        val dropPastimesTableQuery = "drop table if exists $PASTIMES_TABLE_NAME"

        val cloneMoodEntries = "create table temp_table as select * from $MOOD_ENTRY_TABLE_NAME;"

        val restoreMoodEntries = "insert into $MOOD_ENTRY_TABLE_NAME select * from temp_table;"

        val dropTempTable = "drop table if exists temp_table;"

        const val ONE_WEEK_AGO_IN_MILLISECONDS = 604800000
        const val LOGGED_WITHIN = "${MOOD_ENTRY_COLUMN_LOGGED_AT} >= ?"

    }
}
