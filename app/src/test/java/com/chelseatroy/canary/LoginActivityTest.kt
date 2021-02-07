package com.chelseatroy.canary

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import com.chelseatroy.canary.ui.main.MoodEntryFragment
import org.junit.Assert.*

import com.google.android.material.floatingactionbutton.FloatingActionButton

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class LoginActivityTest {

    @Test
    fun loginTest() {
        val systemUnderTest = Robolectric.buildActivity(LoginActivity::class.java)
            .create()
            .visible()
            .get()
        val loginButton = systemUnderTest.findViewById<Button>(R.id.login_button)

        loginButton.performClick()

        assertTrue(systemUnderTest.isAuth)
    }
}