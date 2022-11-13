package rustam.urazov.marvelapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rustam.urazov.marvelapp.BuildConfig
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.data.heroes.impl.HeroesRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    companion object {
        private const val BASE_URL = "http://gateway.marvel.com/v1/public/"
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(createClient())
        .addConverterFactory(MoshiConverterFactory.create()).build()

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)

            okHttpClientBuilder
                .addInterceptor(loggingInterceptor)
                .addInterceptor { chain ->

                    var request = chain.request()

                    val url = request.url.newBuilder()
                        .addQueryParameter("ts", "3600")
                        .addQueryParameter("apikey", "c65bb38ee1ebf27e89cb7093bcfa6d9c")
                        .addQueryParameter("hash", "8f8345d5e7ad9d9253483df2257c04e0")
                        .build()

                    request = request.newBuilder().url(url).build()

                    chain.proceed(request)
                }
        }

        return okHttpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun providesHeroesRepository(heroesRepositoryImpl: HeroesRepositoryImpl): HeroesRepository =
        heroesRepositoryImpl
}