package com.manoffocus.mfdistricts.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Utils {
    companion object {
        const val BASE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"
        @SuppressLint("SimpleDateFormat")
        fun getFormattedDate(): String{
            return SimpleDateFormat(BASE_FORMAT, Locale("es_ES")).format(Calendar.getInstance().time)
        }
        fun getDay(date: String): Int{
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(BASE_FORMAT)
            cal.time = sdf.parse(date)
            return cal.get(Calendar.DAY_OF_MONTH)
        }
        fun getPartialMonthName(date: String): String{
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(BASE_FORMAT)
            val sdfM = SimpleDateFormat("MMM")
            cal.time = sdf.parse(date)
            return sdfM.format(cal.time).uppercase()
        }
        fun getFullMonthName(date: String): String{
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(BASE_FORMAT)
            val sdM = SimpleDateFormat("MMMM")
            cal.time = sdf.parse(date)
            return sdM.format(cal.time)
        }
        fun getHours(date: String): String{
            val cal = Calendar.getInstance()
            val sdf = SimpleDateFormat(BASE_FORMAT)
            val sdfM = SimpleDateFormat("hh:mm aa")
            cal.time = sdf.parse(date)
            return sdfM.format(cal.time).uppercase()
        }
        fun getFormattedDate(date: String): String{
            val cal = Calendar.getInstance()
            val sdfMonth = SimpleDateFormat("MMMM")
            val date = "${getDay(date)} de ${getFullMonthName(date)} a las ${getHours(date)}"
            return date
        }
    }
}