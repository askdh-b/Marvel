package rustam.urazov.marvelapp.core.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.data.characters.impl.CharactersRepositoryImpl
import rustam.urazov.marvelapp.feature.data.network.*
import rustam.urazov.marvelapp.feature.data.network.marvel.MarvelApi
import rustam.urazov.marvelapp.feature.data.network.marvel.MarvelCallAdapterFactory
import rustam.urazov.marvelapp.feature.data.network.marvel.MarvelInterceptor
import rustam.urazov.marvelapp.feature.data.network.marvel.MarvelService
import rustam.urazov.marvelapp.feature.data.storage.marvel.CharactersDatabase
import rustam.urazov.marvelapp.feature.data.storage.marvel.CharacterEntity
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MarvelModule {

    companion object {
        private const val BASE_URL_MARVEL = "http://gateway.marvel.com/v1/public/"
        const val MARVEL = "marvel"
    }

    @Provides
    @Singleton
    @Named(MARVEL)
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_MARVEL)
        .client(createClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(MarvelCallAdapterFactory())
        .build()

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (rustam.urazov.marvelapp.BuildConfig.DEBUG) {

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

            okHttpClientBuilder
                .addInterceptor(loggingInterceptor)
                .addInterceptor(MarvelInterceptor())
        }

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideHeroesRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository =
        charactersRepositoryImpl

    @Provides
    @Singleton
    fun provideApiService(service: MarvelService): MarvelApi = service

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CharactersDatabase =
        Room.databaseBuilder(context, CharactersDatabase::class.java, CharactersDatabase.DB_NAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCharactersDao(db: CharactersDatabase) = db.getCharacterDao()

    @Provides
    fun provideCharactersEntity() = CharacterEntity()

}