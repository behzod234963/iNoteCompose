package coder.behzod.presentation.views

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coder.behzod.R
import coder.behzod.presentation.navigation.ScreensRouter

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun BottomNavigationView(
    themeColor: Color,
    fontColor: Color,
    navController: NavHostController
) {

    val isNotesSelected = remember { mutableStateOf(false) }
    val isTrashSelected = remember { mutableStateOf(false) }
    val isSettingsSelected = remember { mutableStateOf(false) }

    BottomNavigation(
        modifier = Modifier
            .padding(2.dp),
        backgroundColor = themeColor,
        contentColor = fontColor,
        elevation = 5.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            /* All notes */
            BottomNavigationItem(
                selected = isNotesSelected.value,
                onClick = {
                    isNotesSelected.value = true
                    isTrashSelected.value = false
                    isSettingsSelected.value = false
                    navController.popBackStack()

                    if (isNotesSelected.value) {
                        navController.navigate(ScreensRouter.MainScreenRoute.route)
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        painter = painterResource(id = R.drawable.ic_notes),
                        contentDescription = "all notes",
                        tint = fontColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.all_notes)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )

            /* Trashed Notes */
            BottomNavigationItem(
                selected = isTrashSelected.value,
                onClick = {

                    isTrashSelected.value = true
                    isSettingsSelected.value = false
                    isNotesSelected.value = false

                    if (isTrashSelected.value) {
                        navController.navigate(ScreensRouter.TrashScreen.route)
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "trashed notes",
                        tint = fontColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.draft)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )

            /* Settings */
            BottomNavigationItem(
                selected = isSettingsSelected.value,
                onClick = {

                    isSettingsSelected.value = true
                    isNotesSelected.value = false
                    isTrashSelected.value = false

                    if (isSettingsSelected.value) {
                        navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                    }
                },
                icon = {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Icons.Default.Settings,
                        contentDescription = "all notes",
                        tint = fontColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.settings)
                    )
                },
                alwaysShowLabel = true,
                selectedContentColor = LocalContentColor.current,
                unselectedContentColor = Color.Transparent
            )
        }
    }
}