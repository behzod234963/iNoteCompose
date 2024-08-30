package coder.behzod.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.work.WorkManager
import coder.behzod.data.local.dataStore.DataStoreInstance
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.domain.model.NotesModel
import coder.behzod.presentation.notifications.NotificationTrigger
import coder.behzod.presentation.screens.EmptyMainScreen
import coder.behzod.presentation.screens.MainScreen
import coder.behzod.presentation.screens.NewNoteScreen
import coder.behzod.presentation.screens.SettingsScreen
import coder.behzod.presentation.screens.SplashScreens
import coder.behzod.presentation.screens.TrashScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val ctx = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = ScreensRouter.SplashScreenRoute.route
    ) {
        composable(
            ScreensRouter.SplashScreenRoute.route
        ) {
            SplashScreens(
                navController = navController,
                sharedPrefs = SharedPreferenceInstance(ctx)
            )
        }
        composable(ScreensRouter.SettingsScreenRoute.route) {
            SettingsScreen(navController, sharedPrefs = SharedPreferenceInstance(ctx))
        }
        composable(
            route = ScreensRouter.MainScreenRoute.route
        ) {
            MainScreen(
                navController = navController,
                sharedPrefs = SharedPreferenceInstance(ctx),
                dataStoreInstance = DataStoreInstance(ctx),
                workManager = WorkManager.getInstance(ctx),
                notificationTrigger = NotificationTrigger(ctx)
            )
        }
        composable(ScreensRouter.EmptyMainScreenRoute.route) {
            EmptyMainScreen(navController, sharedPrefs = SharedPreferenceInstance(ctx))
        }
        composable(
            ScreensRouter.NewNoteScreenRoute.route + "/{id}",
            arguments = listOf(
                navArgument(
                    name = "id"
                ) {
                    type = NavType.IntType
                    defaultValue = -1
                },
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: -1
            NewNoteScreen(
                navController = navController,
                arguments = Arguments(id),
                sharedPrefs = SharedPreferenceInstance(ctx),
                dataStore = DataStoreInstance(ctx)
            )
        }
        composable(
            ScreensRouter.TrashScreen.route
        ) {
            TrashScreen(
                sharedPrefs = SharedPreferenceInstance(ctx),
                navController = navController,
                notesModel = NotesModel(
                    id = 0,
                    title = "",
                    content = "",
                    color = -1,
                    dataAdded = "",
                    alarmMapper = 0,
                    requestCode = 1,
                    stopCode = 2
                )
            )
        }
    }
}