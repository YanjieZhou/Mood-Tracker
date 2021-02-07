package com.chelseatroy.canary.ui.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.chelseatroy.canary.R
import com.chelseatroy.canary.data.MoodEntrySQLiteDBHelper
import com.chelseatroy.canary.data.PastimeAdapter
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class ProfileFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
//        val textView: TextView = root.findViewById(R.id.section_label)
//        pageViewModel.text.observe(this, Observer<String> {
//            textView.text = it
//        })
        var pastimes = getPastimeData()
        val adapter = this.context?.let { PastimeAdapter(it, pastimes) }
        val listView: ListView? = root.findViewById(R.id.pastime_list_view)
        listView?.adapter = adapter

        val editTextPastime = root.findViewById<EditText>(R.id.editTextPastime)
        val button = root.findViewById<Button>(R.id.addPastimeButton)
        button.setOnClickListener { view ->
            val pastime = editTextPastime.text.toString()

            Log.i("SUBMITTED PASTIME", pastime)

            val databaseHelper = MoodEntrySQLiteDBHelper(activity)
            databaseHelper.save(pastime)

            editTextPastime.text.clear()

            pastimes.add(pastime)
            adapter?.notifyDataSetChanged()
        }
        return root
    }

    fun getPastimeData(): ArrayList<String> {

        val databaseHelper = MoodEntrySQLiteDBHelper(activity)
        val cursor = databaseHelper.listPastimes()
        var pastimes = ArrayList<String>()

        val fromPastimesColumn = cursor.getColumnIndex(MoodEntrySQLiteDBHelper.PASTIMES_COLUMN)

        if(cursor.count == 0) {
            Log.i("NO PASTIMES", "Fetched data and found none.")
        } else {
            Log.i("PASTIMES FETCHED!", "Fetched data and found pastimes.")

            while (cursor.moveToNext()) {
                pastimes.add(cursor.getString(fromPastimesColumn))
            }
        }
        return pastimes
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}

fun getNames(e: Class<out Enum<*>?>): Array<String?>? {
    return Arrays.toString(e.enumConstants).split(", ").toTypedArray()
}