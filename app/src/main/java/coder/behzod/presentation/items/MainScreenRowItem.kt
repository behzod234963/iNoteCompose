package coder.behzod.presentation.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.notes
import coder.behzod.presentation.viewModels.MainViewModel

@Composable
fun MainScreenRowItem(
    notesModel: NotesModel,
    themeColor: Color,
    fontColor: Color,
    fontSize: Int,
    isSelected: Boolean,
    onCheckedChange: (Int) -> Unit,
    onClick: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {

    val selectAllStatus = viewModel.selectAllStatus.value
    val isItemSelected = remember { mutableStateOf(false) }
    val isAllItemsSelected = remember { mutableStateOf(true) }
    val showAlarmContent = remember { mutableStateOf( false ) }

    val colorFont = remember {
        mutableStateOf(
            when (notesModel.color) {
                -16777216 -> {
                    Color.White
                }

                -1 -> {
//                Color for title,when title color is White
                    Color.Black
                }

                -65536 -> {
//                Color for note and data,when that's color was red
                    Color.White
                }

                -65281 -> {
//                 Color for note and data,when that's color was pink
                    Color.White
                }

                -256 -> {
//                Color for title,when that's color was yellow
                    Color.Black
                }

                else -> {
                    fontColor
                }
            }
        )
    }

    Card(
        onClick = {
            onClick.invoke()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (themeColor == Color.Black) Color.White else Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(5.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(notesModel.color))
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes title */
                            Text(
                                modifier = Modifier
                                    .width(200.dp),
                                text = notesModel.title,
                                color = colorFont.value,
                                maxLines = 1,
                                fontSize = fontSize.plus(7).sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                            /* This is notes data added */
                            Text(
                                modifier = Modifier
                                    .padding(top = 5.dp),
                                text = notesModel.dataAdded,
                                color = colorFont.value,
                                fontSize = 20.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                            if (notesModel.alarmStatus) {
                                IconButton(onClick = {
                                    showAlarmContent.value = true
                                }) {
                                    if (showAlarmContent.value){
                                        AlertDialog(
                                            title = {
                                                Text(
                                                    text = "Alarm info",
                                                    color = fontColor,
                                                    fontSize = fontSize.plus(5).sp
                                                )
                                            },
                                            text = {
                                                Text(text = "Date : ${notesModel.}")
                                            },
                                            onDismissRequest = {  },
                                            confirmButton = {  }
                                        )
                                    }else{
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "alarm",
                                            tint = fontColor
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider()
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes text */
                            Text(
                                modifier = Modifier
                                    .padding(end = 3.dp,start = 10.dp, bottom = 10.dp),
                                text = notesModel.content,
                                color = colorFont.value,
                                fontSize = fontSize.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                    }
                }
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Checkbox(
                        modifier = Modifier
                            .padding(end = 5.dp, bottom = 10.dp),
                        checked = if (selectAllStatus) isAllItemsSelected.value
                        else isItemSelected.value,
                        onCheckedChange = {
                            if (selectAllStatus) {
                                isAllItemsSelected.value = it
                            } else {
                                isItemSelected.value = it
                            }
                            onCheckedChange(if (it) 1 else 0)
                        }
                    )
                }
            } else {
                isItemSelected.value = false
                isAllItemsSelected.value = false
            }
        }
    }
}