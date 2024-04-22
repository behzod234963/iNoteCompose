package coder.behzod.presentation.items

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@Composable
fun MainScreenItem(
    notesModel: NotesModel,
    fontColor: Color,
    onClick:()->Unit
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .padding(top = 10.dp)
    ){
        Spacer(modifier = Modifier.height(10.dp ))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(notesModel.color))
                .border(width = 1.dp, color = fontColor, shape = RoundedCornerShape(20.dp))
                .clickable { onClick() }
        ) {
            Column (
                modifier = Modifier
                    .clickable { onClick() }
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .clickable { onClick() },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//          This is notes title
                    notesModel.title?.let {
                        Text(
                            modifier = Modifier
                                .clickable { onClick() },
                            text = it,
                            color = fontColor,
                            fontSize = 25.sp,
                            fontFamily = FontFamily(fontAmidoneGrotesk)
                        )
                    }
//          This is notes data added
                    Text(
                        modifier = Modifier
                            .clickable { onClick() },
                        text = notesModel.dataAdded.toString(),
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
                Row(
                    modifier = Modifier
                        .clickable { notesModel.id }
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//          This is notes text
                    Text(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable { notesModel.id },
                        text = notesModel.note,
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fontAmidoneGrotesk)
                    )
                }
            }
        }
    }
}