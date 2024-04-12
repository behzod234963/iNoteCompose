package coder.behzod.presentation.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.utils.constants.KEY_LANGUAGES
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var sharedPrefs:SharedPreferenceInstance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val index = sharedPrefs.sharedPreferences.getInt(KEY_LANGUAGES,2)
        setContent {
            NavGraph()
            Toast.makeText(this@MainActivity, "index $index", Toast.LENGTH_SHORT).show()
        }
    }
}
