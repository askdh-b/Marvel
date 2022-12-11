package rustam.urazov.marvelapp.feature.data.messages

import retrofit2.Response
import rustam.urazov.marvelapp.feature.data.network.fcm.MessageResponse

interface MessagesRepository {

    suspend fun sendMessage(token: String, body: String): Response<MessageResponse>

}