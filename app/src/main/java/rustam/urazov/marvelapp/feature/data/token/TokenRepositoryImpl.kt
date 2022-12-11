package rustam.urazov.marvelapp.feature.data.token

import rustam.urazov.marvelapp.feature.data.storage.sharedPreferences.MarvelSharedPreferences
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor (private val marvelSharedPreferences: MarvelSharedPreferences) :TokenRepository {

    override fun getToken(): String = marvelSharedPreferences.getToken()

    override fun saveToken(token: String) {
        marvelSharedPreferences.saveToken(token)
    }
}