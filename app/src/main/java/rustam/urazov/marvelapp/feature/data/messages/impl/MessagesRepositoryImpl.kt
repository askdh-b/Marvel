package rustam.urazov.marvelapp.feature.data.messages.impl

import retrofit2.Response
import rustam.urazov.marvelapp.feature.data.messages.MessagesRepository
import rustam.urazov.marvelapp.feature.data.network.fcm.MessageBody
import rustam.urazov.marvelapp.feature.data.network.fcm.MessageResponse
import rustam.urazov.marvelapp.feature.data.network.fcm.MessagesService
import rustam.urazov.marvelapp.feature.data.network.fcm.Notification
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messagesService: MessagesService
) : MessagesRepository {

    companion object {
        private const val TITLE = "Character"
    }

    override suspend fun sendMessage(token: String, body: String): Response<MessageResponse> =
        messagesService.sendMessage(createMessageBody(token, body))


    private fun createMessageBody(token: String, body: String) =
        MessageBody(token, createNotification(body))

    private fun createNotification(body: String): Notification = Notification(TITLE, body)
}