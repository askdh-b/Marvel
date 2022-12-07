package rustam.urazov.marvelapp.feature.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
)

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "path") val path: String,
    @Json(name = "extension") val extension: String
) {
    fun toImageUri(): String = "${path}.${extension}"
}