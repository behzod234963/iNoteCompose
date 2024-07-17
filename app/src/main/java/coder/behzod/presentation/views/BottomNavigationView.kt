package coder.behzod.presentation.views

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.ScreensRouter
import coder.behzod.presentation.utils.constants.KEY_NOTES_ROUTE
import coder.behzod.presentation.utils.constants.KEY_SETTINGS_ROUTE
import coder.behzod.presentation.utils.constants.KEY_TRASH_ROUTE

@Composable
fun BottomNavigationView(
    themeColor: Color,
    fontColor: Color,
    sharedPrefs: SharedPreferenceInstance,
    navController: NavHostController
) {

    val isNotesSelected = remember { mutableStateOf(false) }
    val isNoteRepeating = remember {
        mutableStateOf(
            sharedPrefs.sharedPreferences.getBoolean(
                KEY_NOTES_ROUTE, true
            )
        )
    }

    val isTrashSelected = remember { mutableStateOf(false) }
    val isTrashRepeating = remember {
        mutableStateOf(
            sharedPrefs.sharedPreferences.getBoolean(
                KEY_TRASH_ROUTE, true
            )
        )
    }

    val isSettingsSelected = remember { mutableStateOf(false) }
    val isSettingsRepeating = remember {
        mutableStateOf(
            sharedPrefs.sharedPreferences.getBoolean(
                KEY_SETTINGS_ROUTE,
                true
            )
        )
    }

    BottomNavigation(
        modifier = Modifier
            .padding(2.dp)
            .border(width = 1.dp, shape = RoundedCornerShape(10.dp), color = fontColor),
        backgroundColor = themeColor,
        contentColor = fontColor,
        elevation = 50.dp,
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
                        if (isNoteRepeating.value) {
                            navController.navigate(ScreensRouter.MainScreenRoute.route)
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_NOTES_ROUTE, false)
                                .apply()
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_TRASH_ROUTE, true)
                                .apply()
                            sharedPrefs.sharedPreferences.edit()
                                .putBoolean(KEY_SETTINGS_ROUTE, true).apply()
                        }
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
                    navController.popBackStack()

                    if (isTrashSelected.value) {
                        if (isTrashRepeating.value) {
                            navController.navigate(ScreensRouter.TrashScreen.route)
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_NOTES_ROUTE, true)
                                .apply()
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_TRASH_ROUTE, false)
                                .apply()
                            sharedPrefs.sharedPreferences.edit()
                                .putBoolean(KEY_SETTINGS_ROUTE, true).apply()
                        }
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
                    navController.popBackStack()

                    if (isSettingsSelected.value) {
                        if (isSettingsRepeating.value){
                            navController.navigate(ScreensRouter.SettingsScreenRoute.route)
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_NOTES_ROUTE, true)
                                .apply()
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_TRASH_ROUTE, true)
                                .apply()
                            sharedPrefs.sharedPreferences.edit().putBoolean(KEY_SETTINGS_ROUTE, false)
                                .apply()
                        }
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