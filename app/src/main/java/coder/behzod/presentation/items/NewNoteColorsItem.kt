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
) {
    Column(
        modifier = Modifier
            .size(40.dp)
            .padding(5.dp)
            .border(width = 1.dp, color = color, shape = CircleShape)
            .clip(CircleShape)
    ) {}
}