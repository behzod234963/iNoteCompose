package coder.behzod.presentation.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrashScreenItem(
    model: TrashModel,
    fontColor: Color,
    isSelected: Boolean = true,
    isItemsChecked: Boolean
) {
    val isItemChecked = remember { mutableStateOf(isItemsChecked) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(top = 10.dp)
            .clickable {

            }
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(model.color))
                .border(width = 1.dp, color = fontColor, shape = RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .combinedClickable (onLongClick = {
                        isSelected = true
                    }){

                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//          This is notes title
                    Text(
                        text = model.title,
                        color = fontColor,
                        fontSize = 25.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
//          This is notes data added
                    Text(
                        text = model.daysLeft,
                        color = if (model.color == Color.Gray.toArgb()) Color.White else Color.Gray,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//          This is notes text
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable {
                                /*TODO*/
                            },
                        text = model.content,
                        color = fontColor,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
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
                        .padding(end = 15.dp, bottom = 15.dp),
                    checked = isItemChecked.value,
                    onCheckedChange = {
                        model.isSelected = it
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTSI() {
    TrashScreenItem(
        model =
        TrashModel(1, "dtyjnthy", "netynt", 1, "mnyju"),
        fontColor = Color.White,
        isItemsChecked = false
    )
}