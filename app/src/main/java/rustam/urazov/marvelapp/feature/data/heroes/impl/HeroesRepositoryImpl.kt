package rustam.urazov.marvelapp.feature.data.heroes.impl

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.core.platform.map
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.data.network.MarvelService
import rustam.urazov.marvelapp.feature.data.storage.CharacterDao
import rustam.urazov.marvelapp.feature.model.CharacterModel
import javax.inject.Inject

class HeroesRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val networkService: MarvelService,
    private val storageService: CharacterDao
) : HeroesRepository {

    override suspend fun getCharacterDetails(characterId: Int): Either<Failure, CharacterModel> =
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                val result = networkService.characterDetails(characterId)
                    .map { it.charactersData.result[0].toCharacter() }
                when (result.isRight) {
                    true -> result
                    false -> try {
                        Either.Right(storageService.getCharacter(characterId).toCharacter())
                    } catch (e: Exception) {
                        Either.Left(Failure.Error(e.message.orEmpty()))
                    }
                }
            }
            false -> Either.Right(storageService.getCharacter(characterId).toCharacter())
        }

    override suspend fun getCharacters(offset: Int): Either<Failure, List<CharacterModel>> =
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                val result = networkService.characters(offset.toString())
                    .map { it.charactersData.result.map { character -> character.toCharacter() } }
                when (result.isRight) {
                    true -> {
                        if (storageService.getCharacters(offset).size == 20) {
                            storageService.updateCharacters(characters = (result as Either.Right).b.map { it.toCharacterEntity() })
                        } else storageService.insertCharacters((result as Either.Right).b.map { it.toCharacterEntity() })
                        result
                    }
                    false -> try {
                        Either.Right(storageService.getCharacters(offset).map { it.toCharacter() })
                    } catch (e: Exception) {
                        Either.Left(Failure.Error(e.message.orEmpty()))
                    }
                }
            }
            false -> Either.Right(storageService.getCharacters(offset).map { it.toCharacter() })
        }
}