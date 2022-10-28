package rustam.urazov.marvelapp.feature.data.heroes

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.feature.data.Either
import rustam.urazov.marvelapp.feature.model.HeroDetails
import rustam.urazov.marvelapp.feature.model.HeroesFeed

interface HeroesRepository {

    suspend fun getHeroDetails(hero: Int): Either<Failure, HeroDetails>

    suspend fun getHeroFeed(): Either<Failure, HeroesFeed>
}