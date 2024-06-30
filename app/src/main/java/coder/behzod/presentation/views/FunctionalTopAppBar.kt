package coder.behzod.presentation.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.items.ColorsItem
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.theme.green
import coder.behzod.presentation.theme.liteGreen
import coder.behzod.presentation.theme.yellow
import coder.behzod.presentation.utils.constants.KEY_LIST_STATUS

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunctionalTopAppBar(
    themeColor: Color,
    fontColor: Color,
    priorityColorSelector: (Int) -> Unit,
    sharedPrefs: SharedPreferenceInstance,
    navController: NavController
) {
    val isEmpty = remember {
        mutableStateOf(sharedPrefs.sharedPreferences.getBoolean(KEY_LIST_STATUS, true))
    }
    val isExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .background(themeColor)
            .padding(2.dp)
            .border(width = 1.dp, color = fontColor, shape = RoundedCornerShape(10.dp)),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = themeColor
        ),
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                if (isEmpty.value) {
                    navController.navigate(ScreensRouter.EmptyMainScreenRoute.route)
                } else {
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