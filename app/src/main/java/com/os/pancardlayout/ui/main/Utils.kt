package com.os.pancardlayout.ui.main

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class Utils {
     fun String.dateFormator():String{
        val formatter: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date  = formatter.parse(this)
        val newFormat = SimpleDateFormat("dd-MM-yyyy",Locale.getDefault())
        return newFormat.format(date)
    }
}