package rustam.urazov.marvelapp.feature.data

import retrofit2.Response
import retrofit2.Retrofit
import rustam.urazov.marvelapp.feature.model.CharactersResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarvelService @Inject constructor(retrofit: Retrofit) : MarvelApi {

    private val marvelApi by lazy { retrofit.create(MarvelApi::class.java) }

    override suspend fun characters(offset: String): Response<CharactersResponse> =
        marvelApi.characters(offset)

    override suspend fun characterDetails(id: Int): Response<CharactersResponse> =
        marvelApi.characterDetails(id)
}