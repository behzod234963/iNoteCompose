package coder.behzod.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreens(
    navController: NavController,
    sharedPrefs: SharedPreferenceInstance
) {

    val notesAnimComposition = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.notes)
    )

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

    val isVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Boolean) {
        delay(1500L)
        isVisible.value = true
    }
    val isEmpty = remember {
        mutableStateOf(sharedPrefs.sharedPreferences.getBoolean(KEY_LIST_STATUS, true))
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(themeColor.value),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = isVisible.value,
            enter = fadeIn(),
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                text = stringResource(id = R.string.app_name),
                color = fontColor.value,
                fontFamily = FontFamily(fontAmidoneGrotesk),
                fontSize = 32.sp
            )
        }
        LottieAnimation(
            modifier = Modifier
                .size(250.dp),
            composition = notesAnimComposition.value,
            isPlaying = true,
        )
    }
    LaunchedEffect(key1 = Unit) {
        delay(2500)
        if (isEmpty.value) {
            navController.navigate(ScreensRouter.EmptyMainScreenRoute.route)
        } else {
            navController.navigate(ScreensRouter.MainScreenRoute.route)
        }
    }
}