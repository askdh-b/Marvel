package rustam.urazov.marvelapp.data.heroesFeed

import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.data.Either
import rustam.urazov.marvelapp.model.HeroesFeed

interface HeroesRepository {

    suspend fun getHeroFeed(): Either<Failure, HeroesFeed>
}