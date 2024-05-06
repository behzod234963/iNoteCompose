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
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.screens.EmptyMainScreen
import coder.behzod.presentation.screens.MainScreen
import coder.behzod.presentation.screens.NewNoteScreen
import coder.behzod.presentation.screens.SettingsScreen
import coder.behzod.presentation.screens.SplashScreens

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
            MainScreen(navController = navController, sharedPrefs = SharedPreferenceInstance(ctx))
        }
        composable(ScreensRouter.EmptyMainScreenRoute.route) {
            EmptyMainScreen(navController, sharedPrefs = SharedPreferenceInstance(ctx))
        }
        composable(
            route = ScreensRouter.NewNoteScreenRoute.route +"/{note_id}",
            arguments = listOf(
                navArgument("{note_id}") {
                    this.type = NavType.IntType
                    this.defaultValue = -1
                    this.build()
                }
            )
        ) {navBackStackEntry->
                NewNoteScreen(
                    navController = navController,
                    notesModel = null,
                    id = navBackStackEntry.arguments?.getInt("{note_id}",-1)!!,
                    sharedPrefs = SharedPreferenceInstance(ctx)
                )
        }
    }
}