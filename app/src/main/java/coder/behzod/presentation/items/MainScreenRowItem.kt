package coder.behzod.presentation.items

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.R
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.broadcastReceiver.NotificationReceiver
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.AlertDialogs

@Composable
fun MainScreenRowItem(
    notesModel: NotesModel,
    themeColor: Color,
    fontColor: Color,
    fontSize: Int,
    isAllItemsChecked:(Boolean)->Unit,
    isItemChecked:(Boolean)->Unit,
    isSelected: Boolean,
    onCheckedChange: (Int) -> Unit,
    onClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val ctx = LocalContext.current
    val activityContext = LocalContext.current as Activity
    val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val selectAllStatus = viewModel.selectAllStatus.value
    val isItemSelected = remember { mutableStateOf(false) }
    val isAllItemsSelected = remember { mutableStateOf(true) }
    val showAlarmContent = remember { mutableStateOf(false) }
    val colorFont = remember {
        mutableStateOf(
            when (notesModel.color) {
                -16777216 -> {
                    Color.White
                }

                -1 -> {
//                Color for title,when title color is White
                    Color.Black
                }

                -65536 -> {
//                Color for note and data,when that's color was red
                    Color.White
                }

                -65281 -> {
//                 Color for note and data,when that's color was pink
                    Color.White
                }

                -256 -> {
//                Color for title,when that's color was yellow
                    Color.Black
                }

                else -> {
                    fontColor
                }
            }
        )
    }

    Card(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (themeColor == Color.Black) Color.White else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(notesModel.color))
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes title */
                            Text(
                                modifier = Modifier
                                    .width(200.dp),
                                text = notesModel.title,
                                color = colorFont.value,
                                maxLines = 1,
                                fontSize = fontSize.plus(7).sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                            if (notesModel.alarmStatus) {
                                IconButton(
                                    modifier = Modifier
                                        .size(35.dp),
                                    onClick = {
                                        showAlarmContent.value = true
                                    }) {
                                    if (showAlarmContent.value) {
                                        AlertDialogs(
                                            dismissButton = {
                                                Button(colors = ButtonColors(
                                                    containerColor = Color.Red,
                                                    contentColor = Color.Red,
                                                    disabledContentColor = Color.Red,
                                                    disabledContainerColor = Color.Red
                                                ),
                                                    onClick = {
                                                        showAlarmContent.value = false
                                                    }) {
                                                    Text(
                                                        text = stringResource(id = R.string.close),
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            confirmButton = {
                                                Button(
                                                    colors = ButtonColors(
                                                        containerColor = green,
                                                        contentColor = green,
                                                        disabledContentColor = green,
                                                        disabledContainerColor = green
                                                    ),
                                                    onClick = {
                                                        Intent(
                                                            activityContext,
                                                            NotificationReceiver::class.java
                                                        ).also { intent ->
                                                            PendingIntent.getBroadcast(
                                                                activityContext,
                                                                notesModel.requestCode,
                                                                intent,
                                                                PendingIntent.FLAG_IMMUTABLE
                                                            ).let { pendingIntent ->
                                                                alarmManager.cancel(pendingIntent)
                                                            }
                                                        }
                                                        viewModel.updateAlarmStatus(
                                                            notesModel.requestCode,
                                                            false
                                                        )
                                                    }) {
                                                    Text(
                                                        text = stringResource(id = R.string.cancel),
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            onDismissRequest = { showAlarmContent.value = false },
                                            title = {
                                                Text(
                                                    text = stringResource(R.string.alarm_info),
                                                    color = fontColor,
                                                    fontSize = fontSize.plus(5).sp
                                                )
                                            },
                                            content = {
                                                Column {
                                                    Text(
                                                        text = "${stringResource(id = R.string.date)} : ${notesModel.alarmDate}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                    Text(
                                                        text = "${stringResource(id = R.string.time)} : ${notesModel.alarmTime}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                    Text(
                                                        text = if(notesModel.isRepeat)
                                                            "${stringResource(id = R.string.repeating)} : ${
                                                                stringResource(R.string.everyday)}" else
                                                            "${stringResource(id = R.string.repeating)} : ${
                                                                stringResource(R.string.no)}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            icon = {}
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "alarm",
                                        tint = fontColor
                                    )
                                }
                            } else if (notesModel.isRepeat) {
                                IconButton(
                                    modifier = Modifier
                                        .size(35.dp),
                                    onClick = {
                                        showAlarmContent.value = true
                                    }) {
                                    if (showAlarmContent.value) {
                                        AlertDialogs(
                                            dismissButton = {
                                                Button(colors = ButtonColors(
                                                    containerColor = Color.Red,
                                                    contentColor = Color.Red,
                                                    disabledContentColor = Color.Red,
                                                    disabledContainerColor = Color.Red
                                                ),
                                                    onClick = {
                                                        showAlarmContent.value = false
                                                    }) {
                                                    Text(
                                                        text = stringResource(id = R.string.close),
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            confirmButton = {
                                                Button(
                                                    colors = ButtonColors(
                                                        containerColor = green,
                                                        contentColor = green,
                                                        disabledContentColor = green,
                                                        disabledContainerColor = green
                                                    ),
                                                    onClick = {
                                                        Intent(
                                                            activityContext,
                                                            NotificationReceiver::class.java
                                                        ).also { intent ->
                                                            PendingIntent.getBroadcast(
                                                                activityContext,
                                                                notesModel.requestCode,
                                                                intent,
                                                                PendingIntent.FLAG_IMMUTABLE
                                                            ).let { pendingIntent ->
                                                                alarmManager.cancel(pendingIntent)
                                                            }
                                                        }
                                                        viewModel.updateAlarmStatus(
                                                            notesModel.requestCode,
                                                            false
                                                        )
                                                        viewModel.updateIsRepeat(
                                                            notesModel.requestCode,
                                                            false
                                                        )
                                                    }) {
                                                    Text(
                                                        text = stringResource(id = R.string.cancel),
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            onDismissRequest = { showAlarmContent.value = false },
                                            title = {
                                                Text(
                                                    text = stringResource(R.string.alarm_info),
                                                    color = fontColor,
                                                    fontSize = fontSize.plus(5).sp
                                                )
                                            },
                                            content = {
                                                Column {
                                                    Text(
                                                        text = "${stringResource(id = R.string.date)} : ${notesModel.alarmDate}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                    Text(
                                                        text = "${stringResource(id = R.string.time)} : ${notesModel.alarmTime}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                    Text(
                                                        text = if(notesModel.isRepeat)
                                                            "${stringResource(id = R.string.repeating)} : ${
                                                                stringResource(R.string.everyday)}" else
                                                                    "${stringResource(id = R.string.repeating)} : ${
                                                                        stringResource(R.string.no)}",
                                                        color = fontColor,
                                                        fontSize = fontSize.sp
                                                    )
                                                }
                                            },
                                            icon = {}
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "alarm",
                                        tint = fontColor
                                    )
                                }
                            }
                            /* This is notes data added */
                            Text(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                text = notesModel.dataAdded,
                                color = colorFont.value,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes text */
                            Text(
                                modifier = Modifier
                                    .padding(end = 3.dp, start = 10.dp, bottom = 10.dp),
                                text = notesModel.content,
                                color = colorFont.value,
                                fontSize = fontSize.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    }
                }
            }
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(notesModel.color)),
                contentAlignment = Alignment.BottomEnd
            ) {
                Checkbox(
                    modifier = Modifier
                        .padding(end = 5.dp, bottom = 10.dp),
                    checked = if (selectAllStatus) true
                    else isItemSelected.value,
                    onCheckedChange = {
                        if (selectAllStatus) {
                            isAllItemsSelected.value = it
                        } else {
                            isItemSelected.value = it
                        }
                        if (selectAllStatus){
                            isAllItemsChecked(isAllItemsSelected.value)
                        }else{
                            isItemChecked(isItemSelected.value)
                        }
                        onCheckedChange(if (it) 1 else 0)
                    }
                )
            }
        } else {
            isItemSelected.value = false
            isAllItemsSelected.value = false
        }
    }
}
