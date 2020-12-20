package com.zolfagharipour.onlinemarket.other

import android.content.Context
import android.widget.Toast
import java.text.DecimalFormat

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Any.moneySeparator(): String? {
    return DecimalFormat(",###.##").format(this)
}
