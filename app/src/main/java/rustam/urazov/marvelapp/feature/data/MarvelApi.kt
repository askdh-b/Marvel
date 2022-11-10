package rustam.urazov.marvelapp.feature.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rustam.urazov.marvelapp.feature.model.CharactersResponse
import javax.inject.Singleton

@Singleton
interface MarvelApi {

    companion object {
        private const val ID = "id"
        private const val CHARACTERS = "characters"
        private const val CHARACTER_DETAILS = "$CHARACTERS/{$ID}"
        private const val OFFSET = "offset"
    }

    @GET(CHARACTERS)
    suspend fun characters(@Query(OFFSET) offset: String): Response<CharactersResponse>

    @GET(CHARACTER_DETAILS)
    suspend fun characterDetails(@Path(ID) id: Int): Response<CharactersResponse>

}