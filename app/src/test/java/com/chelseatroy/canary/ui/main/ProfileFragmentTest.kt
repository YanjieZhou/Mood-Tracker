package com.chelseatroy.canary.ui.main

import android.widget.ListView
import org.junit.Assert.*
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragment
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.chelseatroy.canary.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun fragmentLaunches() {
        val scenario = launchFragment<ProfileFragment>()
        scenario.onFragment { fragment ->
            val listview = fragment.view!!.findViewById<ListView>(R.id.pastime_list_view)
            assertEquals("BIRDWATCHING", listview.adapter.getItem(0))
        }
    }
}
