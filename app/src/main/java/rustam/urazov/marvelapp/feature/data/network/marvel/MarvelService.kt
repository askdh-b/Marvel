package rustam.urazov.marvelapp.feature.data.network.marvel

import retrofit2.Retrofit
import rustam.urazov.marvelapp.core.di.MarvelModule
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MarvelService @Inject constructor(@Named(MarvelModule.MARVEL) retrofit: Retrofit) : MarvelApi {

    private val marvelApi by lazy { retrofit.create(MarvelApi::class.java) }

    override suspend fun characters(offset: String): Either<Failure, CharactersResponse> =
        marvelApi.characters(offset)

    override suspend fun characterDetails(id: Int): Either<Failure, CharactersResponse> =
        marvelApi.characterDetails(id)
}