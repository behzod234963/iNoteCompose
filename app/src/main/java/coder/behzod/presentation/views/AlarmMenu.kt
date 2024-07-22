package coder.behzod.presentation.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.widget.DatePicker
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
import androidx.compose.runtime.mutableLongStateOf
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
import java.time.LocalDate
import java.time.LocalTime
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


    val date = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val selectedDateTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

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
        { _: DatePicker, year, month, day ->
            date.value = "$year$month$day".dateFormatter()
            calendarInstance.set(Calendar.YEAR,year)
            calendarInstance.set(Calendar.MONTH,month)
            calendarInstance.set(Calendar.DAY_OF_MONTH,day)
        }, calendarInstance.get(Calendar.YEAR),calendarInstance.get(Calendar.MONTH),calendarInstance.get(Calendar.DAY_OF_MONTH)
    )


    val timePickerDialog = TimePickerDialog(
        ctx,
        { _, hourOfDay, pickedMinute ->
            time.value = "$hourOfDay:$pickedMinute"
            calendarInstance.set(Calendar.HOUR_OF_DAY,hourOfDay)
            calendarInstance.set(Calendar.MINUTE,pickedMinute)
        }, calendarInstance.get(Calendar.HOUR_OF_DAY),calendarInstance.get(Calendar.MINUTE), true
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
                        val triggerTime = calendarInstance.timeInMillis
                        viewModel.saveTriggerAtMillis(triggerTime)
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