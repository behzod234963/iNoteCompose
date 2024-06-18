package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.MainScreenGridItem
import coder.behzod.presentation.items.MainScreenItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.MainTopAppBar
import coder.behzod.presentation.views.SpeedDialFAB
import coder.behzod.presentation.views.SwipeToDeleteContainer
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    sharedPrefs: SharedPreferenceInstance,
    viewModel: MainViewModel = hiltViewModel()
) {

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

    val state = viewModel.state
    viewModel.getNotes(state.value.noteOrder)

    val btnAddAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.btn_add)
    )
    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )

    val isPlaying = remember { mutableStateOf(false) }
    val isEmpty = remember { mutableStateOf(false) }

    if (state.value.notes.isEmpty()) {
        isEmpty.value = true
    } else {
        isEmpty.value = false
    }

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    sharedPrefs.sharedPreferences.edit().putBoolean(KEY_LIST_STATUS, isEmpty.value).apply()

    val isSelected = remember { mutableStateOf(false) }

    val selectedNotes = viewModel.selectedNotes.value
    val selectAllStatus = viewModel.selectAllStatus.value
    val selectedNotesCount = remember { mutableIntStateOf(0) }

    val isClosed = remember { mutableStateOf(false) }
    val fontSize =
        remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE, 18)) }

    val isDialogVisible = remember { mutableStateOf(false) }

    /* if view type is 0 -> List View
    *                  1 -> Grid View */
    val viewType = remember { mutableIntStateOf(1) }

    val dialogType = remember { mutableIntStateOf(0) }

    val functionsCase = remember { mutableIntStateOf(0) }

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
                            }, 1000)
                            viewModel.removeAllFromList()
                            selectedNotesCount.intValue = selectedNotes.size
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
                SpeedDialFAB(
                    modifier = Modifier,
                    labelFirst = stringResource(id = R.string.share),
                    labelSecond = stringResource(id = R.string.delete),
                    painterFirst = R.drawable.ic_share,
                    painterSecond = R.drawable.ic_delete,
                    onClickFirst = {

                        /* Content for share  */
                        TODO()
                    }) {
                    /* Content for functions */
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
                }
            } else {
                /* Floating Action Button add note */
                FloatingActionButton(
                    modifier = Modifier
                        .padding(end = 30.dp, bottom = 30.dp),
                    containerColor = Color.Magenta,
                    shape = CircleShape,
                    onClick = {
                        Handler(Looper.getMainLooper()).postDelayed({
                            navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/-1")
                        }, 900)
                        isPlaying.value = true
                    }) {
                    if (isPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier,
                            composition = btnAddAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "button add",
                            tint = fontColor.value
                        )
                    }
                }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isDialogVisible.value) {
                when (dialogType.intValue) {
                    0 -> {
                        Column(
                            modifier = Modifier,
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AlertDialog(
                                modifier = Modifier
                                    .height(200.dp)
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
                                            color = fontColor.value,
                                            fontSize = fontSize.intValue.plus(7).sp,
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

                                        /* This is dismiss button in alert dialog */
                                        Button(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .padding(end = 7.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = fontColor.value
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
                                                isDialogVisible.value = false
                                                isSelected.value = false
                                            }
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.cancel),
                                                color = themeColor.value,
                                                fontSize = fontSize.intValue.sp
                                            )
                                        }

                                        /* This is confirm button in alert dialog */
                                        Button(
                                            modifier = Modifier
                                                .height(40.dp)
                                                .padding(start = 7.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = fontColor.value
                                            ),
                                            shape = RoundedCornerShape(10.dp),
                                            onClick = {
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
                                            }
                                        ) {
                                            Text(
                                                text = when (functionsCase.intValue) {
                                                    2 -> {
                                                        stringResource(id = R.string.delete)
                                                    }

                                                    3 -> {
                                                        stringResource(id = R.string.delete)
                                                    }

                                                    4 -> {
                                                        stringResource(id = R.string.delete)
                                                    }

                                                    else -> {
                                                        ""
                                                    }
                                                },
                                                color = themeColor.value,
                                                fontSize = fontSize.intValue.sp
                                            )
                                        }
                                    }
                                }
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Gray),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AlertDialog(
                                modifier = Modifier
                                    .height(200.dp)
                                    .background(Color.Gray)
                                    .border(
                                        color = fontColor.value,
                                        width = 1.dp,
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                shape = RoundedCornerShape(10.dp),
                                backgroundColor = themeColor.value,
                                text = {
                                    Text(text = stringResource(R.string.select_view))
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

                                        /* This is grid button in alert dialog */
                                        IconButton(onClick = {
                                            viewType.intValue = 1
                                            isDialogVisible.value = false
                                        }) {
                                            Icon(
                                                modifier = Modifier
                                                    .size(35.dp),
                                                painter = painterResource(id = R.drawable.ic_grid),
                                                contentDescription = "btn grid view",
                                                tint = fontColor.value
                                            )
                                        }

                                        /* This is list button in alert dialog */
                                        IconButton(
                                            onClick = {
                                                viewType.intValue = 0
                                                isDialogVisible.value = false
                                            }) {
                                            Icon(
                                                modifier = Modifier.size(35.dp),
                                                painter = painterResource(id = R.drawable.ic_list),
                                                contentDescription = "btn list",
                                                tint = fontColor.value
                                            )
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
                        .padding(10.dp)
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    if (viewType.intValue == 0) {
                        /* Lazy Column List Item */
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            items(items = state.value.notes, key = { it.toString() }) { notes ->
                                SwipeToDeleteContainer(item = notes,
                                    onDelete = {
                                        viewModel.saveToTrash(
                                            TrashModel(
                                                title = it.title,
                                                content = it.content,
                                                color = it.color,
                                                daysLeft = 30,
                                            )
                                        )
                                        coroutineScope.launch(Dispatchers.IO) {
                                            delay(1000)
                                            viewModel.onEvent(NotesEvent.DeleteNote(it))
                                        }
                                    }) { item ->
                                    MainScreenItem(
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
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }
                    } else {
                        /*  Lazy Column Grid Item  */
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(state.value.notes, key = { notes ->
                                notes.toString()
                            }) { model ->
                                MainScreenGridItem(
                                    themeColor = themeColor.value,
                                    fontColor = fontColor.value,
                                    fontSize = fontSize.intValue,
                                    isSelected = isSelected.value,
                                    onClick = {
                                        navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/${model.id}")
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
                                        viewModel.onEvent(NotesEvent.DeleteNote(model))
                                    },
                                    title = model.title,
                                    note = model.content,
                                    date = model.dataAdded,
                                    notesModel = model,
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