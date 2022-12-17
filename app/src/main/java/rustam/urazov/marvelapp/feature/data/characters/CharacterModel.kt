package rustam.urazov.marvelapp.feature.data.characters

import rustam.urazov.marvelapp.feature.data.storage.marvel.CharacterEntity
import rustam.urazov.marvelapp.feature.ui.general.CharacterView

data class CharacterModel(
    val id: Int,
    private val name: String,
    private val description: String,
    private val thumbnail: String
) {

    fun toCharacterView(): CharacterView = CharacterView(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail
    )

    fun toCharacterEntity(): CharacterEntity = CharacterEntity(
        chId = id,
        name = name,
        description = description,
        thumbnail = thumbnail
    )

}