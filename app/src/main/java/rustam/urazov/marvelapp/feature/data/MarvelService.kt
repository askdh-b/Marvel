package rustam.urazov.marvelapp.feature.data

import retrofit2.Response
import retrofit2.Retrofit
import rustam.urazov.marvelapp.feature.model.CharactersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarvelService @Inject constructor(retrofit: Retrofit) : MarvelApi {
    private val marvelApi by lazy { retrofit.create(MarvelApi::class.java) }

    override suspend fun characters(ts: String, apikey: String, hash: String): Response<CharactersResponse> =
        marvelApi.characters(ts, apikey, hash)

    override suspend fun characterDetails(
        id: Int,
        ts: String,
        apikey: String,
        hash: String
    ): Response<CharactersResponse> = marvelApi.characterDetails(id, ts, apikey, hash)
}