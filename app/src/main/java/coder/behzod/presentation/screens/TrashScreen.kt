package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.TrashScreenItem
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.deletedNotes
import coder.behzod.presentation.utils.constants.notes
import coder.behzod.presentation.utils.events.TrashEvent
import coder.behzod.presentation.viewModels.TrashViewModel
import coder.behzod.presentation.views.BottomNavigationView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun TrashScreen(
    sharedPrefs: SharedPreferenceInstance,
    navController: NavController,
    viewModel: TrashViewModel = hiltViewModel(),
) {
    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )
    val btnTrashAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_trash)
    )

    val themeIndex =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    if (colorTheme == Color.Black) themeColor.value = Color.Black else themeColor.value =
        Color.White

    val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
    val fontColor = remember { mutableStateOf(colorFont) }

    if (colorFont == Color.White) fontColor.value = Color.White else fontColor.value = Color.Black

    val isDialogVisible = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }

    val selectedItems = viewModel.selectedItems.value
    val selectedItemsCount = remember { mutableIntStateOf(0) }
    val trashedNotes = viewModel.trashedNotes.value

    val selectedItemsStatus = remember { mutableStateOf(false) }
    val isSelected = remember { mutableStateOf(false) }

    val isTrashedNotesDeleted = remember { mutableIntStateOf(1) }

    val trashedNote = remember {
        mutableStateOf(
            TrashModel(
                title = "",
                content = "",
                color = 1,
                daysLeft = 1
            )
        )
    }
    val isExpanded = remember { mutableStateOf(false) }
    val selectAllStatus = viewModel.isItemSelected.value

    Scaffold(
        containerColor = themeColor.value,
        bottomBar = {
            BottomNavigationView(
                themeColor = themeColor.value,
                fontColor = fontColor.value,
                navController = navController
            )
        },
        floatingActionButton = {
            if (isSelected.value) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = 30.dp, bottom = 30.dp),
                    containerColor = Color.Magenta,
                    shape = CircleShape,
                    onClick = {
                        isPlaying.value = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            isPlaying.value = false
                            isSelected.value = false
                            isDialogVisible.value = true
                            selectedItemsCount.intValue = selectedItems.size
                            if (selectAllStatus){
                                isTrashedNotesDeleted.intValue = 3
                            }else{
                                isTrashedNotesDeleted.intValue = 1
                            }
                        }, 1000)
                    }) {
                    if (isPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(35.dp),
                            composition = btnTrashAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "button add",
                            tint = fontColor.value
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            if (isSelected.value) {
                TopAppBar(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = fontColor.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = if (selectedItemsCount.intValue == 0) "0 ${stringResource(id = R.string.items_selected)}"
                            else "${selectedItemsCount.intValue} ${stringResource(id = R.string.items_selected)}",
                            color = fontColor.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back button",
                            tint = fontColor.value
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            selectedItemsStatus.value = true
                            if (selectedItemsStatus.value) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    selectedItemsStatus.value = false
                                    isSelected.value = false
                                    viewModel.onEvent(TrashEvent.ClearList(selectedItems))
                                    selectedItemsCount.intValue = 0
                                }, 1000)
                            }

                        }) {
                            if (selectedItemsStatus.value) {
                                LottieAnimation(
                                    composition = btnCloseAnim.value,
                                    modifier = Modifier
                                        .size(35.dp),
                                    iterations = LottieConstants.IterateForever
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "btn close",
                                    tint = fontColor.value
                                )
                            }
                        }
                    }
                )
            } else {
                TopAppBar(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = fontColor.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.deleted_notes),
                            color = fontColor.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back button",
                            tint = fontColor.value
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isExpanded.value = !isExpanded.value
                            }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more functions for trashed notes",
                                tint = fontColor.value
                            )
                        }
                        DropdownMenu(
                            expanded = isExpanded.value,
                            onDismissRequest = {
                                isExpanded.value = false
                            }) {
//                            DropDownMenu Item for content "Select"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.select),
                                        color = themeColor.value,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    viewModel.onEvent(TrashEvent.SelectAll(false), null)
                                    viewModel.onEvent(TrashEvent.ClearList(selectedItems))
                                    selectedItemsCount.intValue = selectedItems.size
                                    isSelected.value = true
                                    isExpanded.value = false
                                }
                            )
//                            DropDownMenu Item for content "Select all"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.select_all),
                                        color = themeColor.value,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isSelected.value = true
                                    viewModel.onEvent(TrashEvent.SelectAll(true))
                                }
                            )
