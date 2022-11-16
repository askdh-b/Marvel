package rustam.urazov.marvelapp.feature.data.heroes

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.feature.model.CharacterModel

interface HeroesRepository {

    suspend fun getCharacterDetails(characterId: Int): Either<Failure, CharacterModel>

    suspend fun getCharacters(offset: Int): Either<Failure, List<CharacterModel>>

}