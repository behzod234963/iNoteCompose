package coder.behzod.presentation.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.utils.constants.KEY_INDEX
import coder.behzod.presentation.utils.constants.KEY_SPLASH_VISIBILITY
import coder.behzod.presentation.views.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var navController: NavController
    @Inject lateinit var sharedPrefs:SharedPreferenceInstance

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter",
        "UnusedMaterial3ScaffoldPaddingParameter"
    )
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isSplashVisibility = sharedPrefs.sharedPreferences.getBoolean(
            KEY_SPLASH_VISIBILITY,true)
        setContent {

            val themeIndex =
                remember { mutableIntStateOf(sharedPrefs.sharedPreferences.getInt(KEY_INDEX, 0)) }
            val colorTheme = if (themeIndex.intValue == 0) Color.Black else Color.White
            val themeColor = remember { mutableStateOf(colorTheme) }
            if (colorTheme == Color.Black) {
                themeColor.value = Color.Black
            } else {
                themeColor.value = Color.White
            }
            val colorFont = if (themeColor.value == Color.Black) Color.White else Color.Black
            val fontColor = remember { mutableStateOf(colorFont) }
            if (colorFont == Color.White) {
                fontColor.value = Color.White
            } else {
                fontColor.value = Color.Black
            }

            Scaffold (
                modifier = Modifier
                    .fillMaxSize(),
                bottomBar = {
                    if (!isSplashVisibility){
                        BottomNavigationView(
                            themeColor = themeColor.value,
                            fontColor = fontColor.value,
                            navController = navController
                        )
                    }
                }
            ){
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    NavGraph()
                }
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}
