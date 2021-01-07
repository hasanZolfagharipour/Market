package com.zolfagharipour.market.other

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import java.text.DecimalFormat
const val TAG = "tag"
object Utilities {

    fun getSpannedText(input: String): SpannableString{
        val spannable = SpannableString(input)
        spannable.setSpan(StrikethroughSpan(), 0, input.length, Spanned.SPAN_MARK_MARK)
        return spannable
    }

    fun separator(price: String): String {
        return DecimalFormat("0,000").format(price.toInt())
    }
}