package rustam.urazov.marvelapp.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import rustam.urazov.marvelapp.feature.data.messages.MessagesRepository
import rustam.urazov.marvelapp.feature.data.messages.impl.MessagesRepositoryImpl
import rustam.urazov.marvelapp.feature.data.network.fcm.MessagesService
import rustam.urazov.marvelapp.feature.data.network.fcm.MessagesServiceImpl
import rustam.urazov.marvelapp.feature.data.storage.sharedPreferences.MarvelSharedPreferences
import rustam.urazov.marvelapp.feature.data.storage.sharedPreferences.MarvelSharedPreferencesImpl
import rustam.urazov.marvelapp.feature.data.token.TokenRepository
import rustam.urazov.marvelapp.feature.data.token.TokenRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MessagesModule {

    companion object {
        private const val BASE_URL = "https://fcm.googleapis.com/fcm/"
        private const val AUTHORIZATION = "Authorization"
        private const val KEY =
            "key=AAAAknpK93Q:APA91bHpgOZkrdRmUBoW1l_3HMkRXxqeWaXSGVQFEfVQ2O612MT30O6ryRAFj23S5haLtEJLNcTMtelDTtkl4s7Cf8k6mk3-XyvX1zV4jcWVi6hzTBa83IH42I6o2iveBTJL8VAp7z42"
        const val FCM = "fcm"
    }

    @Provides
    @Singleton
    @Named(FCM)
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private fun createClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader(AUTHORIZATION, KEY)
                    .build()
            )
        }.build()

    @Provides
    @Singleton
    fun provideMessagesService(messagesService: MessagesServiceImpl): MessagesService =
        messagesService

    @Provides
    @Singleton
    fun provideMarvelSharedPreferences(marvelSharedPreferences: MarvelSharedPreferencesImpl): MarvelSharedPreferences =
        marvelSharedPreferences

    @Provides
    @Singleton
    fun provideMessagesRepository(messagesRepository: MessagesRepositoryImpl): MessagesRepository =
        messagesRepository

    @Provides
    @Singleton
    fun provideTokenRepository(tokenRepository: TokenRepositoryImpl): TokenRepository =
        tokenRepository

}