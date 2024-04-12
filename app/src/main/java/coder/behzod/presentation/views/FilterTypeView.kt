package coder.behzod.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import coder.behzod.R
import coder.behzod.domain.utils.NoteOrder
import coder.behzod.domain.utils.OrderType
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@Composable
fun FilterTypeView(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    fontColor: Color,
    noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    onOrderChange:(NoteOrder)->Unit,
) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ){
        Row (
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
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
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
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
            Row (
                verticalAlignment = Alignment.CenterVertically
            ){
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