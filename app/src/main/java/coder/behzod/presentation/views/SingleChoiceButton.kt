package coder.behzod.presentation.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrioritySelectButtons(
    items: List<Pair<String,Color>>,
    onItemSelected: (Int) -> Unit,
    themeColor: Color,
    fontColor: Color,
) {
    SingleChoiceSegmentedButtonRow {
        items.forEachIndexed{index, pair ->
            SegmentedButton(

                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = themeColor,
                    activeContentColor = Color.Black,
                    inactiveContainerColor = pair.second,
                ),
                selected = index == items.size ,
                onClick = {
                    when(index){
                        0->{ onItemSelected(0) }
                        1->{ onItemSelected(1) }
                        2->{ onItemSelected(2) }
                        3->{ onItemSelected(3) }
                    }
                },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = items.size
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pair.first,
                        color = Color.Black,
                        fontFamily = FontFamily(fontAmidoneGrotesk),
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}