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
import rustam.urazov.marvelapp.feature.data.storage.CharactersDatabase
import rustam.urazov.marvelapp.feature.data.storage.CharacterEntity
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object {
        private const val BASE_URL = "http://gateway.marvel.com/v1/public/"
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
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

    @Singleton
    @Provides
    fun provideHeroesRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository =
        charactersRepositoryImpl

    @Singleton
    @Provides
    fun provideApiService(service: MarvelService): MarvelApi = service

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CharactersDatabase =
        Room.databaseBuilder(context, CharactersDatabase::class.java, "charactersDb")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCharactersDao(db: CharactersDatabase) = db.getCharacterDao()

    @Provides
    fun provideCharactersEntity() = CharacterEntity()
}