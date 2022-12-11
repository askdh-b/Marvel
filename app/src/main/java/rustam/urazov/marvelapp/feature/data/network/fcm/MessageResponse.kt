package rustam.urazov.marvelapp.feature.data.network.fcm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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