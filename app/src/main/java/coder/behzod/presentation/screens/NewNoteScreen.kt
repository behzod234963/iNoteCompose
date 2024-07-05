package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.items.ColorsItem
import coder.behzod.presentation.navigation.Arguments
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.notifications.NotificationScheduler
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.liteGreen
import coder.behzod.presentation.theme.yellow
import coder.behzod.presentation.utils.constants.KEY_ALARM_CONTENT
import coder.behzod.presentation.utils.constants.KEY_ALARM_STATUS
import coder.behzod.presentation.utils.constants.KEY_ALARM_TITLE
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.viewModels.NewNoteViewModel
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.SetAlarmContent
import coder.behzod.presentation.views.SpeedDialFAB
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewNoteScreen(
    navController: NavController,
    arguments: Arguments,
    sharedPrefs: SharedPreferenceInstance,
    notificationScheduler: NotificationScheduler,
    viewModel: NewNoteViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val ctx = LocalContext.current.applicationContext
    val activityContext = LocalContext.current as Activity

    val note = viewModel.note.value

    val title = viewModel.title.value

    val vmColor = viewModel.color.value

    val date = remember { mutableStateOf(LocalDate.now()) }

    val pickedDate = viewModel.dateAndTime

    val themeIndex =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    if (colorTheme == Color.Black) {
        themeColor.value = Color.Black
    } else {
        themeColor.value = Color.White
    }

    val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
    val fontColor = remember { mutableStateOf(colorFont) }

    if (colorFont == Color.White) {
        fontColor.value = Color.White
    } else {
        fontColor.value = Color.Black
    }
    val scriptColor = remember { mutableStateOf(fontColor.value) }

    val priorityColorIndex = remember { mutableIntStateOf(0) }
    val priorityColor = remember { mutableStateOf(Color.Black) }

    when (priorityColorIndex.intValue) {
        0 -> {
            priorityColor.value = themeColor.value
        }

        1 -> {
            priorityColor.value = Color.White
            themeColor.value = priorityColor.value
        }

        2 -> {
            priorityColor.value = yellow
            themeColor.value = priorityColor.value
        }

        3 -> {
            priorityColor.value = green
            themeColor.value = priorityColor.value
        }

        4 -> {
            priorityColor.value = liteGreen
            themeColor.value = priorityColor.value
        }
    }

    val colorsList = listOf(
        Color.White,
        yellow,
        green,
        liteGreen
    )

    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isSwitched = remember { mutableStateOf(false) }
    val isOptionsExpanded = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .background(if (arguments.id != -1) Color(vmColor) else themeColor.value),
        floatingActionButton = {
            SpeedDialFAB(modifier = Modifier
                .padding(bottom = 20.dp, end = 20.dp),
                labelFirst = stringResource(R.string.save),
                painterFirst = R.drawable.ic_save,
                labelSecond = stringResource(R.string.share_and_save),
                painterSecond = R.drawable.ic_share,
                onClickFirst = {
                    if (note.text == "" && note.text.isBlank()) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = ctx.getString(R.string.note_is_cannot_be_empty),
                            )
                        }
                    } else {
                        /* Save note */
                        if (arguments.id != -1) {
                            viewModel.saveNote(
                                NotesModel(
                                    id = arguments.id,
                                    title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                        .text.capitalize(),
                                    content = note.text,
                                    color = priorityColor.value.toArgb(),
                                    dataAdded = date.value.toString().dateFormatter()
                                )
                            )

                            /* alarm */
                            if (viewModel.date.value == 0L || viewModel.time.value == 0L) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Invalid date or time"
                                    )
                                }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                    ActivityCompat.requestPermissions(
                                        activityContext,
                                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                        0
                                    )
                                }
                                sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_TITLE, title.text).apply()
                                sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_CONTENT, note.text).apply()

                                notificationScheduler.scheduleNotification(
                                    activityContext,
                                    viewModel.dateAndTime.value
                                )
                            }
                        } else {
                            viewModel.saveNote(
                                NotesModel(
                                    title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                        .text.capitalize(),
                                    content = note.text,
                                    color = priorityColor.value.toArgb(),
                                    dataAdded = date.value.toString().dateFormatter()
                                )
                            )
                            if (viewModel.date.value == 0L || viewModel.time.value == 0L) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        "Invalid date or time"
                                    )
                                }
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                    ActivityCompat.requestPermissions(
                                        activityContext,
                                        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                        0
                                    )
                                }
                                sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_TITLE, title.text).apply()
                                sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_CONTENT, note.text).apply()

                                notificationScheduler.scheduleNotification(
                                    activityContext,
                                    viewModel.dateAndTime.value
                                )
                            }
                        }
                        navController.navigate(ScreensRouter.MainScreenRoute.route)
                    }
                }) {
                if (note.text == "" && note.text.isBlank()) {
                    coroutineScope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = ctx.getString(R.string.note_is_cannot_be_empty),
                        )
                    }
                } else {
                    /* Share and Save note */
                    if (arguments.id != -1) {

                        ShareNote().execute(
                            title = title.text,
                            content = note.text,
                            ctx = activityContext
                        ) {
                            navController.navigate(ScreensRouter.MainScreenRoute.route)
                        }

                        viewModel.saveNote(
                            NotesModel(
                                id = arguments.id,
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.also {
                                        it.capitalize()
                                    },
                                content = note.text,

                                color = priorityColor.value.toArgb(),
                                dataAdded = date.value.toString().dateFormatter()
                            )
                        )
                        if (viewModel.date.value == 0L || viewModel.time.value == 0L) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Invalid date or time"
                                )
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                ActivityCompat.requestPermissions(
                                    activityContext,
                                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                    0
                                )
                            }
                            sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_TITLE, title.text).apply()
                            sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_CONTENT, note.text).apply()

                            notificationScheduler.scheduleNotification(
                                activityContext,
                                viewModel.dateAndTime.value
                            )
                        }

                    } else {

                        ShareNote().execute(
                            title = title.text,
                            content = note.text,
                            ctx = activityContext,
                        ) {
                            navController.navigate(ScreensRouter.MainScreenRoute.route)
                        }

                        viewModel.saveNote(
                            NotesModel(
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.capitalize(),
                                content = note.text,
                                color = priorityColor.value.toArgb(),
                                dataAdded = date.value.toString().dateFormatter()
                            )
                        )
                        if (viewModel.date.value == 0L || viewModel.time.value == 0L) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    "Invalid date or time"
                                )
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                                ActivityCompat.requestPermissions(
                                    activityContext,
                                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                                    0
                                )
                            }
                            sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_TITLE, title.text).apply()
                            sharedPrefs.sharedPreferences.edit().putString(KEY_ALARM_CONTENT, note.text).apply()

                            notificationScheduler.scheduleNotification(
                                activityContext,
                                viewModel.dateAndTime.value
                            )
                        }
                    }
                }
            }
        },
        topBar = {
            FunctionalTopAppBar(
                themeColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                fontColor = scriptColor.value,
                sharedPrefs = sharedPrefs,
                navController = navController,
                priorityColorSelector = {
                    priorityColorIndex.intValue = it
                }
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (arguments.id != -1) Color(vmColor) else priorityColor.value),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* This is title menu */
            Card(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .padding(5.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                elevation = 10.dp
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else priorityColor.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else priorityColor.value
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(25.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = "notes title leading icon",
                            tint = fontColor.value
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 1,
                    textStyle = TextStyle(
                        color = scriptColor.value,
                        fontSize = fontSize.intValue.sp,
                        textAlign = TextAlign.Companion.Left,
                    ),
                    value = title.text,
                    onValueChange = {
                        viewModel.newNoteEvent(NewNoteEvent.ChangedTitle(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily(fontAmidoneGrotesk),
                            fontSize = fontSize.intValue.plus(7).sp,
                            color = scriptColor.value
                        )
                    }
                )
            }

            /* This is note content */
            Card(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = 10.dp
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = note.text,
                    onValueChange = {
                        viewModel.newNoteEvent(NewNoteEvent.ChangedNote(it))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else priorityColor.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else priorityColor.value
                    ),
                    leadingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(35.dp),
                            painter = painterResource(id = R.drawable.ic_notes),
                            contentDescription = "notes content leading icon",
                            tint = fontColor.value
                        )
                    },
                    textStyle = TextStyle(
                        color = scriptColor.value,
                        fontSize = fontSize.intValue.sp,
                        textAlign = TextAlign.Start
                    ),
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.note),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = fontSize.intValue.plus(7).sp,
                            color = scriptColor.value,
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }

            /* This is other options content */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
                    .padding(horizontal = 5.dp),
                onClick = {
                    isOptionsExpanded.value = !isOptionsExpanded.value
                },
                backgroundColor = priorityColor.value,
                shape = RoundedCornerShape(10.dp),
                elevation = 10.dp,
                border = BorderStroke(width = 1.dp, color = fontColor.value)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            modifier = Modifier
                                .size(30.dp),
                            imageVector = Icons.Default.Settings,
                            contentDescription = "repeating",
                            tint = fontColor.value
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Other options",
                            fontSize = fontSize.intValue.sp,
                            color = fontColor.value,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                    if (isOptionsExpanded.value) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp),
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "expand collapse other options content",
                            tint = fontColor.value
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(35.dp),
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "expand collapse other options content",
                            tint = fontColor.value
                        )
                    }
                }
            }
            if (isOptionsExpanded.value) {

                /* Colors */
                LazyRow(
                    modifier = Modifier
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    items(colorsList) { color ->
                        ColorsItem(color = color, fontColor = fontColor.value) {
                            when (color) {
                                themeColor.value -> {
                                    priorityColorIndex.intValue = 0
                                }

                                Color.White -> {
                                    priorityColorIndex.intValue = 1
                                }

                                yellow -> {
                                    priorityColorIndex.intValue = 2
                                }

                                green -> {
                                    priorityColorIndex.intValue = 3
                                }

                                liteGreen -> {
                                    priorityColorIndex.intValue = 4
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }

                /* This is alarm content */
                Card(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .padding(horizontal = 5.dp),
                    backgroundColor = themeColor.value,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, color = fontColor.value),
                    elevation = 10.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.set_alarm),
                                fontSize = fontSize.intValue.sp,
                                color = fontColor.value,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                            Switch(
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = green,
                                    checkedThumbColor = priorityColor.value,
                                    uncheckedThumbColor = priorityColor.value,
                                    uncheckedTrackColor = fontColor.value
                                ),
                                checked = isSwitched.value,
                                onCheckedChange = {
                                    isSwitched.value = it
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        if (isSwitched.value) {
                            SetAlarmContent(
                                themeColor = priorityColor.value,
                                fontColor = fontColor.value,
                                fontSize = fontSize.intValue,
                                onDatePicked = { pickedDate ->
                                    viewModel.saveDate(pickedDate)
                                },
                                onTimePicked = { pickedTime ->
                                    viewModel.saveTime(pickedTime)
                                }
                            )

                        }
                    }
                }
            }
        }
    }
}
