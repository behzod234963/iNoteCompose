package coder.behzod.presentation.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.R
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    onShare: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {

    /* ContentType 0-> Delete , 1->Share */
    val contentType = remember { mutableIntStateOf(0) }

    val isRemoved = remember { mutableStateOf(false) }
    val isShared = remember { mutableStateOf(false) }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved.value = true
                contentType.intValue = 0
                true
            } else (if (value == SwipeToDismissBoxValue.StartToEnd) {
                isShared.value = true
                contentType.intValue = 1
                true
            } else {
                isRemoved.value = false
                isShared.value = false
                false
            })
        },
    )

    LaunchedEffect(key1 = isRemoved.value) {
        if (isRemoved.value) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }
    LaunchedEffect(key1 = isShared.value) {
        if (isShared.value) {
            delay(animationDuration.toLong())
            onShare(item)
            isShared.value = false
        }
    }
    AnimatedVisibility(
        visible = if (contentType.intValue == 0) !isRemoved.value else isShared.value,
        enter = fadeIn() + expandIn(),
        exit = shrinkVertically(
            animationSpec = snap(delayMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            enableDismissFromStartToEnd = true,
            enableDismissFromEndToStart = true,
            backgroundContent = {
                if (contentType.intValue == 0) {
                    DeleteBackground(swipeDismissState = state)
                } else {
                    ShareBackground(swipeDismissState = state)
                }
            }) {
            content(item)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {

    val color = if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red
    } else Color.Transparent

    val shareContentColor =
        if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Color.Blue
        } else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "", tint = Color.White
            )
            Spacer(modifier = Modifier.width(17.dp))
            Text(
                text = stringResource(id = R.string.delete),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareBackground(
    swipeDismissState: SwipeToDismissBoxState
) {

    val shareContentColor =
        if (swipeDismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
            Color.Blue
        } else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(shareContentColor)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "btn share",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(17.dp))
            Text(
                text = stringResource(id = R.string.share),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = FontFamily(fontAmidoneGrotesk)
            )
        }
    }
}