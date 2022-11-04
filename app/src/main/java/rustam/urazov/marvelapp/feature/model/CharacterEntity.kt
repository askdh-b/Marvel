package rustam.urazov.marvelapp.feature.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import rustam.urazov.marvelapp.core.extention.empty

@JsonClass(generateAdapter = true)
data class CharactersResponse(
    @Json(name = "data") val charactersData: CharactersData
) {

    companion object {
        val empty = CharactersResponse(charactersData = CharactersData.emptyObject)
        val emptyList = CharactersResponse(charactersData = CharactersData.emptyList)
    }

}

@JsonClass(generateAdapter = true)
data class CharactersData(
    @Json(name = "results") val result: List<CharacterEntity>
) {

    companion object {
        val emptyObject = CharactersData(result = listOf(CharacterEntity.empty))
        val emptyList = CharactersData(result = emptyList())
    }

}

@JsonClass(generateAdapter = true)
data class CharacterEntity(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "thumbnail") val thumbnail: Thumbnail,
    @Json(name = "resourceURI") val resourceURI: String
) {

    companion object {
        val empty = CharacterEntity(
            id = 0,
            name = String.empty(),
            description = String.empty(),
            thumbnail = Thumbnail.empty,
            resourceURI = String.empty()
        )
    }

    fun toCharacter(): Character =
        Character(
            id = id,
            name = name,
            description = description,
            thumbnail = thumbnail.toImageUri(),
            resourceURI = resourceURI
        )
}

@JsonClass(generateAdapter = true)
data class Thumbnail(
    @Json(name = "path") val path: String,
    @Json(name = "extension") val extension: String
) {

    companion object {
        val empty = Thumbnail(path = String.empty(), extension = String.empty())
    }

    fun toImageUri(): String = "${path}.${extension}"
}