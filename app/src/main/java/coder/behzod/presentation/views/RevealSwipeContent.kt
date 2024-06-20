package coder.behzod.presentation.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coder.behzod.R
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> RevealSwipeContent(
    item: T,
    onDelete: (T) -> Unit,
    onShare: (T) -> Unit,
    content: @Composable (T) -> Unit
) {

    RevealSwipe(
        modifier = Modifier
            .padding(top = 14.dp),
        directions = setOf(
            RevealDirection.EndToStart,
            RevealDirection.StartToEnd
        ),
        closeOnBackgroundClick = true,
        onBackgroundEndClick = {
            onDelete(item)
        },
        hiddenContentEnd = {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                imageVector = Icons.Default.Delete,
                contentDescription = "revealBtnDelete",
                tint = Color.White
            )

        },
        onBackgroundStartClick = {
            onShare(item)
        },
        hiddenContentStart = {
            Icon(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "revealBtnShare",
                tint = Color.White
            )
        }
    ) {
        content(item)
    }
}