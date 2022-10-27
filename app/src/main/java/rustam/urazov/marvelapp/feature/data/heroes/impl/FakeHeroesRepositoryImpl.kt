package rustam.urazov.marvelapp.feature.data.heroes.impl

import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.feature.data.Either
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.model.Hero
import rustam.urazov.marvelapp.feature.model.HeroDetails
import rustam.urazov.marvelapp.feature.model.HeroesFeed
import rustam.urazov.marvelapp.feature.ui.theme.*
import javax.inject.Inject

class FakeHeroesRepositoryImpl @Inject constructor() : HeroesRepository {

    override suspend fun getHeroDetails(hero: Int): Either<Failure, HeroDetails> = Either.Right(
        heroesFeed.heroes.find { it.nameId == hero }?.heroDetails ?: spiderManDetails
    )

    override suspend fun getHeroFeed(): Either<Failure, HeroesFeed> = Either.Right(heroesFeed)
}

val spiderManDetails =
    HeroDetails(R.string.spider_man_image, R.string.spider_man, R.string.spider_man_desc)
val captainAmericaDetails = HeroDetails(
    R.string.captain_america_image,
    R.string.captain_america,
    R.string.captain_america_desc
)
val shangChiDetails =
    HeroDetails(R.string.shang_chi_image, R.string.shang_chi, R.string.shang_chi_desc)
val lokiDetails = HeroDetails(R.string.loki_image, R.string.loki, R.string.loki_desc)
val daredevilDetails =
    HeroDetails(R.string.daredevil_image, R.string.daredevil, R.string.daredevil_desc)

val spiderMan = Hero(
    SpiderMan,
    spiderManDetails,
    R.drawable.spider_man,
    R.string.spider_man
)
val captainAmerica = Hero(
    CaptainAmerica,
    captainAmericaDetails,
    R.drawable.captain_america,
    R.string.captain_america
)
val shangChi = Hero(
    ShangChi,
    shangChiDetails,
    R.drawable.shang_chi,
    R.string.shang_chi
)
val loki = Hero(Loki, lokiDetails, R.drawable.loki, R.string.loki)
val daredevil = Hero(
    Daredevil,
    daredevilDetails,
    R.drawable.daredevil,
    R.string.daredevil
)

val heroes = listOf(spiderMan, captainAmerica, shangChi, loki, daredevil)

val heroesFeed = HeroesFeed(heroes, spiderMan)