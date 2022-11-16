package rustam.urazov.marvelapp.feature.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL

@JsonClass(generateAdapter = true)
data class CharactersResponse(@Json(name = "data") val charactersData: CharactersData)

@JsonClass(generateAdapter = true)
data class CharactersData(@Json(name = "results") val result: List<CharacterResponse>)

@JsonClass(generateAdapter = true)
data class CharacterResponse(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "thumbnail") val thumbnail: Thumbnail
) {
    fun toCharacter(): CharacterModel =
        CharacterModel(
            id = id,
            name = name,
            description = description,
            thumbnail = runBlocking(Dispatchers.Default) { downloadImage(thumbnail.toImageUri()) }
        )
}

private suspend fun downloadImage(uri: String): Bitmap? {
    val url = URL(uri)
    val connection: HttpURLConnection?
    return try {
        connection = withContext(Dispatchers.IO) {
            url.openConnection()
        } as HttpURLConnection
        val bytes = withContext(Dispatchers.IO) {
            connection.connect()
            connection.inputStream.readBytes()
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }
}

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "path") val path: String,
    @Json(name = "extension") val extension: String
) {
    fun toImageUri(): String = "${path}.${extension}"
}