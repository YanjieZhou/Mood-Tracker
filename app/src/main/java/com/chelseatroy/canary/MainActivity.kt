package com.chelseatroy.canary

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.chelseatroy.canary.ui.main.HistoryFragment
import com.chelseatroy.canary.ui.main.MoodEntryFragment
import com.chelseatroy.canary.ui.main.SectionsPagerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), Updatable {
    lateinit var sectionsPagerAdapter: SectionsPagerAdapter
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbarTitle = findViewById<TextView>(R.id.title)
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        var name = preferences.getString("Name", "")
        if (!name.equals("", ignoreCase = true)) {
            toolbarTitle.text = getString(R.string.greeting, name)
        }

        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        viewPager = findViewById(R.id.view_pager) as ViewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        val floatingActionButton: FloatingActionButton = findViewById(R.id.add_entry_button)
        floatingActionButton.setOnClickListener { view ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            val existingMoodEntryFragment = supportFragmentManager.findFragmentByTag(getString(R.string.mood_entry_fragment_tag))
            if (existingMoodEntryFragment != null) {
                fragmentTransaction.remove(existingMoodEntryFragment)
            }
            fragmentTransaction.addToBackStack(null)

            val dialogFragment = MoodEntryFragment()
            dialogFragment.dismissListener = this
            dialogFragment.show(fragmentTransaction, getString(R.string.mood_entry_fragment_tag))
        }
    }

    override fun onDismissal() {
        val currentFragment =
            supportFragmentManager.findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
        currentFragment?.onResume()
    }

}
