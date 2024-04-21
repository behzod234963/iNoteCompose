package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.items.MainScreenItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.helpers.NotesEvent
import coder.behzod.presentation.viewModels.MainViewModel
import coder.behzod.presentation.views.MainTopAppBar
import coder.behzod.presentation.views.SwipeToDeleteContainer
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainScreen(
    navController: NavController,
    notesModel: NotesModel?,
    sharedPrefs: SharedPreferenceInstance,
    viewModel:MainViewModel = hiltViewModel()
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
    val isPlaying = remember { mutableStateOf( false ) }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themeColor.value)
                .border(width = 1.dp, color = fontColor.value)
        ) {
            MainTopAppBar(
                navController = navController,
                onOrderChange = {
                    viewModel.onEvent(NotesEvent.Order(it))
                },
                backgroundColor = themeColor.value,
                fontColor = fontColor.value
            )
            Spacer(modifier = Modifier.height(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(state.value.notes){
                    MainScreenItem(
                        notesModel = it,
                        fontColor = fontColor.value
                    ) {

                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 30.dp),
            containerColor = Color.Magenta,
            shape = CircleShape,
            onClick = {
                Handler(Looper.getMainLooper()).postDelayed({
                    navController.navigate(ScreensRouter.NewNoteScreenRoute.route)
                },900)
                isPlaying.value = true
            }
        ) {
            if (isPlaying.value) {
                LottieAnimation(
                    modifier = Modifier
                        .matchParentSize(),
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
}