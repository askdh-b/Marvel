package rustam.urazov.marvelapp.feature.data.characters

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either

interface CharactersRepository {

    suspend fun getCharacterDetails(characterId: Int): Either<Failure, CharacterModel>

    suspend fun getCharacters(offset: Int): Either<Failure, List<CharacterModel>>

}