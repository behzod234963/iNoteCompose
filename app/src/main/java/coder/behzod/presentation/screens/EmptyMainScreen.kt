package coder.behzod.presentation.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.views.MainTopAppBar
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun EmptyMainScreen(
    navController: NavController,
    sharedPrefs: SharedPreferenceInstance
) {
    val emptyListAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.empty_list)
    )
    val btnAddAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.btn_add)
    )
    val isPlaying = remember { mutableStateOf( false ) }
    val themeIndex = remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX,0)) }
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
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(themeColor.value),
        ) {
            MainTopAppBar(
                navController = navController,
                onOrderChange = {},
                backgroundColor = themeColor.value,
                fontColor = fontColor.value
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            LottieAnimation(
                composition = emptyListAnimation.value,
                alignment = Alignment.Center,
                iterations = LottieConstants.IterateForever
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 30.dp, end = 30.dp),
                shape = CircleShape,
                containerColor = Color.Magenta,
                onClick = {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(ScreensRouter.NewNoteScreenRoute.route)
                    },900)
                    isPlaying.value = true
                }
            ) {
                if (isPlaying.value){
                    LottieAnimation(
                        modifier = Modifier
                            .matchParentSize(),
                        composition = btnAddAnimation.value,
                        iterations = LottieConstants.IterateForever
                    )
                }else {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "button add",
                        tint = fontColor.value
                    )
                }
            }
        }
    }
}