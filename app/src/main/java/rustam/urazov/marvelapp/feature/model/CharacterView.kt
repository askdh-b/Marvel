package rustam.urazov.marvelapp.feature.model

import rustam.urazov.marvelapp.core.extention.empty

data class CharacterView(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val resourceURI: String
) {

    companion object {
        val empty = CharacterView(
            id = -1,
            name = String.empty(),
            description = String.empty(),
            thumbnail = String.empty(),
            resourceURI = String.empty()
        )
    }

}