package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.TrashScreenItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.notes
import coder.behzod.presentation.utils.events.TrashEvent
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.viewModels.TrashViewModel
import coder.behzod.presentation.views.AlertDialogInstance
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.SpeedDialFAB
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
    notesModel: NotesModel,
    sharedPrefs: SharedPreferenceInstance,
    navController: NavHostController,
    viewModel: TrashViewModel = hiltViewModel(),
) {

    val btnRestore = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_refresh_black)
    )

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

    val isDialogVisible = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }

    val selectedItems = viewModel.selectedItems.value
    val selectedItemsCount = remember { mutableIntStateOf(0) }
    val trashedNotes = viewModel.trashedNotes.value

    val selectedItemsStatus = remember { mutableStateOf(false) }
    val isSelected = remember { mutableStateOf(false) }

    val functionsCase = remember { mutableIntStateOf(1) }

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

    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isRestoreBtnClicked = remember { mutableStateOf(false) }

    val notesList = ArrayList<NotesModel>()

    val fontColor = remember {
        mutableStateOf(
            when (notesModel.color) {

                /* Color for title,when title color is Black */
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
                    Color.LightGray
                }
            }
        )
    }

    val isFontSizeIsBig = remember { mutableStateOf(false) }

    Scaffold(
        containerColor = themeColor.value,
        bottomBar = {
            BottomNavigationView(
                themeColor = themeColor.value,
                fontColor = if (themeColor.value == Color.Black) Color.White else Color.Black,
                navController = navController
            )
        },
        floatingActionButton = {

            /* Floating action button close */
            if (isSelected.value) {
                SpeedDialFAB(
                    fontColor = fontColor.value,
                    themeColor = themeColor.value,
                    modifier = Modifier,
                    labelFirst = stringResource(id = R.string.delete),
                    labelSecond = stringResource(id = R.string.restore),
                    painterFirst = R.drawable.ic_delete,
                    painterSecond = R.drawable.ic_restore,
                    onClickFirst = {
                        /* Content for delete function */
                        if (selectAllStatus) {
                            functionsCase.intValue = 3
                            isDialogVisible.value = true
                            isSelected.value = false
                        } else {
                            functionsCase.intValue = 1
                            isDialogVisible.value = true
                            isSelected.value = false
                        }
                    }) {
                    /* Content for restore function */
                    if (selectAllStatus) {
                        functionsCase.intValue = 4
                        isDialogVisible.value = true
                        isSelected.value = false
                    } else {
                        isDialogVisible.value = true
                        isSelected.value = false
                        functionsCase.intValue = 5
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
                        .clip(RoundedCornerShape(10.dp))
                        .padding(5.dp),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = if (selectedItemsCount.intValue == 0) "0 ${stringResource(id = R.string.items_selected)}"
                            else "${selectedItemsCount.intValue} ${stringResource(id = R.string.items_selected)}",
                            color = if (themeColor.value == Color.Black) Color.White else fontColor.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            isSelected.value = false
                            viewModel.onEvent(
                                event = TrashEvent.ClearList(selectedItems),
                                trashedNotes = trashedNotes
                            )
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "back button",
                                tint = if (themeColor.value == Color.Black) Color.White else fontColor.value
                            )
                        }
                    },
                    actions = {

                        /* Button close */
                        IconButton(onClick = {
                            selectedItemsStatus.value = true
                            if (selectedItemsStatus.value) {
                                Handler(Looper.getMainLooper()).postDelayed({

                                    selectedItemsStatus.value = false
                                    viewModel.onEvent(
                                        event = TrashEvent.SelectAll(false),
                                        trashedNotes = trashedNotes
                                    )
                                    isSelected.value = false

                                    viewModel.onEvent(
                                        event = TrashEvent.ClearList(selectedItems),
                                        trashedNotes = trashedNotes
                                    )
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
                                    tint = if (themeColor.value == Color.Black) Color.White else fontColor.value
                                )
                            }
                        }
                    }
                )
            } else {
                TopAppBar(
                    modifier = Modifier
                        .padding(end = 5.dp, top = 5.dp, start = 5.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.deleted_notes),
                            color = if (themeColor.value == Color.Black) Color.White else Color.Black,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigate(ScreensRouter.MainScreenRoute.route)
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back),
                                contentDescription = "back button",
                                tint = if (themeColor.value == Color.Black) Color.White else Color.Black
                            )
                        }
                    },
                    actions = {

                        /* Button more */
                        IconButton(
                            onClick = {
                                isExpanded.value = !isExpanded.value
                            }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "more functions for trashed notes",
                                tint = if (themeColor.value == Color.Black) Color.White else Color.Black
                            )
                        }
                        DropdownMenu(
                            expanded = isExpanded.value,
                            modifier = Modifier
                                .background(if (fontColor.value == Color.Black) Color.White else Color.Black),
                            onDismissRequest = {
                                isExpanded.value = false
                            }) {

                            /* DropDownMenu Item for content "Select without case" */
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp),
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "select",
                                        tint = fontColor.value
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(R.string.select),
                                        color = fontColor.value,
                                        fontSize = fontSize.intValue.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    viewModel.onEvent(
                                        TrashEvent.SelectAll(false),
                                        trashedNotes
                                    )
                                    viewModel.onEvent(
                                        event = TrashEvent.ClearList(selectedItems),
                                        trashedNotes = trashedNotes
                                    )
                                    selectedItemsCount.intValue = selectedItems.size
                                    isSelected.value = true
                                    isExpanded.value = false
                                }
                            )
                            HorizontalDivider()

                            /* DropDownMenu Item for content "Select all without case" */
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(25.dp),
                                        painter = painterResource(id = R.drawable.ic_select_all),
                                        contentDescription = "view",
                                        tint = fontColor.value
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(R.string.select_all),
                                        color = fontColor.value,
                                        fontSize = fontSize.intValue.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isSelected.value = true
                                    viewModel.onEvent(
                                        TrashEvent.SelectAll(true),
                                        trashedNotes
                                    )
                                }
                            )
                            HorizontalDivider()

                            /* DropDownMenu Item for content "Restore all case-4" */
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp),
                                        painter = painterResource(id = R.drawable.ic_restore),
                                        contentDescription = "restore",
                                        tint = fontColor.value
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(R.string.restore_all),
                                        color = fontColor.value,
                                        fontSize = fontSize.intValue.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isDialogVisible.value = true
                                    functionsCase.intValue = 4
                                }
                            )
                            HorizontalDivider()

                            /* DropDownMenu Item for content "Delete all  case-3" */
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier
                                            .size(25.dp),
                                        painter = painterResource(id = R.drawable.ic_delete_all),
                                        contentDescription = "view",
                                        tint = fontColor.value
                                    )
                                },
                                text = {
                                    Text(
                                        text = stringResource(R.string.delete_all),
                                        color = fontColor.value,
                                        fontSize = fontSize.intValue.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                onClick = {
                                    isExpanded.value = false
                                    isDialogVisible.value = true
                                    viewModel.onEvent(
                                        event = TrashEvent.SelectAll(true),
                                        trashedNotes = trashedNotes
                                    )
                                    functionsCase.intValue = 3
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

                AlertDialogInstance(
                    showDialog = isDialogVisible.value,
                    fontSize = fontSize.intValue,
                    icon = null,
                    title = null,
                    text = when (functionsCase.intValue) {
                        1 -> {
                            stringResource(id = R.string.delete_selected_notes)
                        }

                        2 -> {
                            stringResource(R.string.action_with_a_note)
                        }

                        3 -> {
                            stringResource(id = R.string.delete_all_notes)
                        }

                        4 -> {
                            stringResource(id = R.string.restore_all)
                        }

                        5 -> {
                            stringResource(id = R.string.restore_selected)
                        }

                        else -> {
                            ""
                        }
                    },
                    confirmButtonText = when (functionsCase.intValue) {
                        1 -> {
                            stringResource(id = R.string.delete)
                        }

                        2 -> {
                            stringResource(id = R.string.delete)
                        }

                        3 -> {
                            stringResource(id = R.string.delete)
                        }

                        4 -> {
                            stringResource(id = R.string.restore)
                        }

                        5 -> {
                            stringResource(id = R.string.restore)
                        }

                        else -> {
                            ""
                        }
                    },
                    confirmButton = {

                        when (functionsCase.intValue) {

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

                            4 -> {
                                viewModel.onEvent(
                                    event = TrashEvent.RestoreAllNotes(
                                        notesModelList = notes,
                                        trashedNotesList = trashedNotes,
                                    ),
                                    trashedNotes = trashedNotes
                                )

                            }

                            5 -> {
                                /* This will be restore selected function */
                                viewModel.onEvent(
                                    event = TrashEvent.RestoreSelected(),
                                    trashedNotes = trashedNotes
                                )
                            }
                        }
                        selectedItemsCount.intValue = selectedItems.size
                        isDialogVisible.value = false
                    },
                    dismissButtonText = if (functionsCase.intValue != 2) stringResource(
                        R.string.cancel
                    )
                    else stringResource(R.string.restore),
                    dismissButton = {

                        if (functionsCase.intValue == 2) {
                            viewModel.restoreNote(
                                trashModel = trashedNote.value,
                                note = NotesModel(
                                    id = trashedNote.value.id,
                                    title = trashedNote.value.title,
                                    content = trashedNote.value.content,
                                    color = trashedNote.value.color,
                                    dataAdded = LocalDate.now().toString()
                                        .dateFormatter(),
                                    requestCode = 1,
                                    stopCode = 3
                                )
                            )
                            isDialogVisible.value = false
                            isSelected.value = false
                        } else {
                            isDialogVisible.value = false
                            isSelected.value = false
                        }
                    }) {
                    isDialogVisible.value = false
                    isSelected.value = false
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Spacer(modifier = Modifier.height(60.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 5.dp, bottom = 70.dp)
                ) {
                    items(trashedNotes) { selectedModel ->

                        if (selectAllStatus) {
                            viewModel.onEvent(
                                event = TrashEvent.ClearList(selectedItems),
                                trashedNotes = trashedNotes
                            )
                            viewModel.addAllToList(trashedNotes)
                            selectedItemsCount.intValue = selectedItems.size
                        }

                        /* check the object after 30 day */
                        if (selectedModel.daysLeft <= 0) {
                            viewModel.delete(selectedModel)
                        }
                        TrashScreenItem(
                            model = selectedModel,
                            fontColor = fontColor.value,
                            fontSize = fontSize.intValue,
                            onClick = {
                                trashedNote.value = selectedModel
                            },
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
                            selectedContent = { functionsCase.intValue = it },
                            isSelected = isSelected.value,
                            sharedPref = sharedPrefs
                        )
                    }
                }
            }
        }
    }
}