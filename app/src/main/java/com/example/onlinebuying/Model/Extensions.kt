package com.example.onlinebuying.Model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.sql.Timestamp
import java.text.DateFormat
import java.time.Instant

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun String.toTimeStamp(dateString: String) : Date {
    return Timestamp.from(Instant.now())
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toTimeStamp(instant: Instant) : Date {
    return Timestamp.from(instant)
}


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toTimeStamp() : Date{
    return Timestamp.from(
        this.atStartOfDay(ZoneId.systemDefault()).toInstant()
    )
}

fun Date.toDateString() : String{
    return DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        .format(this)
}

