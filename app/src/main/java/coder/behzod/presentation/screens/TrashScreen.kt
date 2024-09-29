package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.TrashScreenItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.red
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.notes
import coder.behzod.presentation.utils.events.TrashEvent
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.viewModels.TrashViewModel
import coder.behzod.presentation.views.AlertDialogs
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.SpeedDialFAB
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState",
    "CoroutineCreationDuringComposition"
)
@Composable
fun TrashScreen(
    notesModel: NotesModel,
    sharedPrefs: SharedPreferenceInstance,
    navController: NavHostController,
    viewModel: TrashViewModel = hiltViewModel(),
) {

    val ctx = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )

    val themeIndex =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    if (colorTheme == Color.Black) themeColor.value = Color.Black else themeColor.value =
        Color.White

    val isDialogVisible = remember { mutableStateOf(false) }

    val selectedItems = viewModel.selectedItems.value
    val selectedItemsCount = remember { mutableIntStateOf(0) }
    val trashedNotes = viewModel.trashedNotes.value

    val closeSelectorMenu = remember { mutableStateOf(false) }
    val isSelected = remember { mutableStateOf(false) }
    val selectAllStatus = viewModel.isItemSelected.value
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

    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

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
    val dataStoreInstance = DataStoreInstance(ctx)
    coroutineScope.launch { dataStoreInstance.mainScreenState(false) }
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
                    fontColor = Color.Magenta,
                    themeColor = Color.Cyan,
                    modifier = Modifier,
                    labelFirst = stringResource(id = R.string.delete),
                    labelSecond = stringResource(id = R.string.restore),
                    painterFirst = R.drawable.ic_delete,
                    painterSecond = R.drawable.ic_restore,
                    onClickFirst = {
                        /* Content for delete function */
                        if (selectAllStatus) {
                            if (selectedItems.isEmpty() || selectedItems.size == 0){
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        ctx.getString(R.string.nothing_is_selected)
                                    )
                                }
                                functionsCase.intValue = 0
                                isDialogVisible.value = false
                                isSelected.value = true
                            }else{
                                functionsCase.intValue = 3
                                isDialogVisible.value = true
                                isSelected.value = false
                            }
                        } else {
                            if (selectedItems.isEmpty() || selectedItems.size == 0){
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        ctx.getString(R.string.nothing_is_selected)
                                    )
                                }
                                functionsCase.intValue = 0
                                isDialogVisible.value = false
                                isSelected.value = true
                            }else{
                                functionsCase.intValue = 1
                                isDialogVisible.value = true
                                isSelected.value = false
                            }
                        }
                    }) {
                    /* Content for restore function */
                    if (selectAllStatus) {
                        if (selectedItems.isEmpty() || selectedItems.size == 0){
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    ctx.getString(R.string.nothing_is_selected)
                                )
                            }
                            functionsCase.intValue = 0
                            isDialogVisible.value = false
                            isSelected.value = true
                        }else{
                            functionsCase.intValue = 4
                            isDialogVisible.value = true
                            isSelected.value = false
                        }
                    } else {
                        if (selectedItems.isEmpty() || selectedItems.size == 0){
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    ctx.getString(R.string.nothing_is_selected)
                                )
                            }
                            functionsCase.intValue = 0
                            isDialogVisible.value = false
                            isSelected.value = true
                        }else{
                            functionsCase.intValue = 5
                            isDialogVisible.value = true
                            isSelected.value = false
                        }
                    }
                }
            }else{
                viewModel.clearList()
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
                            closeSelectorMenu.value = true
                            if (closeSelectorMenu.value) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    viewModel.onEvent(
                                        event = TrashEvent.SelectAll(false),
                                        trashedNotes = trashedNotes
                                    )
                                    isSelected.value = false
                                    viewModel.onEvent(
                                        event = TrashEvent.ClearList(selectedItems),
                                        trashedNotes = trashedNotes
                                    )
                                    selectedItemsCount.intValue = selectedItems.size
                                    closeSelectorMenu.value = false
                                }, 1000)
                            }
                        }) {
                            if (closeSelectorMenu.value) {
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
                                navController.popBackStack()
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
                                    if (trashedNotes.isNotEmpty()){
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
                                    }else{
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                ctx.getString(R.string.empty_list)
                                            )
                                        }
                                    }
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
                                    if (trashedNotes.isNotEmpty()){
                                        isExpanded.value = false
                                        isSelected.value = true
                                        viewModel.onEvent(
                                            TrashEvent.SelectAll(true),
                                            trashedNotes
                                        )
                                    }else{
                                        coroutineScope.launch {
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                ctx.getString(R.string.empty_list)
                                            )
                                        }
                                    }
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
                                    if(trashedNotes.isNotEmpty()){
                                        isExpanded.value = false
                                        isDialogVisible.value = true
                                        functionsCase.intValue = 4
                                    }else{
                                        coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(
                                            ctx.getString(R.string.empty_list)
                                        ) }
                                    }
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
                                    if (trashedNotes.isNotEmpty()){
                                        isExpanded.value = false
                                        isDialogVisible.value = true
                                        viewModel.onEvent(
                                            event = TrashEvent.SelectAll(true),
                                            trashedNotes = trashedNotes
                                        )
                                        functionsCase.intValue = 3
                                    }else {
                                        coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(ctx.getString(R.string.empty_list)) }
                                    }
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
                AlertDialogs(
                    onDismissRequest = {
                        isSelected.value = false
                        viewModel.clearList()
                        viewModel.onEvent(TrashEvent.SelectAll(false),selectedItems)
                        selectedItemsCount.intValue = selectedItems.size
                    },
                    dismissButton = {
                        Button(
                            modifier = Modifier,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = red
                            ),
                            onClick = {
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
                                            alarmMapper = 0,
                                            requestCode = 1,
                                            stopCode = 3,
                                            alarmDate = "",
                                            alarmTime = "",
                                            isRepeat = false
                                        )
                                    )
                                    isDialogVisible.value = false
                                    isSelected.value = false
                                } else {
                                    isDialogVisible.value = false
                                    isSelected.value = false
                                }
                                viewModel.clearList()
                                viewModel.onEvent(TrashEvent.SelectAll(false),selectedItems)
                            }) {
                            Text(
                                text = if (functionsCase.intValue != 2) stringResource(
                                    R.string.cancel
                                )
                                else stringResource(R.string.restore),
                                color = Color.White,
                                fontSize = fontSize.intValue.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = green
                            ),
                            onClick = {
                                when (functionsCase.intValue) {

                                    /*This is delete selected */
                                    1 -> {
                                        viewModel.multipleDelete(selectedItems)
                                        viewModel.onEvent(TrashEvent.SelectAll(false),selectedItems)
                                        isSelected.value = false
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
                                            event = TrashEvent.RestoreSelected,
                                            trashedNotes = trashedNotes
                                        )
                                    }
                                }
                                isDialogVisible.value = false
                                isSelected.value = false
                                viewModel.clearList()
                                selectedItemsCount.intValue = selectedItems.size
                            }) {
                            Text(
                                text = when (functionsCase.intValue) {
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
                                color = Color.White,
                                fontSize = fontSize.intValue.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    },
                    title = { },
                    content = {
                        Text(
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
                            color = Color.White,
                            fontSize = fontSize.intValue.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }) {
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
                            isSelected = isSelected.value
                        )
                    }
                }
            }
        }
    }
}