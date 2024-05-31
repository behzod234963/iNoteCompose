package coder.behzod.presentation.views

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.presentation.navigation.ScreensRouter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun BottomNavigationView(
    themeColor: Color,
    fontColor: Color,
    navController: NavController
) {

    val interactionSource = remember { MutableInteractionSource() }

    val isNotesAnimPlaying = remember { mutableStateOf(false) }
    val isTrashAnimPlaying = remember { mutableStateOf(false) }
    val isSettingsAnimPlaying = remember { mutableStateOf(false) }

    val isNotesSelected = remember { mutableStateOf(false) }
    val isTrashSelected = remember { mutableStateOf(false) }
    val isSettingsSelected = remember { mutableStateOf(false) }

    val btnNotesAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_notes)
    )
    val btnTrashAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_trash)
    )
    val btnSettingsAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.settings_black)
    )

    BottomNavigation(
        modifier = Modifier
            .border(width = 1.dp, shape = RoundedCornerShape(10.dp), color = fontColor),
        backgroundColor = themeColor,
        contentColor = fontColor,
        elevation = 50.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavigationItem(
                selected = isNotesSelected.value,
                onClick = {
                    isNotesSelected.value = true

                    isTrashSelected.value = false
                    isSettingsSelected.value = false

                    isNotesAnimPlaying.value = true
                    isTrashAnimPlaying.value = false
                    isSettingsAnimPlaying.value = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        isNotesAnimPlaying.value = false
                        navController.navigate(ScreensRouter.MainScreenRoute.route)
                    }, 500)
                },
                icon = {
                    if (isNotesAnimPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(40.dp),
                            composition = btnNotesAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(40.dp),
                            painter = painterResource(id = R.drawable.ic_notes),
                            contentDescription = "all notes",
                            tint = fontColor
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.all_notes)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )
            BottomNavigationItem(
                selected = isTrashSelected.value,
                onClick = {

                    isTrashSelected.value = true
                    isSettingsSelected.value = false
                    isNotesSelected.value = false

                    isTrashAnimPlaying.value = true
                    isNotesAnimPlaying.value = false
                    isSettingsAnimPlaying.value = false

                    Handler(Looper.getMainLooper()).postDelayed({
                        isTrashAnimPlaying.value = false
                        navController.navigate(route = ScreensRouter.TrashScreen.route)
                    }, 1000)
                },
                icon = {
                    if (isTrashAnimPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(40.dp),
                            composition = btnTrashAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(40.dp),
                            imageVector = Icons.Default.Delete,
                            contentDescription = "trashed notes",
                            tint = fontColor
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.draft)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )
            BottomNavigationItem(
                selected = isSettingsSelected.value,
                onClick = {
                    isSettingsAnimPlaying.value = true
                    isNotesSelected.value = false
                    isTrashSelected.value = false

                    isNotesAnimPlaying.value = false
                    isTrashAnimPlaying.value = false
                    isSettingsAnimPlaying.value = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        isSettingsAnimPlaying.value = true
                        navController.navigate(route = ScreensRouter.SettingsScreenRoute.route)
                    }, 1000)
                },
                icon = {
                    if (isSettingsAnimPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier
                                .size(40.dp),
                            composition = btnSettingsAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            modifier = Modifier
                                .size(40.dp),
                            imageVector = Icons.Default.Settings,
                            contentDescription = "all notes",
                            tint = fontColor
                        )
                    }
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.settings)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomNav(modifier: Modifier = Modifier) {
    BottomNavigationView(
        themeColor = Color.Black,
        fontColor = Color.White,
        navController = NavController(LocalContext.current)
    )
}