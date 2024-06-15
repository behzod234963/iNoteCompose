package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.utils.constants.KEY_FONT_SIZE
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.views.BottomNavigationView
import coder.behzod.presentation.views.MainTopAppBar
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun EmptyMainScreen(
    navController: NavHostController,
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

    val fontSize = remember { mutableIntStateOf( sharedPrefs.sharedPreferences.getInt(KEY_FONT_SIZE,18) ) }

    Scaffold (
        modifier = Modifier,
        bottomBar = {
          BottomNavigationView(
              themeColor = themeColor.value,
              fontColor = fontColor.value,
              navController = navController
          )
        },
        topBar = {
            MainTopAppBar(
                backgroundColor = themeColor.value,
                fontColor = fontColor.value,
                fontSize = fontSize.intValue,
                contentView = {},
                contentSelect = {},
                contentSelectAll = {},
                contentDeleteAll = {},
            ) {}
        },
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = Color.Magenta,
                onClick = {
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(route = ScreensRouter.NewNoteScreenRoute.route + "/-1")
                    },900)
                    isPlaying.value = true
                }
            ) {
                if (isPlaying.value){
                    LottieAnimation(
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
    ){
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
    }
}