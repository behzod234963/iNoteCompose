package coder.behzod.presentation.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor

@Composable
fun ColorsItem(
    color:Color,
    onClick:()->Unit,
    fontColor:Color
) {
    Column(
        modifier = Modifier
            .size(60.dp)
            .padding(10.dp)
            .border(width = 1.dp, color = fontColor, shape = CircleShape)
            .clip(CircleShape)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .clickable { onClick() }
        ){}
    }
}