//                            DropDownMenu Item for content "Restore all"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.restore_all),
                                        color = themeColor.value,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    viewModel.onEvent(
                                        TrashEvent.RestoreAllNotes(deletedNotes),
                                        notes
                                    )
                                    isTrashedNotesDeleted.intValue = 4
                                }
                            )
//                            DropDownMenu Item for content "Delete all"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(R.string.delete_all),
                                        color = themeColor.value,
                                        fontSize = 18.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isDialogVisible.value = true
                                    viewModel.onEvent(TrashEvent.SelectAll(true))
                                    isTrashedNotesDeleted.intValue = 3
                                }
                            )
                        }
                    }
                )
            }
        }
    ) {
        if (isDialogVisible.value) {
            Column(
                modifier = Modifier
                    .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AlertDialog(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.Gray)
                        .border(
                            color = fontColor.value,
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    shape = RoundedCornerShape(10.dp),
                    backgroundColor = themeColor.value,
                    text = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = when (isTrashedNotesDeleted.intValue) {
                                    1 -> {
                                        stringResource(id = R.string.delete_selected_notes)
                                    }

                                    2 -> {
                                        stringResource(R.string.action_with_a_note)
                                    }

                                    3 -> {
                                        stringResource(id = R.string.delete_all_notes)
                                    }

                                    else -> {
                                        ""
                                    }
                                },
                                color = fontColor.value,
                                fontSize = 23.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    },
                    onDismissRequest = {
                        isDialogVisible.value = false
                        isSelected.value = false
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = fontColor.value
                                ),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    if (isTrashedNotesDeleted.intValue == 3) {
                                        viewModel.restoreNote(
                                            trashModel = trashedNote.value,
                                            note = NotesModel(
                                                id = trashedNote.value.id,
                                                title = trashedNote.value.title,
                                                note = trashedNote.value.content,
                                                color = trashedNote.value.color,
                                                dataAdded = LocalDate.now().toString()
                                            )
                                        )
                                        isDialogVisible.value = false
                                        isSelected.value = false
                                    } else {
                                        isDialogVisible.value = false
                                        isSelected.value = false
                                    }
                                }
                            ) {
                                Text(
                                    text = if (isTrashedNotesDeleted.intValue != 2) stringResource(R.string.cancel)
                                    else stringResource(R.string.restore),
                                    color = themeColor.value,
                                    fontSize = 18.sp
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = fontColor.value
                                ),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    when (isTrashedNotesDeleted.intValue) {

                                        /*This is delete selected */
                                        1 -> {
                                            viewModel.multipleDelete(selectedItems)
                                        }

                                        /*This is delete*/
                                        2 -> {
                                            viewModel.delete(trashedNote.value)
                                        }

                                        /*This is clear*/
                                        3 -> {
                                            viewModel.multipleDelete(trashedNotes)
                                        }

                                        4->{
                                            /*This will be restore all TODO()*/
                                        }
                                    }
                                    selectedItemsCount.intValue = selectedItems.size
                                    isDialogVisible.value = false
                                }
                            ) {
                                Text(
                                    text = if (isTrashedNotesDeleted.intValue == 1
                                        || isTrashedNotesDeleted.intValue == 3
                                    ) "Ok"
                                    else stringResource(R.string.delete),
                                    color = themeColor.value,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                )
            }
        } else {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                LazyColumn {
                    items(trashedNotes) { selectedModel ->
                        if (selectAllStatus){
                            viewModel.onEvent(TrashEvent.ClearList(selectedItems))
                            viewModel.addAllToList(trashedNotes)
                            selectedItemsCount.intValue = selectedItems.size
                        }
                        TrashScreenItem(
                            model = selectedModel,
                            fontColor = fontColor.value,
                            onChange = {
                                if (it == 1) {
                                    if (selectAllStatus) {
                                        selectedItemsCount.intValue = selectedItems.size
                                    } else {
                                        viewModel.addToList(selectedModel)
                                        selectedItemsCount.intValue = selectedItems.size
                                    }
                                } else {
                                    viewModel.removeFromList(selectedModel)
                                    selectedItemsCount.intValue = selectedItems.size
                                }
                            },
                            isDialogVisible = { isDialogVisible.value = it },
                            selectedContent = { isTrashedNotesDeleted.intValue = it },
                            isSelected = isSelected.value,
                            onClick = {
                                trashedNote.value = selectedModel
                            }
                        )
                    }
                }
            }
        }
    }
}