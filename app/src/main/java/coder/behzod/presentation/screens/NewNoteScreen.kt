package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.navigation.Arguments
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.extensions.dataFormatter
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.viewModels.NewNoteViewModel
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.PrioritySelectButtons
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

    val color = remember { mutableStateOf(themeColor.value) }
    val scriptColor = remember { mutableStateOf(fontColor.value) }

    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val priorityList = listOf(
        Pair(stringResource(R.string.priority_low), Color.White),
        Pair(stringResource(R.string.priority_medium), Color.Yellow),
        Pair(stringResource(R.string.priority_high), Color.Green),
        Pair(stringResource(R.string.priority_immediate), Color.Red)
    )

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
                                    color = color.value.toArgb(),
                                    dataAdded = date.value.toString().dataFormatter()
                                )
                            )
                        } else {
                            viewModel.saveNote(
                                NotesModel(
                                    title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                        .text.capitalize(),
                                    content = note.text,
                                    color = color.value.toArgb(),
                                    dataAdded = date.value.toString().dataFormatter()
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

                        viewModel.saveNote(
                            NotesModel(
                                id = arguments.id,
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.also {
                                        it.capitalize()
                                    },
                                content = note.text,

                                color = color.value.toArgb(),
                                dataAdded = date.value.toString().dataFormatter()
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

                        viewModel.saveNote(
                            NotesModel(
                                title = if (title.text.isBlank() && title.text.isEmpty() && title.text == "") "" else title
                                    .text.capitalize(),
                                content = note.text,
                                color = color.value.toArgb(),
                                dataAdded = date.value.toString().dataFormatter()
                            )
                        )
                    }
                }
            }
        },
        topBar = {
            FunctionalTopAppBar(
                themeColor = if (arguments.id != -1) Color(vmColor) else color.value,
                fontColor = scriptColor.value,
                navController = navController,
                sharedPrefs = sharedPrefs
            )
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(if (arguments.id != -1) Color(vmColor) else color.value),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* This is priority level select */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.priority),
                    color = scriptColor.value,
                    fontFamily = FontFamily(fontAmidoneGrotesk),
                    fontSize = fontSize.intValue.sp
                )

                PrioritySelectButtons(
                    items = priorityList,
                    onItemSelected = {},
                    themeColor = themeColor.value,
                    fontColor = fontColor.value
                )
            }

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
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value
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
                        focusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value,
                        unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value
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
                backgroundColor = themeColor.value,
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
                    Text(
                        text = "Other options",
                        fontSize = fontSize.intValue.sp,
                        color = fontColor.value,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
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

                /* this is photo content */
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp)
                        .padding(horizontal = 10.dp)
                        .height(180.dp),
                    onClick = {
                        TODO()
                    },
                    backgroundColor = themeColor.value,
                    shape = RoundedCornerShape(10.dp),
                    elevation = 10.dp,
                    border = BorderStroke(width = 1.dp, color = fontColor.value)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp)
                                .padding(start = 5.dp, top = 5.dp),
                            painter = painterResource(id = R.drawable.ic_add_photp),
                            contentDescription = "add photo",
                            tint = fontColor.value
                        )
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
                            checked = isSwitched.value,
                            onCheckedChange = {
                                isSwitched.value = it
                            }
                        )
                    }
                }
            }
        }
    }
}
