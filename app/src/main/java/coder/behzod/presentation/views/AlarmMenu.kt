package coder.behzod.presentation.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.R
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.viewModels.NewNoteViewModel
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetAlarmContent(
    themeColor: Color,
    fontColor: Color,
    fontSize: Int,
    viewModel: NewNoteViewModel = hiltViewModel()
) {

    val isDatePicked = remember { mutableStateOf(false) }
    val isTimePicked = remember { mutableStateOf(false) }
    val isRepeating = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val calendarInstance = Calendar.getInstance()
    val hours = calendarInstance.get(Calendar.HOUR_OF_DAY)
    val minutes = calendarInstance.get(Calendar.MINUTE)


    val date = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(LocalDateTime.now()) }

//    val millisToLocalDate = datePickerState.selectedDateMillis?.let {
//        DateUtils().convertMillisToLocalDate(it)
//    }
//    val date = datePickerState.selectedDateMillis?.let {
//        DateUtils().dateToString(millisToLocalDate!!)
//    } ?: LocalDate.now().toString().dateFormatter()

    val time = remember { mutableStateOf("") }
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }

    val datePickerDialog = DatePickerDialog(
        ctx,
        { _:DatePicker,year:Int,month:Int,day:Int ->
            date.value = "$year$month$day".dateFormatter()
            selectedDate.value = LocalDateTime.of(year,month+1,day,selectedTime.value.hour,selectedTime.value.minute)
        },selectedDate.value.year,selectedDate.value.monthValue-1,selectedDate.value.dayOfMonth
    )

    val timePickerDialog = TimePickerDialog(
        ctx,
        { _, hourOfDay: Int, pickedMinute: Int ->
            time.value = "$hourOfDay:$pickedMinute"
            selectedTime.value = LocalTime.of(hourOfDay,pickedMinute)
        }, selectedTime.value.hour,selectedTime.value.minute, true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColor)
    ) {

        /* Date picker */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "date",
                    tint = fontColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(id = R.string.date),
                    color = fontColor,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = date.value,
                    color = fontColor,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
                IconButton(
                    onClick = {
                        datePickerDialog.show()
                        val triggerAtMillis = selectedDate.value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        viewModel.saveTriggerAtMillis(triggerAtMillis)
                        isDatePicked.value = true
                        viewModel.isDatePicked(true)
                    }) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "datePicker visible",
                        tint = fontColor
                    )
                }
            }
        }

        /* Time picker */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "date",
                    tint = fontColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.time),
                    color = fontColor,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = time.value,
                    color = fontColor,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
                IconButton(
                    onClick = {
                        timePickerDialog.show()
                        val triggerAtMillis = selectedDate.value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        viewModel.saveTriggerAtMillis(triggerAtMillis)
                        isTimePicked.value = true
                        viewModel.isTimePicked(true)
                    }) {
                    Icon(
                        modifier = Modifier.size(35.dp),
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "datePicker visible",
                        tint = fontColor
                    )
                }
            }
        }

        /* Repeating */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(30.dp),
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "repeating",
                    tint = fontColor
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.repeating),
                    color = fontColor,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }

            /* Coming soon */
            Switch(
                checked = isRepeating.value,
                onCheckedChange = {
                    isRepeating.value = it
                },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = green,
                    checkedThumbColor = themeColor,
                    uncheckedThumbColor = themeColor,
                    uncheckedTrackColor = fontColor
                ),
            )
        }
    }
}