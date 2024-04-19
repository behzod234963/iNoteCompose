package coder.behzod.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.views.FunctionalTopAppBar
import coder.behzod.presentation.views.SpeedDialFAB

@Composable
fun NewNoteScreen(
    navController: NavController,
    notesModel: NotesModel?,
    sharedPrefs: SharedPreferenceInstance
) {
    val newNote = remember { mutableStateOf("") }
    val newTitle = remember { mutableStateOf("") }
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

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themeColor.value)
        ) {
            FunctionalTopAppBar(
                themeColor = themeColor.value,
                fontColor = fontColor.value,
                navController
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = themeColor.value,
                        unfocusedContainerColor = themeColor.value
                    ),
                    textStyle = TextStyle(
                        color = fontColor.value,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                    ),
                    singleLine = true,
                    value = newTitle.value,
                    onValueChange = {
                        newTitle.value = it
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
                        .padding(5.dp)
                        .align(Alignment.Start),
                    value = newNote.value,
                    onValueChange = {
                        newNote.value = it
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = themeColor.value,
                        unfocusedContainerColor = themeColor.value
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.note),
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontFamily = FontFamily(fontAmidoneGrotesk),
                            fontSize = 25.sp,
                            color = fontColor.value
                        )
                    }
                )
            }
        }
        SpeedDialFAB(modifier = Modifier
            .padding(bottom = 20.dp, end = 20.dp),
            onSave = {

            }) {

        }
    }
}

@Preview
@Composable
private fun PreviewNewNote() {
    val ctx = LocalContext.current
    NewNoteScreen(
        navController = NavController(ctx),
        notesModel = null,
        sharedPrefs = SharedPreferenceInstance(ctx)
    )
}