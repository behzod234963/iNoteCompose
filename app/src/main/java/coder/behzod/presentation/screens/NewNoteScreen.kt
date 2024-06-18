package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import coder.behzod.presentation.items.ColorsItem
import coder.behzod.presentation.navigation.Arguments
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.colorList
import coder.behzod.presentation.utils.events.NewNoteEvent
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.utils.extensions.dataFormatter
import coder.behzod.presentation.viewModels.NewNoteViewModel
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.SpeedDialFAB
import kotlinx.coroutines.launch
import java.time.LocalDate

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

    Scaffold(
        modifier = Modifier
            .background(if (arguments.id != -1) Color(vmColor) else themeColor.value),
        floatingActionButton = {
            SpeedDialFAB(modifier = Modifier
                .padding(bottom = 20.dp, end = 20.dp),
                labelFirst = stringResource(R.string.save),
                painterFirst = R.drawable.ic_save,
                labelSecond = stringResource(R.string.share),
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
                        ){
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
                        ){
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* Color select content */
            LazyRow(
                modifier = Modifier.height(70.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                items(colorList) {
                    ColorsItem(color = it, fontColor = scriptColor.value, onClick = {
                        coroutineScope.launch {
                            color.value = it
                            viewModel.newNoteEvent(NewNoteEvent.NoteBackground(it.toArgb()))
                            when (it) {
                                Color.Black -> {
                                    scriptColor.value = Color.White
                                }

                                Color.White -> {
                                    scriptColor.value = Color.Black
                                }

                                Color.Red -> {
                                    scriptColor.value = Color.Black
                                }

                                Color.Magenta -> {
                                    scriptColor.value = Color.Black
                                }

                                Color.Blue -> {
                                    scriptColor.value = Color.White
                                }

                                Color.Cyan -> {
                                    scriptColor.value = Color.Black
                                }

                                Color.DarkGray -> {
                                    scriptColor.value = Color.White
                                }

                                Color.Green -> {
                                    scriptColor.value = Color.Black
                                }

                                Color.Yellow -> {
                                    scriptColor.value = Color.Black
                                }
                            }
                        }
                    })
                }
            }
            /* This is title menu */
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value,
                    unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value
                ),
                maxLines = 1,
                textStyle = TextStyle(
                    color = scriptColor.value,
                    fontSize = 32.sp,
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
                        fontSize = 25.sp,
                        color = scriptColor.value
                    )
                })
            /* This is note menu */
            OutlinedTextField(modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
                .padding(5.dp),
                value = note.text,
                onValueChange = {
                    viewModel.newNoteEvent(NewNoteEvent.ChangedNote(it))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value,
                    unfocusedContainerColor = if (arguments.id != -1) Color(vmColor) else color.value
                ),
                textStyle = TextStyle(
                    color = scriptColor.value,
                    fontSize = fontSize.intValue.sp,
                    textAlign = TextAlign.Start
                ),
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.note),
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = fontSize.intValue.plus(7).sp,
                        color = scriptColor.value,
                        textAlign = TextAlign.Center
                    )
                })
        }
    }
}