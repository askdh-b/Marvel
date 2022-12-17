package rustam.urazov.marvelapp.feature.data.storage.sharedPreferences

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import rustam.urazov.marvelapp.core.extention.empty
import javax.inject.Inject

class MarvelSharedPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    MarvelSharedPreferences {

    private val sharedPreferences = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE)

    companion object {
        private const val SETTINGS = "FCMSettings"
        private const val TOKEN = "token"
    }

    override fun getToken(): String =
        sharedPreferences.getString(TOKEN, String.empty()) ?: String.empty()

    override fun saveToken(token: String) {
        sharedPreferences.edit()
            .putString(TOKEN, token)
            .apply()
    }

}