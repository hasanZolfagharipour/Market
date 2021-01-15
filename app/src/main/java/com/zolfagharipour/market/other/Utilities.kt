package com.zolfagharipour.market.other

import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.DecimalFormat
const val TAG = "tag"
object Utilities {

    fun getSpannedText(input: String): SpannableString{
        val spannable = SpannableString(input)
        spannable.setSpan(StrikethroughSpan(), 0, input.length, Spanned.SPAN_MARK_MARK)
        return spannable
    }

    val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler{ _, exception -> println("$exception") }


}