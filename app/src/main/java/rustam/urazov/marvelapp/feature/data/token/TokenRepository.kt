package rustam.urazov.marvelapp.feature.data.token

interface TokenRepository {

    fun getToken(): String

    fun saveToken(token: String)

}