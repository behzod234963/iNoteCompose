package coder.behzod.presentation.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.R
import coder.behzod.domain.model.TrashModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.viewModels.TrashViewModel

@Composable
fun TrashScreenItem(
    model: TrashModel,
    fontColor: Color,
    fontSize:Int,
    onClick: () -> Unit,
    onChange: (Int) -> Unit,
    isDialogVisible: (Boolean) -> Unit,
    selectedContent: (Int) -> Unit,
    isSelected: Boolean = false,
    viewModel: TrashViewModel = hiltViewModel()
) {

    val selectAllStatus = viewModel.isItemSelected.value
    val isItemSelected = remember { mutableStateOf(false) }
    val isAllItemSelected = remember { mutableStateOf(true) }
    val isClicked = remember { mutableStateOf( true) }

    if (isClicked.value) isItemSelected.value = false
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 5.dp, end = 5.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(10.dp),
        border = if (model.color == Color.Black.toArgb())BorderStroke(width = 1.dp, color = Color.White) else BorderStroke(width = 0.dp, color = Color.Transparent),
        onClick = {
            if (isClicked.value){
                isDialogVisible(true)
                selectedContent(2)
                onClick()
            }
        },
        colors = CardColors(
            containerColor = Color(model.color),
            contentColor = Color(model.color),
            disabledContentColor = Color(model.color),
            disabledContainerColor = Color(model.color)
        )
    ){
        Column{
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(model.color))
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        /* This is notes title */
                        Text(
                            text = model.title,
                            maxLines = 1,
                            color = fontColor,
                            fontSize = fontSize.plus(7).sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                        /* This is notes date */
                        Text(
                            text = "${model.daysLeft} ${stringResource(id = R.string.days)}",
                            color = if (model.color == Color.Black.toArgb() ) Color.White else Color.Black,
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        /* This is notes content */
                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            text = model.content,
                            color = if (model.color == Color.Black.toArgb())Color.White else Color.Black,
                            fontSize = fontSize.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
                }
            }
            if (isSelected) {
                isClicked.value = false
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Checkbox(
                        modifier = Modifier
                            .padding(end = 15.dp, bottom = 15.dp),
                        checked = if (selectAllStatus) isAllItemSelected.value
                        else isItemSelected.value,
                        onCheckedChange = {
                            if (selectAllStatus) {
                                isAllItemSelected.value = it
                            } else {
                                isItemSelected.value = it
                            }
                            onChange(if (it) 1 else 0)
                        }
                    )
                }
            }else{
                isClicked.value = true
            }
        }
    }
}