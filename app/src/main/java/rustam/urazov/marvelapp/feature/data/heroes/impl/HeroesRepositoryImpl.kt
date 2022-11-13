package rustam.urazov.marvelapp.feature.data.heroes.impl

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.feature.data.Either
import rustam.urazov.marvelapp.feature.data.MarvelService
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.data.request
import rustam.urazov.marvelapp.feature.model.CharactersResponse
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler, private val service: MarvelService
) : HeroesRepository {

    override suspend fun getCharacterDetails(characterId: Int): Either<Failure, rustam.urazov.marvelapp.feature.model.Character> =
        when (networkHandler.isNetworkAvailable()) {
            true -> request(
                { service.characterDetails(characterId) },
                { it.charactersData.result.first().toCharacter() },
                CharactersResponse.empty
            )
            false -> Either.Left(Failure.ConnectionError)
        }

    override suspend fun getCharacters(offset: Int): Either<Failure, List<rustam.urazov.marvelapp.feature.model.Character>> =
        when (networkHandler.isNetworkAvailable()) {
            true -> request(
                { service.characters(offset.toString()) },
                { it.charactersData.result.map { characterEntity -> characterEntity.toCharacter() } },
                CharactersResponse.emptyList
            )
            false -> Either.Left(Failure.ConnectionError)
        }
}