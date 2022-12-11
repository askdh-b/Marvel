package rustam.urazov.marvelapp.feature.data.network.marvel

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either

interface MarvelApi {

    companion object {
        private const val ID = "id"
        private const val CHARACTERS = "characters"
        private const val CHARACTER_DETAILS = "$CHARACTERS/{$ID}"
        private const val OFFSET = "offset"
    }

    @GET(CHARACTERS)
    suspend fun characters(@Query(OFFSET) offset: String): Either<Failure, CharactersResponse>

    @GET(CHARACTER_DETAILS)
    suspend fun characterDetails(@Path(ID) id: Int): Either<Failure, CharactersResponse>

}