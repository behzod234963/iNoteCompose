package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import coder.behzod.presentation.alarmManager.schedulers.AlarmScheduler
import coder.behzod.presentation.items.MainScreenGridItem
import coder.behzod.presentation.items.MainScreenRowItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_ALARM_STATUS
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS
import coder.behzod.presentation.utils.constants.KEY_VIEW_TYPE
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.utils.extensions.dateFormatter
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.AlertDialogInstance
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.DialogViewInstance
import coder.behzod.presentation.views.MainTopAppBar
import coder.behzod.presentation.views.RevealSwipeContent
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    sharedPrefs: SharedPreferenceInstance,
    viewModel: MainViewModel = hiltViewModel()
) {

    val themeIndex = remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    if (colorTheme == Color.Black)  themeColor.value = Color.Black else  themeColor.value = Color.White

    val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
    val fontColor = remember { mutableStateOf(colorFont) }

    if (colorFont == Color.White) fontColor.value = Color.White else fontColor.value = Color.Black

    val state = viewModel.state
    viewModel.getNotes(state.value.noteOrder)

    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )

    val isEmpty = remember { mutableStateOf(false) }

    if (state.value.notes.isEmpty()) isEmpty.value = true else isEmpty.value = false

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    sharedPrefs.sharedPreferences.edit().putBoolean(KEY_LIST_STATUS, isEmpty.value).apply()

    val isSelected = remember { mutableStateOf(false) }

    val selectedNotes = viewModel.selectedNotes.value
    val selectAllStatus = viewModel.selectAllStatus.value
    val selectedNotesCount = remember { mutableIntStateOf(0) }

    val isClosed = remember { mutableStateOf(false) }
    val fontSize = remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isDialogVisible = remember { mutableStateOf(false) }

    /* if view type is 0 -> List View
    *                  1 -> Grid View */
    val viewType = viewModel.viewType.value

    val dialogType = remember { mutableIntStateOf(0) }

    val functionsCase = remember { mutableIntStateOf(0) }

    val isDialogViewVisible = remember { mutableStateOf(false) }

    val note = remember {
        mutableStateOf(
            NotesModel(
                title = "",
                content = "",
                color = -1,
                dataAdded = LocalDate.now().toString().dateFormatter(),
                requestCode = 1,
                notificationCode = 2,
                stopCode = 3,
            )
        )
    }

    val activityContext = LocalContext.current as Activity

    val dataStore = DataStoreInstance(activityContext)

    Scaffold(
        modifier = Modifier
            .background(themeColor.value),
        bottomBar = {
            BottomNavigationView(
                themeColor = themeColor.value,
                fontColor = fontColor.value,
                navController = navController
            )
        },
        topBar = {
            if (isSelected.value) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = if (selectedNotesCount.intValue == 0) "0 ${stringResource(id = R.string.items_selected)}"
                            else "${selectedNotesCount.intValue} ${stringResource(id = R.string.items_selected)}",
                            color = fontColor.value,
                            fontSize = fontSize.intValue.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    actions = {

                        /* Button close */
                        IconButton(onClick = {

                            isClosed.value = true
                            Handler(Looper.getMainLooper()).postDelayed({
                                isClosed.value = false
                                isSelected.value = false
                                viewModel.removeAllFromList()
                                selectedNotesCount.intValue = selectedNotes.size
                            }, 1000)

                        }) {
                            if (isClosed.value) {
                                LottieAnimation(
                                    composition = btnCloseAnim.value,
                                    modifier = Modifier
                                        .size(35.dp),
                                    iterations = LottieConstants.IterateForever
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "btn delete all items",
                                    tint = fontColor.value
                                )
                            }
                        }
                    }
                )
            } else {
                MainTopAppBar(
                    backgroundColor = themeColor.value,
                    fontColor = fontColor.value,
                    fontSize = fontSize.intValue,
                    contentView = {
                        isDialogVisible.value = true
                        dialogType.intValue = 1
                        isDialogViewVisible.value = true
                    },
                    contentSelect = {
                        isSelected.value = true
                    },
                    contentSelectAll = {
                        isSelected.value = true
                        viewModel.onEvent(NotesEvent.SelectAllStatus(true))
                        viewModel.addAllToList()
                        selectedNotesCount.intValue = selectedNotes.size
                    },
                    contentDeleteAll = {
                        dialogType.intValue = 0
                        isDialogVisible.value = true
                        functionsCase.intValue = 4
                    },
                    noteOrder = state.value.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
        },
        floatingActionButton = {
            if (isSelected.value) {

                /* Floating action button for functions */
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = 30.dp),
                    containerColor = fontColor.value,
                    shape = CircleShape,
                    onClick = {
                        if (selectAllStatus) {
                            dialogType.intValue = 0
                            isDialogVisible.value = true
                            functionsCase.intValue = 3
                            isSelected.value = false
                        } else {
                            dialogType.intValue = 0
                            isDialogVisible.value = true
                            isSelected.value = false
                            functionsCase.intValue = 2
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "btn delete",
                        tint = themeColor.value
                    )
                }

            } else {
                /* Floating Action Button add note */
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = 30.dp),
                    containerColor = fontColor.value,
                    shape = CircleShape,
                    onClick = {
                        navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/-1")
                    }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        modifier = Modifier
                            .size(30.dp),
                        contentDescription = "btnAdd",
                        tint = themeColor.value
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        if (isDialogVisible.value) {
            when (dialogType.intValue) {
                0 -> {
                    Column(
                        modifier = Modifier
                            .background(themeColor.value),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AlertDialogInstance(
                            fontSize = fontSize.intValue,
                            icon = Icons.Default.Delete,
                            title = null,
                            text = when (functionsCase.intValue) {
                                2 -> {
                                    stringResource(R.string.delete_selected_notes)
                                }

                                3 -> {
                                    stringResource(id = R.string.delete_all_notes)
                                }

                                4 -> {
                                    stringResource(id = R.string.delete_all_notes)
                                }

                                else -> {
                                    ""
                                }
                            },
                            confirmButtonText = "Ok",
                            confirmButton = {

                                when (functionsCase.intValue) {
                                    /*This is delete selected */
                                    2 -> {
                                        isDialogVisible.value = false
                                        isSelected.value = false
                                        viewModel.saveAllToTrash(selectedNotes)
                                        viewModel.multipleDelete(selectedNotes)
                                    }

                                    3 -> {
                                        isDialogVisible.value = false
                                        isSelected.value = false
                                        viewModel.saveAllToTrash(selectedNotes)
                                        viewModel.multipleDelete(selectedNotes)
                                    }

                                    4 -> {
                                        isDialogVisible.value = false
                                        isSelected.value = false
                                        viewModel.saveAllToTrash(state.value.notes)
                                    }
                                }
                                selectedNotesCount.intValue = selectedNotes.size
                                isDialogVisible.value = false
                                isSelected.value = false
                            },
                            dismissButtonText = stringResource(id = R.string.cancel),
                            showDialog = isDialogVisible.value,
                            onDismissRequest = {
                                isDialogVisible.value = false
                                isSelected.value = false
                            },
                            dismissButton = {
                                isDialogVisible.value = false
                                isSelected.value = false
                            }
                        )
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .background(themeColor.value),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DialogViewInstance(
                            fontSize = fontSize.intValue,
                            dismissRequest = {
                                isDialogViewVisible.value = false
                                isSelected.value = false
                                isDialogVisible.value = false
                            },
                            isDialogVisible = isDialogViewVisible.value,
                            viewIndex = {
                                when (it) {
                                    0 -> {
                                        viewModel.onEvent(NotesEvent.ViewType(0))
                                        isDialogViewVisible.value = false
                                        isDialogVisible.value = false
                                    }

                                    1 -> {
                                        viewModel.onEvent(NotesEvent.ViewType(1))
                                        isDialogViewVisible.value = false
                                        isDialogVisible.value = false
                                    }
                                }
                            }
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeColor.value)
            ) {
                if (viewType == 0) {

                    /* Lazy Column List RowItem */
                    sharedPrefs.sharedPreferences.edit().putInt(KEY_VIEW_TYPE, 0).apply()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 75.dp)
                    ) {
                        items(items = state.value.notes, key = { it.toString() }) { notes ->
                            note.value = notes
                            RevealSwipeContent(
                                item = notes,
                                onShare = { shareItem ->
                                    ShareNote().execute(
                                        title = shareItem.title,
                                        content = shareItem.content,
                                        ctx = activityContext
                                    ) {}
                                },
                                onDelete = { deleteItem ->

                                    viewModel.saveToTrash(
                                        TrashModel(
                                            title = deleteItem.title,
                                            content = deleteItem.content,
                                            color = deleteItem.color,
                                            daysLeft = 30,
                                        )
                                    )
                                    coroutineScope.launch(Dispatchers.IO) {
                                        delay(100)
                                        viewModel.onEvent(NotesEvent.DeleteNote(deleteItem))
                                    }
                                    coroutineScope.launch {
                                        val snackbarResult =
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = activityContext.getString(R.string.note_deleted),
                                                actionLabel = activityContext.getString(R.string.undo)
                                            )
                                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                                            viewModel.returnDeletedNote(deleteItem)
                                        }
                                    }
                                    note.value = deleteItem
                                },
                            ) { item ->

                                note.value = item
                                MainScreenRowItem(
                                    notesModel = item,
                                    themeColor = themeColor.value,
                                    fontColor = fontColor.value,
                                    fontSize = fontSize.intValue,
                                    isSelected = isSelected.value,
                                    onCheckedChange = {
                                        if (it == 1) {
                                            if (selectAllStatus) {
                                                viewModel.addAllToList()
                                                selectedNotesCount.intValue = selectedNotes.size
                                            } else {
                                                viewModel.addNoteToList(item)
                                                selectedNotesCount.intValue = selectedNotes.size
                                            }
                                        } else {
                                            viewModel.removeFromList(item)
                                            selectedNotesCount.intValue = selectedNotes.size
                                        }
                                    },
                                    onClick = {
                                        navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/${item.id}")
                                    }
                                )
                                AlarmScheduler(activityContext,note.value,sharedPrefs,viewModel).execute()
                            }
                        }
                    }
                } else {

                    /*  Lazy Column Grid Item  */
                    sharedPrefs.sharedPreferences.edit().putInt(KEY_VIEW_TYPE, 1).apply()

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2)) {
                        items(state.value.notes, key = { notes ->
                            notes.toString()
                        }) { model ->

                            note.value = model
                            MainScreenGridItem(
                                themeColor = themeColor.value,
                                fontColor = fontColor.value,
                                fontSize = fontSize.intValue,
                                onClick = {
                                    navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/${model.id}")
                                },
                                onShare = {
                                    ShareNote().execute(
                                        title = model.title,
                                        content = model.content,
                                        ctx = activityContext
                                    ) {}
                                },
                                onChange = {
                                    if (it == 1) {
                                        if (selectAllStatus) {
                                            viewModel.addAllToList()
                                            selectedNotesCount.intValue = selectedNotes.size
                                        } else {
                                            viewModel.addNoteToList(model)
                                            selectedNotesCount.intValue = selectedNotes.size
                                        }
                                    } else {
                                        viewModel.removeFromList(model)
                                        selectedNotesCount.intValue = selectedNotes.size
                                    }
                                },
                                onDelete = {
                                    viewModel.saveToTrash(
                                        TrashModel(
                                            id = model.id,
                                            title = model.title,
                                            content = model.content,
                                            color = model.color,
                                            daysLeft = 30
                                        )
                                    )
                                    CoroutineScope(Dispatchers.Default).launch {
                                        dataStore.saveStatus(KEY_ALARM_STATUS,false)
                                    }
                                    viewModel.onEvent(NotesEvent.DeleteNote(model))
                                    coroutineScope.launch {

                                        val snackbarResult =
                                            scaffoldState.snackbarHostState.showSnackbar(
                                                message = activityContext.getString(R.string.note_deleted),
                                                actionLabel = activityContext.getString(R.string.undo)
                                            )
                                        if (snackbarResult == SnackbarResult.ActionPerformed) {
                                            viewModel.returnDeletedNote(model)
                                        }
                                    }
                                },
                                isSelected = isSelected.value,
                                notesModel = model,
                                title = model.title,
                                note = model.content,
                                date = model.dataAdded,
                                backgroundColor = model.color
                            )
                            AlarmScheduler(activityContext,note.value,sharedPrefs,viewModel).execute()
                        }
                    }
                }
            }
        }
    }
}