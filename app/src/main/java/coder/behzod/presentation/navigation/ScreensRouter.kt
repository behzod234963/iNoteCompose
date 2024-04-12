package coder.behzod.presentation.navigation

sealed class ScreensRouter (val route:String){
    data object EmptyMainScreenRoute: ScreensRouter("empty_main_screen")
    data object MainScreenRoute: ScreensRouter("main_screen")
    data object SettingsScreenRoute: ScreensRouter("settings_screen")
    data object SplashScreenRoute: ScreensRouter("splash_screen")
    data object NewNoteScreenRoute: ScreensRouter("new_note_screen")
}