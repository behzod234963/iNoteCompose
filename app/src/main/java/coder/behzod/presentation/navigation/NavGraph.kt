package coder.behzod.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.screens.EmptyMainScreen
import coder.behzod.presentation.screens.MainScreen
import coder.behzod.presentation.screens.NewNoteScreen
import coder.behzod.presentation.screens.SettingsScreen
import coder.behzod.presentation.screens.SplashScreens

@Composable
fun NavGraph() {
    Log.d("BBB", "NavGraph: is started")
    val navController = rememberNavController()
    val ctx = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = ScreensRouter.SplashScreenRoute.route
    ) {
        composable(ScreensRouter.SplashScreenRoute.route) {
            SplashScreens(navController,SharedPreferenceInstance(ctx))
        }
        composable(ScreensRouter.SettingsScreenRoute.route) {
            SettingsScreen(navController,SharedPreferenceInstance(ctx))
        }
        composable(ScreensRouter.MainScreenRoute.route) {
            MainScreen(navController, null,SharedPreferenceInstance(ctx))
        }
        composable(ScreensRouter.EmptyMainScreenRoute.route) {
            EmptyMainScreen(navController,SharedPreferenceInstance(ctx))
        }
        composable(ScreensRouter.NewNoteScreenRoute.route) {
            NewNoteScreen(navController,null,SharedPreferenceInstance(ctx))
        }
    }
}