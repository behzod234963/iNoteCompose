package coder.behzod.presentation.utils.extensions

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("SimpleDateFormat")
fun String.dateFormatter():String{

    val simpleDataFormatter = SimpleDateFormat("dd.MM.yyyy")
    val result = simpleDataFormatter.format(Date())
    return result.toString()
}