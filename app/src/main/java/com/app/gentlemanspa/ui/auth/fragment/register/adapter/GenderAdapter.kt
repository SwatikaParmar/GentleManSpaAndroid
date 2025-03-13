package com.app.gentlemanspa.ui.auth.fragment.register.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.app.gentlemanspa.R
import com.app.gentlemanspa.ui.auth.fragment.register.model.GenderRequest


class GenderAdapter(context: Context, private val list: ArrayList<GenderRequest>) :
    ArrayAdapter<String>(context, R.layout.item_spinner) {

    override fun getItem(position: Int): String {
        return list[position].name
    }

    override fun getItemId(position: Int): Long {
        try {
            return list[position].id.toLong()
        } catch (_: NumberFormatException) {

        }
        return 0.toLong()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.item_spinner, parent, false)
        layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        val textView = layout.findViewById<TextView>(android.R.id.text1)
        if (position == 0) {
            textView.height = 0
            textView.visibility = View.GONE
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.black))
            textView.text = list[position].name

        }
        return layout
    }
}