package rustam.urazov.marvelapp.feature.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface MessagesService {

    @POST("send")
    fun sendMessage(@Header("Authorization") key: String, @Body messageBody: MessageBody): Call<MessageResponse>
}

class MessagesServiceImpl(private val retrofit: Retrofit) : MessagesService {

    private val messagesService by lazy { retrofit.create(MessagesService::class.java) }

    override fun sendMessage(key: String, messageBody: MessageBody): Call<MessageResponse> =
        messagesService.sendMessage(key, messageBody)

}

@JsonClass(generateAdapter = true)
data class MessageBody(
    @Json val to: String,
    @Json val notification: Notification
)

@JsonClass(generateAdapter = true)
data class Notification(
    @Json val title: String,
    @Json val body: String
)

@JsonClass(generateAdapter = true)
data class MessageResponse(
    @Json val multicast_id: String,
    @Json val success: Int,
    @Json val failure: Int,
    @Json val canonical_ids: Int,
    @Json val results: List<Message>
)

@JsonClass(generateAdapter = true)
data class Message(
    @Json val message_id: String
)