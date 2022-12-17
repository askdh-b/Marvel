package rustam.urazov.marvelapp.feature.data.characters.impl

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.core.platform.map
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.data.storage.marvel.CharacterDao
import rustam.urazov.marvelapp.feature.data.characters.CharacterModel
import rustam.urazov.marvelapp.feature.data.network.marvel.CharacterResponse
import rustam.urazov.marvelapp.feature.data.network.marvel.MarvelApi
import rustam.urazov.marvelapp.feature.data.storage.marvel.CharacterEntity
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val networkService: MarvelApi,
    private val storageService: CharacterDao
) : CharactersRepository {

    override suspend fun getCharacterDetails(characterId: Int): Either<Failure, CharacterModel> =
        try {
            when (networkHandler.isNetworkAvailable()) {
                true -> {
                    val result = networkService.characterDetails(characterId)
                        .map { toCharacter(it.charactersData.result[0]) }
                    when (result.isRight) {
                        true -> result
                        false -> Either.Right(toCharacter(getCharacterFromDataStore(characterId)))
                    }
                }
                false -> Either.Right(toCharacter(getCharacterFromDataStore(characterId)))
            }
        } catch (e: Exception) {
            Either.Left(Failure.NoDataError)
        }

    override suspend fun getCharacters(offset: Int): Either<Failure, List<CharacterModel>> = try {
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                val result = networkService.characters(offset.toString())
                    .map { it.charactersData.result.map { character -> toCharacter(character) } }
                when (result.isRight) {
                    true -> {
                        saveCharacters((result as Either.Right).b.map { it.toCharacterEntity() })
                        result
                    }
                    false -> {
                        val characters = getCharactersFromDataStore()
                        when (characters.isEmpty()) {
                            true -> Either.Left(Failure.NoDataError)
                            false -> Either.Right(characters.map { toCharacter(it) })
                        }
                    }
                }
            }
            false -> {
                val characters = getCharactersFromDataStore()
                when (characters.isEmpty()) {
                    true -> Either.Left(Failure.NoDataError)
                    false -> Either.Right(characters.map { toCharacter(it) })
                }
            }
        }
    } catch (e: Exception) {
        Either.Left(Failure.NoDataError)
    }

    private suspend fun getCharacterFromDataStore(characterId: Int): CharacterEntity =
        storageService.getCharacter(characterId)

    private suspend fun getCharactersFromDataStore(): List<CharacterEntity> =
        storageService.getCharacters()

    private suspend fun saveCharacters(characters: List<CharacterEntity>) =
        if (!getCharactersFromDataStore().map { toCharacter(it) }
                .containsAll(characters.map { toCharacter(it) })) insertCharacters(characters)
        else updateCharacters(characters)

    private suspend fun insertCharacters(characters: List<CharacterEntity>) {
        storageService.insertCharacters(characters)
    }

    private suspend fun updateCharacters(characters: List<CharacterEntity>) {
        storageService.updateCharacters(characters)
    }

    private fun toCharacter(characterResponse: CharacterResponse): CharacterModel =
        CharacterModel(
            id = characterResponse.id,
            name = characterResponse.name,
            description = characterResponse.description,
            thumbnail = characterResponse.thumbnail.toImageUri()
        )

    private fun toCharacter(characterEntity: CharacterEntity): CharacterModel = CharacterModel(
        id = characterEntity.chId,
        name = characterEntity.name,
        description = characterEntity.description,
        thumbnail = characterEntity.thumbnail
    )

}