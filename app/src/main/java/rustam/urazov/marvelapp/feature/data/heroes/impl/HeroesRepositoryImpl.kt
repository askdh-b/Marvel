package rustam.urazov.marvelapp.feature.data.heroes.impl

import dagger.hilt.android.scopes.ActivityScoped
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.feature.data.Either
import rustam.urazov.marvelapp.feature.data.MarvelService
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.data.request
import rustam.urazov.marvelapp.feature.model.CharactersResponse
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: MarvelService
) : HeroesRepository {

    override suspend fun getCharacterDetails(characterId: Int): Either<Failure, rustam.urazov.marvelapp.feature.model.Character> =
        when (networkHandler.isNetworkAvailable()) {
            true -> request(
                {service.characterDetails(
                    id = characterId,
                    ts = "3600",
                    apikey = "781ac4be09ebe9d7afd37e37d7a502e8",
                    hash = "97a5e47cec70c9cfb5f998f260bf55f6"
                )},
                { it.charactersData.result[0].toCharacter() },
                CharactersResponse.empty
            )
            false -> Either.Left(Failure.ConnectionError)
        }

    override suspend fun getCharacters(): Either<Failure, List<rustam.urazov.marvelapp.feature.model.Character>> =
        when (networkHandler.isNetworkAvailable()) {
            true -> request(
                {
                    service.characters(
                        ts = "3600",
                        apikey = "781ac4be09ebe9d7afd37e37d7a502e8",
                        hash = "97a5e47cec70c9cfb5f998f260bf55f6"
                    )
                },
                { it.charactersData.result.map { characterEntity -> characterEntity.toCharacter() } },
                CharactersResponse.emptyList
            )
            false -> Either.Left(Failure.ConnectionError)
        }
}