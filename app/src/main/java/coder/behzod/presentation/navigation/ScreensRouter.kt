package coder.behzod.presentation.navigation

sealed class ScreensRouter (val route:String){
    object EmptyMainScreenRoute: ScreensRouter("empty_main_screen")
    object MainScreenRoute: ScreensRouter("main_screen")
    object SettingsScreenRoute: ScreensRouter("settings_screen")
    object SplashScreenRoute: ScreensRouter("splash_screen")
    object NewNoteScreenRoute: ScreensRouter("new_note_screen")
    object TrashScreen:ScreensRouter("trash_screen_route")
}