package com.zolfagharipour.market.other

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.zolfagharipour.market.R

class ShowToast(context: Context, msg: String) {

    init {
        val toast = Toast(context)

        val view = LayoutInflater.from(context).inflate(R.layout.toast_layout, null)
        val txtToast: TextView = view.findViewById(R.id.txtToastLayout)

        txtToast.text = msg
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT

        toast.show()
    }

    constructor(context: Context, msg: Int):this(context, context.getString(msg) )

}