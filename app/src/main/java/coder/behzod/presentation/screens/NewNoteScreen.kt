package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.items.ColorsItem
import coder.behzod.presentation.navigation.Arguments
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.blue
import coder.behzod.presentation.theme.cyan
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.red
import coder.behzod.presentation.theme.yellow
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.colorsList
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.viewModels.NewNoteViewModel
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.SetAlarmContent
import coder.behzod.presentation.views.SpeedDialFAB
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NewNoteScreen(
    navController: NavHostController,
    arguments: Arguments,
    sharedPrefs: SharedPreferenceInstance,
    viewModel: NewNoteViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val ctx = LocalContext.current.applicationContext
    val activityContext = LocalContext.current as Activity

    val note = viewModel.note.value

    val title = viewModel.title.value

    val vmColor = viewModel.color.value

    val date = remember { mutableIntStateOf(0) }

    val alarmStatus = remember { mutableStateOf( false ) }
    val isDatePicked = remember { mutableStateOf( false ) }
    val isTimePicked = remember { mutableStateOf( false ) }
    val triggerDate = remember { mutableIntStateOf( 0 ) }
    val triggerTime = remember { mutableLongStateOf( 0 ) }

    val pickedDate = viewModel.dateAndTime

    val themeIndex =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val themeColor =
        remember { mutableStateOf(if (themeIndex.intValue == 0) Color.Black else Color.White) }

    val fontColor = remember { mutableStateOf(if (themeColor.value == Color.Black) Color.White else Color.Black) }

    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isSwitched = remember { mutableStateOf(false) }
    val isOptionsExpanded = remember { mutableStateOf(false) }

    /* Color identifier */
    when (themeIndex.intValue) {
        0->{
            themeColor.value = Color.Black
            fontColor.value = Color.White
        }
        1->{
            themeColor.value = Color.White
            fontColor.value = Color.Black
        }
        2 -> {
            themeColor.value = yellow
            fontColor.value = Color.White
        }

        3 -> {
            themeColor.value = green
            fontColor.value = Color.White
        }

        4 -> {
            themeColor.value = cyan
            fontColor.value = Color.White
        }

        5 -> {
            themeColor.value = red
            fontColor.value = Color.White
        }

        6 -> {
            themeColor.value = blue
            fontColor.value = Color.White
        }
    }
    if (arguments.id != -1){
        when (vmColor) {
            Color.Black.toArgb() -> {
                fontColor.value = Color.White
            }

            Color.White.toArgb() -> {
                fontColor.value = Color.Black
            }
        }
    }

    val isKeyboardShown = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier
            .background(if (arguments.id != -1) Color(vmColor) else themeColor.value),
        floatingActionButton = {
            SpeedDialFAB(
                fontColor = fontColor.value,
                themeColor = themeColor.value,
                modifier = Modifier
                    .padding(bottom = 20.dp, end = 20.dp),
                labelFirst = stringResource(R.string.save),
                labelSecond = stringResource(R.string.share_and_save),
                painterFirst = R.drawable.ic_save,
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

                            if(isDatePicked.value && isTimePicked.value){
                                alarmStatus.value = true
                            }else{
                                Toast.makeText(
                                    activityContext,
                                    "invalid date or time date: ${pickedDate.value}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            viewModel.saveNote(
                                NotesModel(
                                    id = arguments.id,
                                    title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") ""
                                    else title
                                        .text.capitalize(),
                                    content = note.text,
                                    color = vmColor,
                                    dataAdded = date.intValue.toString().dateFormatter(),
                                    alarmStatus = alarmStatus.value,
                                    triggerDate = triggerDate.intValue,
                                    triggerTime = triggerTime.longValue
                                )
                            )
                        } else {

                            if(isDatePicked.value && isTimePicked.value){

                                alarmStatus.value = true
                            }else{
                                Toast.makeText(
                                    activityContext,
                                    "invalid date or time date: ${pickedDate.value}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            viewModel.saveNote(
                                NotesModel(
                                    title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") ""
                                    else title
                                        .text.capitalize(),
                                    content = note.text,
                                    color = themeColor.value.toArgb(),
                                    dataAdded = date.intValue.toString().dateFormatter(),
                                    alarmStatus = alarmStatus.value,
                                    triggerDate = triggerDate.intValue,
                                    triggerTime = triggerTime.longValue
                                )
                            )
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

                        if (isDatePicked.value && isTimePicked.value){
                            alarmStatus.value = true
                        }else{
                            Toast.makeText(
                                activityContext,
                                "invalid date or time date: ${pickedDate.value}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        viewModel.saveNote(
                            NotesModel(
                                id = arguments.id,
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.also {
                                        it.capitalize()
                                    },
                                content = note.text,
                                color = vmColor,
                                dataAdded = date.intValue.toString().dateFormatter(),
                                alarmStatus = alarmStatus.value,
                                triggerDate = triggerDate.intValue,
                                triggerTime = triggerTime.longValue
                            )
                        )
                    } else {

                        ShareNote().execute(
                            title = title.text,
                            content = note.text,
                            ctx = activityContext,
                        ) {
                            navController.navigate(ScreensRouter.MainScreenRoute.route)
                        }

                        if (isDatePicked.value && isTimePicked.value){

                            alarmStatus.value = true
                        }else{
                            Toast.makeText(
                                activityContext,
                                "invalid date or time date: ${pickedDate.value}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        viewModel.saveNote(
                            NotesModel(
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.capitalize(),
                                content = note.text,
                                color = themeColor.value.toArgb(),
                                dataAdded = date.intValue.toString().dateFormatter(),
                                alarmStatus = alarmStatus.value,
                                triggerDate = triggerDate.intValue,
                                triggerTime = triggerTime.longValue
                            )
                        )
                    }
                }
            }
        },
        topBar = {
            FunctionalTopAppBar(
                themeColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                fontColor = fontColor.value,
                fontSize = fontSize.intValue,
                sharedPrefs = sharedPrefs,
                navController = navController
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            if (isKeyboardShown.value) {
                                keyboardController?.hide()
                                isKeyboardShown.value = false
                            }
                        }
                    )
                }
                .background(if (arguments.id != -1) Color(vmColor) else themeColor.value),
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
                elevation = 10.dp,
                onClick = {
                    isKeyboardShown.value = true
                }
            ) {
                OutlinedTextField(
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else themeColor.value
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
                        color = fontColor.value,
                        fontSize = fontSize.intValue.sp,
                        textAlign = TextAlign.Companion.Left,
                    ),
                    value = title.text,
                    onValueChange = {
                        isKeyboardShown.value = true
                        viewModel.newNoteEvent(NewNoteEvent.ChangedTitle(it))
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = FontFamily(fontAmidoneGrotesk),
                            fontSize = fontSize.intValue.plus(7).sp,
                            color = fontColor.value
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
                        isKeyboardShown.value = true
                        viewModel.newNoteEvent(NewNoteEvent.ChangedNote(it))
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else themeColor.value
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
                        color = fontColor.value,
                        fontSize = fontSize.intValue.sp,
                        textAlign = TextAlign.Start
                    ),
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.note),
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = fontSize.intValue.plus(7).sp,
                            color = fontColor.value,
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
                backgroundColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                shape = RoundedCornerShape(10.dp),
                elevation = 10.dp,
                border = BorderStroke(width = 1.dp, color = fontColor.value)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .background(if (arguments.id != -1) Color(vmColor) else themeColor.value),
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
                            tint = if (arguments.id != -1) Color(vmColor) else fontColor.value
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
                            themeColor.value = color
                            viewModel.newNoteEvent(NewNoteEvent.NoteBackground(color.toArgb()))
                            when (color) {
                                Color.White -> {
                                    themeIndex.intValue = 1
                                    fontColor.value = Color.White
                                }

                                yellow -> {
                                    themeIndex.intValue = 2
                                }

                                green -> {
                                    themeIndex.intValue = 3
                                }

                                cyan -> {
                                    themeIndex.intValue = 4
                                }

                                red -> {
                                    themeIndex.intValue = 5
                                }

                                blue -> {
                                    themeIndex.intValue = 6
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
                    backgroundColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
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
                                    checkedThumbColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                                    uncheckedThumbColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
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
                                themeColor = if (arguments.id != -1) Color(vmColor) else themeColor.value,
                                fontColor = fontColor.value,
                                fontSize = fontSize.intValue,
                                onDateSet = { triggerDate.intValue = it },
                                onTimeSet = { triggerTime.longValue = it },
                                onPicked = {date,time->
                                    isDatePicked.value = date
                                    isTimePicked.value = time
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
