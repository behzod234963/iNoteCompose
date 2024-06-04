package coder.behzod.presentation.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coder.behzod.R
import coder.behzod.presentation.utils.events.PassDataEvents

@Composable
fun AnimatedDropDownMenu(
    backgroundColor: Color,
    fontColor: Color,
    initiallyOpened: Boolean = false,
    content: @Composable (events: PassDataEvents,expanded:PassDataEvents) -> Unit
) {
    val isExpanded = remember { mutableStateOf( false ) }
    var isOpen by remember {
        mutableStateOf(initiallyOpened)
    }
    val alpha = animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        ), label = ""
    )
    val rotateX = animateFloatAsState(
        targetValue = if (isOpen) 0f else -90f,
        animationSpec = tween(
            durationMillis = 300
        ), label = ""
    )
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "Open or close",
                tint = fontColor,
                modifier = Modifier
                    .padding(top = 15.dp,start = 10.dp, end = 5.dp)
                    .clickable {
                        isOpen = !isOpen
                        if (isOpen){
                            isExpanded.value = false
                        }
                    }
                    .scale(1f, if (isOpen) -1f else 1f)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0.5f, 0f)
                    rotationX = rotateX.value
                }
                .alpha(alpha.value)
        ) {
            content(PassDataEvents.PassStatus(isOpen),PassDataEvents.IsExpanded(isExpanded.value))
        }
    }
}