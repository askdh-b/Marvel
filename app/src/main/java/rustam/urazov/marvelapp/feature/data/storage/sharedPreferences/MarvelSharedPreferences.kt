package rustam.urazov.marvelapp.feature.data.storage.sharedPreferences

interface MarvelSharedPreferences {

    fun getToken(): String

    fun saveToken(token: String)

}