package com.zolfagharipour.market.data.room.entities

import android.text.SpannableString
import com.zolfagharipour.market.other.Utilities
import kotlin.math.roundToInt


data class Product(
    val id: Int,
    val name: String,
    val CurrentPrice: String,
    val regularPrice: String,
    val images: ArrayList<String>
) {


    fun currentPriceFormatted(): String {
        val text = Utilities.separator(CurrentPrice)
        return "$text تومان"
    }

    fun regularPriceFormatted(): SpannableString {
        return if (CurrentPrice == regularPrice)
            SpannableString("")
        else
            Utilities.getSpannedText(Utilities.separator(regularPrice))
    }

    fun discountPercentFormatted(): String {
        val regularPrice = regularPrice.toDouble()
        val currentPrice = CurrentPrice.toDouble()

        val value = (((regularPrice - currentPrice) / regularPrice) * 100).roundToInt()

        return if (value == 0) ""
        else " $value% "
    }
}