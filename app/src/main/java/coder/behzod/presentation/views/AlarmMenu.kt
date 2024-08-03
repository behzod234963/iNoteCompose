package coder.behzod.presentation.views

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.utils.constants.KEY_DAY
import coder.behzod.presentation.utils.constants.KEY_MONTH
import coder.behzod.presentation.utils.constants.KEY_TRIGGER
import coder.behzod.presentation.utils.constants.KEY_YEAR
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.viewModels.NewNoteViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetAlarmContent(
    themeColor: Color,
    fontColor: Color,
    fontSize: Int,
    dataStore: DataStoreInstance,
    viewModel: NewNoteViewModel = hiltViewModel()
) {

    val isRepeating = remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    val calendarInstance = Calendar.getInstance()

    val date = remember { mutableStateOf("") }
    val time = remember { mutableStateOf("") }

    val selectedDate = remember { mutableLongStateOf( System.currentTimeMillis() ) }
    val selectedTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    val triggerTime = remember { mutableLongStateOf(System.currentTimeMillis()) }

    val pickedYear = calendarInstance.get(Calendar.YEAR)
    val pickedMonth = calendarInstance.get(Calendar.MONTH)
    val pickedDayOfMonth = calendarInstance.get(Calendar.DAY_OF_MONTH)
    val pickedHour = calendarInstance.get(Calendar.HOUR_OF_DAY)
    val pickedMinute = calendarInstance.get(Calendar.MINUTE)


    val datePickerDialog = DatePickerDialog(
        ctx,
        { _, year, month, dayOfMonth ->
            date.value = "$year.$month.$dayOfMonth".dateFormatter()
            calendarInstance.set(Calendar.YEAR, year)
            calendarInstance.set(Calendar.MONTH, month)
            calendarInstance.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        },
        pickedYear, pickedMonth, pickedDayOfMonth,
    )

    val timePickerDialog = TimePickerDialog(
        ctx,
        { _: TimePicker, hourOfDay, minute ->
            time.value = "$hourOfDay:$minute"
            calendarInstance.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendarInstance.set(Calendar.MINUTE, minute)
            calendarInstance.set(Calendar.SECOND, 0)
            selectedTime.longValue = calendarInstance.time.time
        }, pickedHour, pickedMinute, true
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
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                elevation = ButtonDefaults.buttonElevation(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeColor
                ),
                onClick = {
                    triggerTime.longValue = selectedTime.longValue

                    SharedPreferenceInstance(ctx).sharedPreferences.edit().putLong(KEY_TRIGGER, triggerTime.longValue).apply()

                    viewModel.isTimePicked(true)
                    viewModel.isDatePicked(true)
                }) {
                Spacer(
                    modifier = Modifier
                        .width(10.dp)
                )
                Text(
                    text = stringResource(id = R.string.set_alarm),
                    fontSize = fontSize.sp,
                    color = fontColor,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
    }
}