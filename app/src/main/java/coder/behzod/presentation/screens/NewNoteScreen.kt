package coder.behzod.presentation.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.colorList
import coder.behzod.presentation.viewModels.NewNoteViewModel
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.SpeedDialFAB
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewNoteScreen(
    navController: NavController,
    id:Int?,
    notesModel: NotesModel?,
    sharedPrefs: SharedPreferenceInstance,
    viewModel: NewNoteViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current.applicationContext
    val note = remember { mutableStateOf( "" ) }
    var title = remember { mutableStateOf( "" ) }
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
    if (id != -1){
        title.value = viewModel.title.value.toString()
        note.value = viewModel.note.value.toString()
    }
    Log.d("debug", "$id screen")
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color.value),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FunctionalTopAppBar(
                themeColor = color.value,
                fontColor = scriptColor.value,
                navController = navController,
                sharedPrefs = sharedPrefs
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
            ) {
                LazyRow(
                    modifier = Modifier
                        .height(70.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    items(colorList) {
                        ColorsItem(color = it, fontColor = scriptColor.value, onClick = {
                            color.value = it
                            scriptColor.value = if (it == Color.White) {
                                Color.Black
                            } else {
                                Color.White
                            }
                        })
                    }
                }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(5.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = color.value,
                        unfocusedContainerColor = color.value
                    ),
                    textStyle = TextStyle(
                        color = scriptColor.value,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Companion.Left,
                    ),
                    value = title.value,
                    onValueChange = {
                        title.value = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontFamily = FontFamily(fontAmidoneGrotesk),
                            fontSize = 25.sp,
                            color = fontColor.value
                        )
                    }
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 10.dp)
                        .padding(5.dp),
                    value = note.value,
                    onValueChange = {
                        note.value = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = color.value,
                        unfocusedContainerColor = color.value
                    ),
                    textStyle = TextStyle(
                        color = scriptColor.value,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.note),
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 25.sp,
                            color = scriptColor.value,
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }
        }
        SpeedDialFAB(modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 20.dp, end = 20.dp),
            onSave = {
                viewModel.saveNote(
                    NotesModel(
                        title = title.value,
                        note = note.value,
                        color = color.value.toArgb(),
                        dataAdded = date.value.toString()
                    )
                )
                navController.navigate(ScreensRouter.MainScreenRoute.route)
            }) {
            viewModel.shareAndSaveNote(
                NotesModel(
                    title = title.value,
                    note = note.value,
                    color = color.value.toArgb(),
                    dataAdded = date.value.toString()
                ),
                text = "$title" +
                        "$note",
                ctx
            )
        }
    }
}