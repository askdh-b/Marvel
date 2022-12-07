package rustam.urazov.marvelapp.feature.data.network.fcm

import retrofit2.Response
import retrofit2.Retrofit
import rustam.urazov.marvelapp.core.di.MessagesModule
import javax.inject.Inject
import javax.inject.Named

class MessagesServiceImpl @Inject constructor(@Named(MessagesModule.FCM) private val retrofit: Retrofit) :
    MessagesService {

    private val messagesService by lazy { retrofit.create(MessagesService::class.java) }

    override suspend fun sendMessage(messageBody: MessageBody): Response<MessageResponse> =
        messagesService.sendMessage(messageBody)

}