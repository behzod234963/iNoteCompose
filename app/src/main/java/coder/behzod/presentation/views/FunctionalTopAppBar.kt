package coder.behzod.presentation.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coder.behzod.R
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.notes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionalTopAppBar(
    themeColor: Color,
    fontColor: Color,
    navController: NavController
) {
    TopAppBar(
        modifier = Modifier
            .padding(2.dp)
            .border(width = 1.dp, color = fontColor, shape = RoundedCornerShape(10.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = themeColor
        ),
        title = {
            Text(
                text = stringResource(R.string.creating_note),
                fontSize = 25.sp,
                fontFamily = FontFamily(fontAmidoneGrotesk),
                color = fontColor
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (notes.isEmpty()){
                    navController.navigate(ScreensRouter.EmptyMainScreenRoute.route)
                }else{
                    navController.navigate(ScreensRouter.MainScreenRoute.route)
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "back to main",
                    tint = fontColor
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewFTAB() {
    FunctionalTopAppBar(
        Color.Black,
        Color.White,
        navController = NavController(LocalContext.current)
    )
}