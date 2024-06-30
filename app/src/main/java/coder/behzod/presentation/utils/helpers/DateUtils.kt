package coder.behzod.presentation.utils.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertMillisToLocalDate(millis:Long):LocalDate{
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter:DateTimeFormatter):LocalDate{

        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter),dateTimeFormatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateToString(date:LocalDate):String{

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy",Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(date,dateFormatter)
        return dateFormatter.format(dateInMillis)
    }
}