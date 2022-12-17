package rustam.urazov.marvelapp.feature.data.network.fcm

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MessagesService {

    @POST("send")
    suspend fun sendMessage(@Body messageBody: MessageBody): Response<MessageResponse>

}