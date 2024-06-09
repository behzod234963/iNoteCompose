package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.MainScreenItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS
import coder.behzod.presentation.utils.constants.deletedNotes
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.MainTopAppBar
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

    Scaffold(
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
                            text = "${selectedNotesCount.intValue} ${stringResource(id = R.string.items_selected)}",
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
                    contentSelect = {
                        isSelected.value = true
                    },
                    contentSelectAll = {
                        isSelected.value = true
                        viewModel.onEvent(NotesEvent.SelectAllStatus(true))
                    },
                    contentDeleteAll = {
                        viewModel.deleteAllUseCase(selectedNotes)
                    },
                    noteOrder = state.value.noteOrder,
                    onOrderChange = {
                        viewModel.onEvent(NotesEvent.Order(it))
                    }
                )
            }
        },
        floatingActionButton = {

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
        },
        scaffoldState = scaffoldState
    ) {it.calculateTopPadding()
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(themeColor.value)
                    .padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(5.dp))
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
                                    content = it.note,
                                    color = it.color,
                                    daysLeft = 30,
                                )
                            )
                            coroutineScope.launch(Dispatchers.IO) {
                                delay(1000)
                                deletedNotes.add(it)
                                viewModel.onEvent(NotesEvent.DeleteNote(it))
                            }
                        }) { item ->
                            MainScreenItem(
                                notesModel = item,
                                fontColor = fontColor.value,
                                isSelected = isSelected.value,
                                onClick = {
                                    navController.navigate(ScreensRouter.NewNoteScreenRoute.route + "/${item.id}")
                                },
                                fontSize = fontSize.intValue,
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
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}