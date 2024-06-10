package coder.behzod.presentation.items

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import coder.behzod.presentation.utils.events.NotesEvent
import coder.behzod.presentation.viewModels.MainViewModel

@Composable
fun MainScreenItem(
    notesModel: NotesModel,
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
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(end = 5.dp,top = 15.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(15.dp),
        onClick = {
            onClick()
            notesModel.id
    }) {
        Box{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(notesModel.color))
                ) {
                    Column{
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes title */
                            Text(
                                text = notesModel.title,
                                color = colorFont.value,
                                fontSize = fontSize.plus(7).sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                            /* This is notes data added */
                            Text(
                                text = notesModel.dataAdded,
                                color = colorFont.value,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(fontAmidoneGrotesk)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            /* This is notes text */
                            Text(
                                modifier = Modifier
                                    .padding(start = 10.dp),
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
                                viewModel.onEvent(NotesEvent.SelectAllStatus(false))
                                isItemSelected.value = it
                            }
                            onCheckedChange(if (it) 1 else 0)
                        }
                    )
                }
            }
        }
    }
}