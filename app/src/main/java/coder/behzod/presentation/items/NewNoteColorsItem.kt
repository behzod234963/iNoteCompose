package coder.behzod.presentation.items

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorsItem(
    color:Color,
    fontColor: Color,
    onClick:()->Unit
) {
    Card(
        modifier = Modifier
            .size(30.dp),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            5.dp
        ),
        border = BorderStroke(
            width = 0.3.dp,
            color = fontColor,
        ),
        colors = CardColors(
            containerColor = color,
            contentColor = color,
            disabledContentColor = color,
            disabledContainerColor = color
        ),
        onClick = { onClick() }
    ){}
}