package coder.behzod.presentation.views

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.events.PassDataEvents
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainTopAppBar(
    navController: NavController,
    backgroundColor: Color,
    fontColor: Color,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange: (NoteOrder) -> Unit
) {
    val isOpened = remember { mutableStateOf(false) }
    val btnTrashAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_trash)
    )
    val btnSettingsAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.settings_black)
    )
    val isSettingsPlaying = remember { mutableStateOf(false) }
    val isTrashPlaying = remember { mutableStateOf(false) }
    val isExpanded = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isOpened.value) 145.dp else 60.dp)
            .background(backgroundColor)
            .border(color = fontColor, width = 1.dp, shape = RoundedCornerShape(10.dp))
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            AnimatedDropDownMenu(
                backgroundColor = backgroundColor,
                fontColor = fontColor
            ) { event ->
                when (event) {
                    is PassDataEvents.PassStatus -> {
                        isOpened.value = event.status
                    }
                    is PassDataEvents.PassDeleteState -> TODO()
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButtonSkeleton(
                                    selected = noteOrder is NoteOrder.Title,
                                    onSelect = { onOrderChange(NoteOrder.Title(noteOrder.orderType)) },
                                    themeColor = backgroundColor,
                                    fontColors = fontColor
                                )
                                Text(
                                    text = stringResource(R.string.title),
                                    fontSize = 18.sp,
                                    color = fontColor,
                                    fontFamily = FontFamily(fontAmidoneGrotesk)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButtonSkeleton(
                                    selected = noteOrder is NoteOrder.Date,
                                    onSelect = { onOrderChange(NoteOrder.Date(noteOrder.orderType)) },
                                    themeColor = backgroundColor,
                                    fontColors = fontColor
                                )
                                Text(
                                    text = stringResource(R.string.date),
                                    fontSize = 18.sp,
                                    color = fontColor,
                                    fontFamily = FontFamily(fontAmidoneGrotesk)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButtonSkeleton(
                                    selected = noteOrder is NoteOrder.Color,
                                    onSelect = { onOrderChange(NoteOrder.Color(noteOrder.orderType)) },
                                    themeColor = backgroundColor,
                                    fontColors = fontColor
                                )
                                Text(
                                    text = stringResource(R.string.color),
                                    fontSize = 18.sp,
                                    color = fontColor,
                                    fontFamily = FontFamily(fontAmidoneGrotesk)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButtonSkeleton(
                                    selected = noteOrder.orderType is OrderType.Ascending,
                                    onSelect = { onOrderChange(noteOrder.copy(OrderType.Ascending)) },
                                    themeColor = backgroundColor,
                                    fontColors = fontColor
                                )
                                Text(
                                    text = stringResource(R.string.ascending),
                                    fontSize = 18.sp,
                                    color = fontColor,
                                    fontFamily = FontFamily(fontAmidoneGrotesk)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButtonSkeleton(
                                    selected = noteOrder.orderType is OrderType.Descending,
                                    onSelect = { onOrderChange(noteOrder.copy(OrderType.Descending)) },
                                    themeColor = backgroundColor,
                                    fontColors = fontColor
                                )
                                Text(
                                    text = stringResource(R.string.descending),
                                    fontSize = 18.sp,
                                    color = fontColor,
                                    fontFamily = FontFamily(fontAmidoneGrotesk)
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                color = fontColor,
                fontSize = 25.sp,
                fontWeight = FontWeight(600),
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 5.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    onClick = {
                        isExpanded.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "more functions",
                        tint = fontColor
                    )
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    DropdownMenu(
                        modifier = Modifier
                            .width(150.dp)
                            .align(Alignment.End),
                        expanded = isExpanded.value,
                        onDismissRequest = { isExpanded.value = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        isTrashPlaying.value = true
                                        if (isTrashPlaying.value) {
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                                                isTrashPlaying.value = false
                                            }, 1000)
                                        }
                                    }
                                ) {
                                    if (isTrashPlaying.value) {
                                        LottieAnimation(
                                            modifier = Modifier
                                                .size(35.dp),
                                            composition = btnTrashAnimation.value,
                                            iterations = LottieConstants.IterateForever
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            tint = Color.Black,
                                            contentDescription = "icon more settings"
                                        )
                                    }
                                }
                            },
                            text = {
                                Text(text = stringResource(R.string.draft))
                            },
                            onClick = {
                                isTrashPlaying.value = true
                                if (isTrashPlaying.value) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                                        isTrashPlaying.value = false
                                    }, 1000)
                                }
                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(text = stringResource(R.string.draft))
                            },
                            onClick = {
                                isSettingsPlaying.value = true
                                if (isSettingsPlaying.value) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                                        isSettingsPlaying.value = false
                                    }, 1500)
                                }
                            },
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        isSettingsPlaying.value = true
                                        if (isSettingsPlaying.value) {
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                                                isSettingsPlaying.value = false
                                            }, 1500)
                                        }
                                    }
                                ) {
                                    if (isSettingsPlaying.value) {
                                        LottieAnimation(
                                            modifier = Modifier
                                                .size(35.dp),
                                            composition = btnSettingsAnimation.value,
                                            iterations = LottieConstants.IterateForever
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            tint = Color.Black,
                                            contentDescription = "icon more settings"
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
//IconButton(
//                onClick = {
//
//                }) {
//
//            }