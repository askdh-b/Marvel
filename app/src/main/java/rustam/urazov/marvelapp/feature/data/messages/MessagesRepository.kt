package rustam.urazov.marvelapp.feature.data.messages

interface MessageRepository {

    suspend fun sendMessage()

}