package coder.behzod.data.local.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceInstance(ctx: Context) {
    val sharedPreferences: SharedPreferences =
        ctx.getSharedPreferences(SHARED_PREF_NAME, AppCompatActivity.MODE_PRIVATE)

    companion object {
        private const val SHARED_PREF_NAME = "SHARED_PREF_NAME"
    }
}