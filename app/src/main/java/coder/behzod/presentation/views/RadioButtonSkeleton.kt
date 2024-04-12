package coder.behzod.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun  RadioButtonSkeleton (
    selected:Boolean,
    onSelect:()->Unit,
    themeColor: Color,
    fontColors: Color
){
    Row(
        modifier = Modifier
            .background(color = themeColor),
    ){
        RadioButton(
            selected = selected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Magenta,
                unselectedColor = fontColors
            )
        )
    }
}