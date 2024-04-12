package coder.behzod.presentation.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@Composable
fun MainScreenItem(
    notesModel: NotesModel,
    backgroundColor: Color,
    fontColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(backgroundColor)
            .border(width = 1.dp, color = fontColor)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//          This is notes title
                notesModel.title?.let {
                    Text(
                        text = it,
                        color = fontColor,
                        fontSize = 22.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
//          This is notes data added
                Text(
                    text = notesModel.dataAdded.toString(),
                    color = Color.Gray,
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
                        .padding(start = 10.dp),
                    text = notesModel.note,
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(fontAmidoneGrotesk)
                )
                Column(
                    modifier = Modifier
                        .size(50.dp)
                ) {

                }
            }
        }
    }
}