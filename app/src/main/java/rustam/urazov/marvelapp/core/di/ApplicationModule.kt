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
import rustam.urazov.marvelapp.core.platform.NetworkHandler
import rustam.urazov.marvelapp.core.platform.NetworkHandlerImpl
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.data.characters.impl.CharactersRepositoryImpl
import rustam.urazov.marvelapp.feature.data.network.*
import rustam.urazov.marvelapp.feature.data.storage.CharactersDatabase
import rustam.urazov.marvelapp.feature.data.storage.CharacterEntity
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object {
        private const val BASE_URL = "http://gateway.marvel.com/v1/public/"
        private const val PUBLIC_KEY = "c65bb38ee1ebf27e89cb7093bcfa6d9c"
        private const val PRIVATE_KEY = "556faf4b0e52d037de4b43451aa315013763ed40"
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
                .addInterceptor { chain ->

                    var request = chain.request()
                    val ts = (Math.random() * 1000).toInt().toString()

                    val url = request.url.newBuilder()
                        .addQueryParameter("ts", ts)
                        .addQueryParameter("apikey", PUBLIC_KEY)
                        .addQueryParameter("hash", md5(compose(ts)))
                        .build()

                    request = request.newBuilder().url(url).build()

                    chain.proceed(request)
                }
        }

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideHeroesRepository(charactersRepositoryImpl: CharactersRepositoryImpl): CharactersRepository =
        charactersRepositoryImpl

    @Singleton
    @Provides
    fun provideImageLoader(imageLoader: ImageLoaderImpl): ImageLoader = imageLoader

    @Singleton
    @Provides
    fun provideApiService(service: MarvelService): MarvelApi = service

    @Singleton
    @Provides
    fun provideNetworkHandler(networkHandler: NetworkHandlerImpl): NetworkHandler = networkHandler

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun compose(ts: String): String {
        return "$ts$PRIVATE_KEY$PUBLIC_KEY"
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CharactersDatabase =
        Room.databaseBuilder(context, CharactersDatabase::class.java, "charactersDb")
            .allowMainThreadQueries().fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCharactersDao(db: CharactersDatabase) = db.getCharacterDao()

    @Provides
    fun provideCharactersEntity() = CharacterEntity()
}