package rustam.urazov.marvelapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rustam.urazov.marvelapp.data.heroesFeed.HeroesRepository
import rustam.urazov.marvelapp.data.heroesFeed.impl.FakeHeroesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun providesHeroesRepository(heroesRepositoryImpl: FakeHeroesRepositoryImpl): HeroesRepository = heroesRepositoryImpl
}