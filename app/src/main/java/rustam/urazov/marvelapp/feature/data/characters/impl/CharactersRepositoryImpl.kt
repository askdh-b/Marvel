package rustam.urazov.marvelapp.feature.data.characters.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.core.platform.map
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.data.storage.CharacterDao
import rustam.urazov.marvelapp.feature.data.characters.CharacterModel
import rustam.urazov.marvelapp.feature.data.network.CharacterResponse
import rustam.urazov.marvelapp.feature.data.network.ImageLoader
import rustam.urazov.marvelapp.feature.data.network.MarvelApi
import rustam.urazov.marvelapp.feature.data.storage.CharacterEntity
import javax.inject.Inject

class CharactersRepositoryImpl @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val networkService: MarvelApi,
    private val storageService: CharacterDao,
    private val imageLoader: ImageLoader
) : CharactersRepository {

    override suspend fun getCharacterDetails(characterId: Int): Either<Failure, CharacterModel> =
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

    override suspend fun getCharacters(offset: Int): Either<Failure, List<CharacterModel>> =
        when (networkHandler.isNetworkAvailable()) {
            true -> {
                val result = networkService.characters(offset.toString())
                    .map { it.charactersData.result.map { character -> toCharacter(character) } }
                when (result.isRight) {
                    true -> {
                        saveCharacters((result as Either.Right).b.map { it.toCharacterEntity() })
                        result
                    }
                    false -> Either.Right(getCharactersFromDataStore(offset).map { toCharacter(it) })
                }
            }
            false -> Either.Right(getCharactersFromDataStore(offset).map { toCharacter(it) })
        }

    private suspend fun getCharacterFromDataStore(characterId: Int): CharacterEntity =
        storageService.getCharacter(characterId)

    private suspend fun getCharactersFromDataStore(offset: Int): List<CharacterEntity> =
        storageService.getCharacters(offset)

    private suspend fun saveCharacters(characters: List<CharacterEntity>) =
        when (characters.size == 20) {
            true -> updateCharacters(characters)
            false -> insertCharacters(characters)
        }

    private suspend fun insertCharacters(characters: List<CharacterEntity>) {
        storageService.insertCharacters(characters)
    }

    private suspend fun updateCharacters(characters: List<CharacterEntity>) {
        storageService.updateCharacters(characters)
    }

    private suspend fun toCharacter(characterResponse: CharacterResponse): CharacterModel =
        CharacterModel(
            id = characterResponse.id,
            name = characterResponse.name,
            description = characterResponse.description,
            thumbnail = imageLoader.loadImage(characterResponse.thumbnail.toImageUri())
        )

    private fun toCharacter(characterEntity: CharacterEntity) = CharacterModel(
        id = characterEntity.chId,
        name = characterEntity.name,
        description = characterEntity.description,
        thumbnail = toBitmap(characterEntity.thumbnail)
    )

    private fun toBitmap(image: ByteArray): Bitmap? =
        when (image.isEmpty()) {
            true -> null
            false -> BitmapFactory.decodeByteArray(image, 0, image.size)
        }
}