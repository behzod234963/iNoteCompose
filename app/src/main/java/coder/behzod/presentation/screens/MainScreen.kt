package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import coder.behzod.R
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.data.workManager.workers.CheckDateWorker
import coder.behzod.domain.model.NotesModel
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.MainScreenGridItem
import coder.behzod.presentation.items.MainScreenRowItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.red
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS
import coder.behzod.presentation.utils.constants.KEY_VIEW_TYPE
import coder.behzod.presentation.utils.constants.notesModel
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.utils.helpers.ShareNote
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.AlertDialogs
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.MainTopAppBar
import coder.behzod.presentation.views.RevealSwipeContent
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint(
    "CoroutineCreationDuringComposition",
    "UnusedMaterialScaffoldPaddingParameter",
    "UnusedMaterial3ScaffoldPaddingParameter"
)
@Composable
fun MainScreen(
    navController: NavHostController,
    sharedPrefs: SharedPreferenceInstance,
    dataStoreInstance: DataStoreInstance,
    workManager: WorkManager,
    notificationTrigger: NotificationTrigger,
    viewModel: MainViewModel = hiltViewModel()
) {
    val themeIndex =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
    val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
    val themeColor = remember { mutableStateOf(colorTheme) }

    val selectAllStatus = dataStoreInstance.getStatus().collectAsState(initial = true)

    if (colorTheme == Color.Black) themeColor.value = Color.Black else themeColor.value =
        Color.White

    val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
    val fontColor = remember { mutableStateOf(colorFont) }

    if (colorFont == Color.White) fontColor.value = Color.White else fontColor.value = Color.Black

    val state = viewModel.state
    viewModel.getNotes(state.value.noteOrder)

    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )
    val emptyListAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.empty_list)
    )

    val isEmpty = remember { mutableStateOf(false) }

    if (state.value.notes.isEmpty()) isEmpty.value = true else isEmpty.value = false

    val ctx = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    sharedPrefs.sharedPreferences.edit().putBoolean(KEY_LIST_STATUS, isEmpty.value).apply()

    val isSelected = remember { mutableStateOf(false) }

    val selectedNotes = viewModel.selectedNotes.value
    val selectedNotesCount = remember { mutableIntStateOf(0) }

    val isClosed = remember { mutableStateOf(false) }
    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isDialogVisible = remember { mutableStateOf(false) }

    /* if view type is 0 -> List View
    *                  1 -> Grid View */
    val viewType = viewModel.viewType.value

    val dialogType = remember { mutableIntStateOf(0) }

    val functionsCase = remember { mutableIntStateOf(0) }

    val isDialogViewVisible = remember { mutableStateOf(false) }

    val activityContext = LocalContext.current as Activity

    val note = remember { mutableStateOf(notesModel) }

    val notes: ArrayList<NotesModel> = ArrayList()

    coroutineScope.launch { dataStoreInstance.mainScreenState(true) }
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
        backgroundColor = themeColor.value,
        contentColor = themeColor.value,
        topBar = {
            if (isSelected.value && state.value.notes.isNotEmpty()) {
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
                                coroutineScope.launch { dataStoreInstance.selectAllStatus(false) }
                                selectedNotes.clear()
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
                        if (state.value.notes.isNotEmpty()){
                            isSelected.value = true
                            coroutineScope.launch { dataStoreInstance.selectAllStatus(false) }
                            viewModel.onEvent(NotesEvent.SelectAll(false))
                            selectedNotes.clear()
                        }else{
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    ctx.getString(R.string.empty_list)
                                )
                            }
                        }
                    },
                    contentSelectAll = {
                        if (state.value.notes.isNotEmpty()){
                            isSelected.value = true
                            viewModel.onEvent(NotesEvent.SelectAll(true))
                            viewModel.addAllToList(state.value.notes)
                            selectedNotesCount.intValue = selectedNotes.size
                        }else{
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    ctx.getString(R.string.empty_list)
                                )
                            }
                        }
                    },
                    contentDeleteAll = {
                        if (state.value.notes.isNotEmpty()){
                            dialogType.intValue = 0
                            isDialogVisible.value = true
                            functionsCase.intValue = 4
                        }else{
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    ctx.getString(R.string.empty_list)
                                )
                            }
                            isDialogViewVisible.value = false
                        }
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
                        if (selectAllStatus.value) {
                            if (selectedNotes.isEmpty()) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        ctx.getString(R.string.nothing_is_selected)
                                    )
                                }
                                dialogType.intValue = 0
                                isDialogVisible.value = false
                                isSelected.value = true
                                functionsCase.intValue = 0
                            } else {
                                dialogType.intValue = 0
                                isDialogVisible.value = true
                                isSelected.value = false
                                functionsCase.intValue = 3
                            }
                        } else {
                            if (selectedNotes.isEmpty()) {
                                coroutineScope.launch {
                                    scaffoldState.snackbarHostState.showSnackbar(
                                        ctx.getString(R.string.nothing_is_selected)
                                    )
                                }
                                dialogType.intValue = 0
                                isDialogVisible.value = false
                                isSelected.value = true
                                functionsCase.intValue = 0
                            } else {
                                dialogType.intValue = 0
                                isDialogVisible.value = true
                                isSelected.value = false
                                functionsCase.intValue = 2
                            }
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
        if (state.value.notes.isEmpty()){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeColor.value)
            ) {
                LottieAnimation(
                    composition = emptyListAnimation.value,
                    alignment = Alignment.Center,
                    restartOnPlay = true,
                    iterations = LottieConstants.IterateForever
                )
            }
        }else{
            if (isDialogVisible.value) {
                when (dialogType.intValue) {
                    0 -> {
                        Column(
                            modifier = Modifier
                                .background(themeColor.value),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AlertDialogs(
                                dismissButton = {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = red
                                        ),
                                        onClick = {
                                            isDialogVisible.value = false
                                            isSelected.value = false
                                            viewModel.onEvent(NotesEvent.SelectAll(false))
                                            selectedNotes.clear()
                                            selectedNotesCount.intValue = selectedNotes.size
                                        }) {
                                        Text(
                                            text = stringResource(id = R.string.cancel),
                                            color = Color.White,
                                            fontSize = fontSize.intValue.sp,
                                            fontFamily = FontFamily(fontAmidoneGrotesk)
                                        )
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        modifier = Modifier
                                            .width(110.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = green
                                        ),
                                        onClick = {
                                            when (functionsCase.intValue) {
                                                /*This is delete selected */
                                                2 -> {
                                                    viewModel.saveAllToTrash(selectedNotes)
                                                    isDialogVisible.value = false
                                                    isSelected.value = false
                                                    viewModel.onEvent(NotesEvent.SelectAll(false))
                                                }

                                                3 -> {
                                                    viewModel.saveAllToTrash(selectedNotes)
                                                    isDialogVisible.value = false
                                                    isSelected.value = false
                                                    viewModel.clearList()
                                                    selectedNotesCount.intValue = selectedNotes.size
                                                }

                                                4 -> {
                                                    isDialogVisible.value = false
                                                    isSelected.value = false
                                                    viewModel.saveAllToTrash(state.value.notes)
                                                    viewModel.clearList()
                                                    selectedNotesCount.intValue = selectedNotes.size
                                                }
                                            }
                                            selectedNotesCount.intValue = selectedNotes.size
                                            isDialogVisible.value = false
                                            isSelected.value = false
                                        }) {
                                        Text(
                                            text = "Ok",
                                            color = Color.White,
                                            fontSize = fontSize.intValue.sp,
                                            fontFamily = FontFamily(fontAmidoneGrotesk)
                                        )
                                    }
                                },
                                onDismissRequest = {
                                    isDialogVisible.value = false
                                    isSelected.value = false
                                    selectedNotes.clear()
                                    selectedNotesCount.intValue = selectedNotes.size
                                },
                                title = {
                                    Text(
                                        text = notesModel.title,
                                        color = Color.White,
                                        fontSize = fontSize.intValue.plus(5).sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                content = {
                                    Text(
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
                                        color = Color.White,
                                        fontSize = fontSize.intValue.sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )

                                }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "example Icon",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .background(themeColor.value),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AlertDialogs(
                                dismissButton = {
                                    Button(
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = red
                                        ),
                                        onClick = {
                                            isDialogVisible.value = false
                                        }) {
                                        Text(
                                            text = stringResource(id = R.string.cancel),
                                            color = Color.White,
                                            fontSize = fontSize.intValue.sp,
                                            fontFamily = FontFamily(fontAmidoneGrotesk)
                                        )
                                    }
                                },
                                confirmButton = { },
                                onDismissRequest = {
                                    isDialogViewVisible.value = false
                                    isSelected.value = false
                                    isDialogVisible.value = false
                                },
                                title = {
                                    Text(
                                        text = stringResource(id = R.string.select_view),
                                        color = Color.White,
                                        fontSize = fontSize.intValue.plus(5).sp,
                                        fontFamily = FontFamily(fontAmidoneGrotesk)
                                    )
                                },
                                content = { }) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = {
                                            viewModel.onEvent(NotesEvent.ViewType(1))
                                            isDialogViewVisible.value = false
                                            isDialogVisible.value = false
                                        }
                                    ) {
                                        Icon(
                                            painterResource(
                                                id = R.drawable.ic_grid
                                            ),
                                            modifier = Modifier
                                                .size(35.dp),
                                            contentDescription = "gridView",
                                            tint = Color.White
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            viewModel.onEvent(NotesEvent.ViewType(0))
                                            isDialogViewVisible.value = false
                                            isDialogVisible.value = false
                                        }
                                    ) {
                                        Icon(
                                            painterResource(
                                                id = R.drawable.ic_list
                                            ),
                                            modifier = Modifier
                                                .size(35.dp),
                                            contentDescription = "listView",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
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
                            items(items = state.value.notes, key = { it.toString() }) { model ->

                                when (model.alarmMapper) {
                                    /* 0->Neutral */
                                    0 -> {}
                                    /* 1->Current day */
                                    1 -> {
                                        if (model.alarmStatus && !model.isFired && !model.isRepeat) {
                                            notificationTrigger.scheduleNotification(
                                                activityContext,
                                                model
                                            )
                                        }
                                    }
                                    /* 2->Other day */
                                    2 -> {
                                        if (state.value.notes.isNotEmpty() && model.alarmStatus && !model.isFired){
                                            val checkDateRequest =
                                                PeriodicWorkRequestBuilder<CheckDateWorker>(
                                                    2,
                                                    TimeUnit.HOURS
                                                )
                                                    .addTag("checkDate")
                                                    .build()
                                            workManager.enqueue(checkDateRequest)
                                        }
                                    }
                                    /* 3->Repeating */
                                    3 -> {
                                        if (model.alarmStatus && model.isRepeat) {
                                            Log.d("REPEATING ALARM TEST", "MainScreen: IS WORKING NOW !!!!!!!!")
                                            notificationTrigger.scheduleRepeatingNotification(
                                                activityContext,
                                                model
                                            )
                                        }
                                    }
                                }
                                RevealSwipeContent(
                                    item = model,
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
                                                if (selectAllStatus.value) {
                                                    viewModel.addAllToList(state.value.notes)
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
                                }
                            }
                        }
                    } else {
                        /*  Lazy Column Grid Item  */
                        sharedPrefs.sharedPreferences.edit().putInt(KEY_VIEW_TYPE, 1).apply()
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2)
                        ) {
                            items(state.value.notes, key = { notes ->
                                notes.toString()
                            }) { model ->
                                /* 0-> Neutral */
                                when (model.alarmMapper) {
                                    /* 0->Neutral */
                                    0 -> {}
                                    /* 1->Current day */
                                    1 -> {
                                        if (model.alarmStatus && !model.isFired && !model.isRepeat) {
                                            notificationTrigger.scheduleNotification(
                                                activityContext,
                                                model
                                            )
                                        }
                                    }
                                    /* 2->Other day */
                                    2 -> {
                                        if (state.value.notes.isNotEmpty() && model.alarmStatus && !model.isFired){
                                            val checkDateRequest =
                                                PeriodicWorkRequestBuilder<CheckDateWorker>(
                                                    2,
                                                    TimeUnit.HOURS
                                                )
                                                    .addTag("checkDate")
                                                    .build()
                                            workManager.enqueue(checkDateRequest)
                                        }
                                    }
                                    /* 3->Repeating */
                                    3 -> {
                                        if (model.alarmStatus && model.isRepeat) {
                                            Log.d("REPEATING ALARM TEST", "MainScreen: IS WORKING NOW !!!!!!!!")
                                            notificationTrigger.scheduleRepeatingNotification(
                                                activityContext,
                                                model
                                            )
                                        }
                                    }
                                }
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
                                            if (selectAllStatus.value) {
                                                viewModel.addAllToList(state.value.notes)
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
                            }
                        }
                    }
                }
            }

        }
    }
}