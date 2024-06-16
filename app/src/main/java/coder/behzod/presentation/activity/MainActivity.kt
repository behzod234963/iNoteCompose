package coder.behzod.presentation.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import coder.behzod.R
import coder.behzod.data.local.sharedPreferences.SharedPreferenceInstance
import coder.behzod.presentation.navigation.NavGraph
import coder.behzod.presentation.utils.constants.KEY_CONTENT
import coder.behzod.presentation.utils.constants.KEY_SHARE
import coder.behzod.presentation.utils.constants.KEY_TITLE
import coder.behzod.presentation.utils.helpers.ShareAndSave
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var sharedPrefs:SharedPreferenceInstance

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs.sharedPreferences.getBoolean(KEY_SHARE,false)
        val title = sharedPrefs.sharedPreferences.getString(KEY_TITLE,"")
        val content = sharedPrefs.sharedPreferences.getString(KEY_CONTENT,"")
        setContent {
        val onShare = remember { mutableStateOf( sharedPrefs.sharedPreferences.getBoolean(KEY_SHARE,false) ) }

            if (onShare.value){
                NavGraph()
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT,"$title\n$content")
                }.also {
                    val chooser = Intent.createChooser(
                        it,this@MainActivity.getString(R.string.share_via)
                    )
                    startActivity(chooser)
                }
                sharedPrefs.sharedPreferences.edit().putBoolean(KEY_SHARE,false).apply()
            }else{
                NavGraph()
            }
        }
    }
}


//startActivity(
//                        this@MainActivity,
//                        Intent.createChooser(intent, this@MainActivity.getString(R.string.share_via)),
//                        null
//                    )