package coder.behzod.presentation.screens

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.items.TrashScreenItem
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.viewModels.TrashViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrashScreen(
    sharedPrefs: SharedPreferenceInstance,
    model: TrashModel?,
    viewModel: TrashViewModel = hiltViewModel(),
    isSelected: Boolean = false
) {

    val btnAddAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = R.raw.btn_add)
    )
    val btnCloseAnim = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_close)
    )
    val btnTrashAnimation = rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.btn_trash)
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
    val isTrashPlaying = remember { mutableStateOf(false) }
    val selectedItems: ArrayList<TrashModel> = ArrayList()
    val trashedNotes = viewModel.trashedNotes.value
    val selectedItemsStatus = remember { mutableStateOf(false) }
    val isDialogVisible = remember { mutableStateOf(false) }
    val isPlaying = remember { mutableStateOf(false) }
    val isSelectedState = remember { mutableStateOf( isSelected ) }

    Scaffold(
        containerColor = themeColor.value,
        floatingActionButton = {
            if (isDialogVisible.value) {
                FloatingActionButton(modifier = Modifier
                    .padding(end = 30.dp, bottom = 30.dp),
                    containerColor = Color.Magenta,
                    shape = CircleShape,
                    onClick = {
                        Handler(Looper.getMainLooper()).postDelayed({
                            viewModel.deleteAll(selectedItems)
                        }, 1000)
                        isPlaying.value = true
                    }) {
                    if (isPlaying.value) {
                        LottieAnimation(
                            modifier = Modifier,
                            composition = btnTrashAnimation.value,
                            iterations = LottieConstants.IterateForever
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "button add",
                            tint = fontColor.value
                        )
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            if (isSelectedState.value) {
                TopAppBar(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = fontColor.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = "${selectedItems.size} items selected",
                            color = fontColor.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back button",
                            tint = fontColor.value
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            if (selectedItemsStatus.value) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    selectedItemsStatus.value = false
                                }, 1000)
                            }
                            selectedItems.clear()
                        }) {
                            if (selectedItemsStatus.value) {
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
                TopAppBar(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = fontColor.value,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = themeColor.value
                    ),
                    title = {
                        Text(
                            text = stringResource(R.string.deleted_notes),
                            color = fontColor.value,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "back button",
                            tint = fontColor.value
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                isTrashPlaying.value = true
                                if (isTrashPlaying.value) {
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        isTrashPlaying.value = false
                                        isDialogVisible.value = true
                                    }, 1000)
                                }
                            }) {
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
                                    tint = fontColor.value,
                                    contentDescription = "icon more settings"
                                )
                            }
                        }
                    }
                )
            }
        }
    ) {
        if (isDialogVisible.value) {
            Column(
                modifier = Modifier
                    .background(Color.Gray),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AlertDialog(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.Gray)
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
                            LottieAnimation(
                                modifier = Modifier
                                    .size(35.dp),
                                composition = btnTrashAnimation.value,
                                iterations = LottieConstants.IterateForever
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.delete_selected_notes),
                                color = fontColor.value,
                                fontSize = 23.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    },
                    onDismissRequest = {
                        isDialogVisible.value = false
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = fontColor.value
                                ),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    isDialogVisible.value = false
                                }
                            ) {
                                Text(
                                    text = stringResource(R.string.cancel),
                                    color = themeColor.value,
                                    fontSize = 18.sp
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(120.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = fontColor.value
                                ),
                                shape = RoundedCornerShape(10.dp),
                                onClick = {
                                    viewModel.deleteAll(selectedItems)
                                }
                            ) {
                                Text(
                                    text = "Ok",
                                    color = themeColor.value,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                )
            }
        }else{
            Column (
                verticalArrangement = Arrangement.SpaceAround
            ){
                LazyColumn {
                    if(trashedNotes != null){
                        Log.d("fix", "TrashScreen: ${trashedNotes.size}")
                        items(trashedNotes) {
                            Log.d("fix", "TrashScreen: ${trashedNotes.size}")
                            TrashScreenItem(
                                model = it,
                                fontColor = fontColor.value
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTrashScreen() {
    TrashScreen(
        sharedPrefs = SharedPreferenceInstance(LocalContext.current),
        model = TrashModel(1, "", "", 1, 30)
    )
}