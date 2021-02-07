package com.chelseatroy.canary.data

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chelseatroy.canary.R


class PastimeAdapter(context: Context, list: ArrayList<String>) :
    BaseAdapter() {
    private val list: ArrayList<String> = list
    private val context: Context = context

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.pastime_list_item, null)
        }

        val listItemText = view?.findViewById(R.id.pastime_item_text) as TextView
        listItemText.text = list[position]


        val deleteBtn = view.findViewById(R.id.pastime_delete_btn) as ImageButton
        deleteBtn.setOnClickListener {
            showDialog(position)
        }
        return view
    }

     private fun showDialog(position: Int) {
         val alertDialog = AlertDialog.Builder(context).apply {
             setTitle(R.string.alert_dialog_title)
             setMessage(R.string.alert_dialog_msg)
             setPositiveButton(R.string.ok) { dialog, id ->
                 var temp = list[position]
                 list.removeAt(position)
                 notifyDataSetChanged()

                 val dbHelper = MoodEntrySQLiteDBHelper(context)
                 dbHelper.deletePastime(temp)
             }
             setNegativeButton(R.string.cancel, null)
         }.create()

         alertDialog.show()
     }

}