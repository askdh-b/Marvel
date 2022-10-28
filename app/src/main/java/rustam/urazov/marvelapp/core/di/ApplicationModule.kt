package rustam.urazov.marvelapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.data.heroes.impl.FakeHeroesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun providesHeroesRepository(heroesRepositoryImpl: FakeHeroesRepositoryImpl): HeroesRepository = heroesRepositoryImpl
}