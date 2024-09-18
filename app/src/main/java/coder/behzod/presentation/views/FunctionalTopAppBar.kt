package coder.behzod.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.fontAmidoneGrotesk
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionalTopAppBar(
    themeColor: Color,
    fontColor: Color,
    fontSize:Int,
    sharedPrefs: SharedPreferenceInstance,
    navController: NavHostController
) {
    val isEmpty = remember {
        mutableStateOf(sharedPrefs.sharedPreferences.getBoolean(KEY_LIST_STATUS, true))
    }

    Card (
        modifier = Modifier
            .fillMaxWidth()
            .background(themeColor),
        shape = RoundedCornerShape(10.dp),
        elevation = 10.dp,
        backgroundColor = themeColor
    ){
        TopAppBar(
            modifier = Modifier
                .background(themeColor)
                .padding(2.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = themeColor
            ),
            title = {
                Text(
                    text = stringResource(id = R.string.creating_note),
                    color = fontColor,
                    fontFamily = FontFamily(fontAmidoneGrotesk),
                    fontSize = fontSize.plus(7).sp
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    if (isEmpty.value) {
                        navController.popBackStack()
                        navController.navigate(ScreensRouter.EmptyMainScreenRoute.route)
                    } else {
                        navController.popBackStack()
                        navController.navigate(ScreensRouter.MainScreenRoute.route)
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back to main",
                        tint = fontColor
                    )
                }
            },
        )
    }
}