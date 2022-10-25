package rustam.urazov.marvelapp.data.heroesFeed.impl

import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.data.Either
import rustam.urazov.marvelapp.data.heroesFeed.HeroesRepository
import rustam.urazov.marvelapp.model.Hero
import rustam.urazov.marvelapp.model.HeroesFeed
import rustam.urazov.marvelapp.ui.theme.*
import java.util.UUID
import javax.inject.Inject

class FakeHeroesRepositoryImpl @Inject constructor() : HeroesRepository {

    override suspend fun getHeroFeed(): Either<Failure, HeroesFeed> = Either.Right(heroesFeed)
}

val spiderMan = Hero(UUID.randomUUID().toString(), SpiderMan, R.drawable.spider_man, R.string.spider_man)
val captainAmerica = Hero(UUID.randomUUID().toString(), CaptainAmerica, R.drawable.captain_america, R.string.captain_america)
val shangChi = Hero(UUID.randomUUID().toString(), ShangChi, R.drawable.shang_chi, R.string.shang_chi)
val loki = Hero(UUID.randomUUID().toString(), Loki, R.drawable.loki, R.string.loki)
val daredevil = Hero(UUID.randomUUID().toString(), Daredevil, R.drawable.daredevil, R.string.daredevil)

val heroes = listOf(spiderMan, captainAmerica, shangChi, loki, daredevil)

val heroesFeed = HeroesFeed(heroes, spiderMan)