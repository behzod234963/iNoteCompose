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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coder.behzod.R
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.viewModels.MainViewModel

@Composable
fun MainScreenGridItem(
    themeColor: Color,
    fontColor: Color,
    fontSize: Int,
    onClick: () -> Unit,
    onShare: () -> Unit,
    onChange: (Int) -> Unit,
    onDelete: () -> Unit,
    isSelected: Boolean,
    notesModel: NotesModel,
    viewModel: MainViewModel = hiltViewModel(),
    title: String,
    note: String,
    date: String,
    backgroundColor: Int
) {

    val selectAllStatus = viewModel.selectAllStatus
    val isItemSelected = remember { mutableStateOf(false) }
    val isAllItemSelected = remember { mutableStateOf(true) }

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
            .width(200.dp)
            .padding(5.dp)
            .background(themeColor)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(5.dp),
        border = BorderStroke(
            1.dp,
            if (themeColor == Color.Black) Color.White else Color.Transparent
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(backgroundColor)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color(backgroundColor))
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
        ) {

            /* Here is header content it's contains title and btnShare */
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 7.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                /* This is title */
                Text(
                    modifier = Modifier
                        .width(140.dp),
                    text = "$title...",
                    maxLines = 1,
                    fontSize = fontSize.plus(7).sp,
                    color = colorFont.value,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
                IconButton(
                    modifier = Modifier,
                    onClick = {
                        onShare()
                    }) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        painter = painterResource(id = R.drawable.ic_share),
                        contentDescription = "btn share note",
                        tint = colorFont.value
                    )
                }
            }
            HorizontalDivider()

            /*Here is content*/
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp)
                    .padding(top = 7.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                /* This is content */
                Text(
                    text = note,
                    color = colorFont.value,
                    fontSize = fontSize.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
            }
            HorizontalDivider()

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 7.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                /* This is date */
                Text(
                    text = if (fontSize == 25 || fontSize == 32) date.substring(0..date.length - 6) else date,
                    color = colorFont.value,
                    fontSize = fontSize.minus(3).sp,
                )
                if (isSelected){

                    Checkbox(
                        modifier = Modifier
                            .padding(end = 10.dp, bottom = 10.dp),
                        checked = if (selectAllStatus.value) isAllItemSelected.value
                        else isItemSelected.value,
                        onCheckedChange = {
                            if (selectAllStatus.value) {
                                isAllItemSelected.value = it
                            } else {
                                isItemSelected.value = it
                            }
                            onChange(if (it) 1 else 0)
                        }
                    )
                }else{

                    IconButton(
                        modifier = Modifier
                            .size(35.dp),
                        onClick = {
                            onDelete()
                        }) {
                        Icon(
                            modifier = Modifier
                                .size(35.dp),
                            imageVector = Icons.Default.Delete,
                            contentDescription = "btn delete note",
                            tint = colorFont.value
                        )
                    }
                }
            }
        }
    }
}