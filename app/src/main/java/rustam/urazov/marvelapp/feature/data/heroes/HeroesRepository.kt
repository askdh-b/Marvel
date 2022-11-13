package rustam.urazov.marvelapp.feature.data.heroes

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.feature.data.Either

interface HeroesRepository {

    suspend fun getCharacterDetails(characterId: Int): Either<Failure, rustam.urazov.marvelapp.feature.model.Character>

    suspend fun getCharacters(offset: Int): Either<Failure, List<rustam.urazov.marvelapp.feature.model.Character>>
}