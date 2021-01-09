package com.zolfagharipour.market.data.room.entities

import android.text.Html
import android.text.SpannableString
import com.zolfagharipour.market.other.Utilities
import java.io.Serializable
import kotlin.math.roundToInt


data class ProductModel(
    val id: String,
    val name: String,
    val currentPrice: String,
    val regularPrice: String,
    val images: ArrayList<String>,
    val description: String = "",
    val totalSale: String = "",
    val relatedIds: ArrayList<String> = ArrayList(),
    val categories: ArrayList<CategoryModel> = ArrayList(),
    val rate: String = "",
    val relatedProductModel: ArrayList<ProductModel> = ArrayList()
): Serializable {


    fun currentPriceFormatted(): String {
        val text = Utilities.separator(currentPrice)
        return "$text تومان"
    }

    fun regularPriceFormatted(): SpannableString {
        return if (currentPrice == regularPrice)
            SpannableString("")
        else
            Utilities.getSpannedText(Utilities.separator(regularPrice))
    }

    fun discountPercentFormatted(): String {
        val regularPrice = regularPrice.toDouble()
        val currentPrice = currentPrice.toDouble()

        val value = (((regularPrice - currentPrice) / regularPrice) * 100).roundToInt()

        return if (value == 0) ""
        else " $value% "
    }

    fun descriptionFormatted(): String{
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            Html.fromHtml(description).toString()
        }
    }

    fun rateFormatted(): Int = (rate.toDouble() * 2).roundToInt()

    fun totalSaleFormatted():String = "($totalSale)"

    fun rateFormattedString(): String {
        val rateInt =  rate.toDouble().roundToInt()
        if (rateInt == 0)
            return "0"
        else
            return rate.toDouble().toString()
    }

}