package coder.behzod.presentation.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coder.behzod.R
import coder.behzod.presentation.theme.fontAmidoneGrotesk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionalTopAppBar(
    onSave: () -> Unit,
    onShare:()->Unit
) {
    TopAppBar(
        modifier = Modifier
            .border(width = 2.dp, color = Color.White),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        ),
        title = {
            Text(
                text = "Creating note...",
                fontSize = 25.sp,
                fontWeight = FontWeight(700),
                fontFamily = FontFamily(fontAmidoneGrotesk),
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    tint = Color.White,
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "button back to main"
                )
            }
        },
        actions = {
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp),
                onClick = {
                    onSave()
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_save),
                    contentDescription = "button save note",
                    tint = Color.White
                )
            }
            IconButton(
                modifier = Modifier
                    .padding(end = 10.dp),
                onClick = {
                    onShare()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "button share note",
                    tint = Color.White
                )
            }
        }
    )
}