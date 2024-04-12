package coder.behzod.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.views.FunctionalTopAppBar
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun NewNoteScreen(
    navController: NavController,
    notesModel: NotesModel?,
    sharedPrefs: SharedPreferenceInstance
) {
    Log.d("AAA", "NewNoteScreens: is started")
    val newNote = remember { mutableStateOf("") }
    val newTitle = remember { mutableStateOf("") }
    val simpleDataFormat = SimpleDateFormat("'dd-MM-yyyy'")
    val currentData = simpleDataFormat.format(Date())
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.value)
    ) {
        FunctionalTopAppBar(
            onSave = {
//                this will save notes in database
            },
            onShare = {

            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp)
        ) {
            OutlinedTextField(
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(color = Color.White, width = 2.dp)
                    .background(Color.Black),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black
                ),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                ),
                value = newTitle.value,
                onValueChange = {
                    newTitle.value = it
                },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(id = R.string.title),
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
                    .border(width = 2.dp, color = Color.White),
                value = newNote.value,
                onValueChange = {
                    newNote.value = it
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk),
                    textAlign = TextAlign.Start
                ),
                placeholder = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.note),
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
            )
        }
    }
}