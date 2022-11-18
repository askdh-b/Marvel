package rustam.urazov.marvelapp.feature.data.characters

import android.graphics.Bitmap
import rustam.urazov.marvelapp.feature.data.storage.CharacterEntity
import rustam.urazov.marvelapp.feature.ui.general.CharacterView
import java.io.ByteArrayOutputStream

data class CharacterModel(
    val id: Int,
    private val name: String,
    private val description: String,
    private val thumbnail: Bitmap?
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
        thumbnail = toByteArray(thumbnail)
    )

    private fun toByteArray(image: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.JPEG, 100, stream) ?: return ByteArray(0)
        return stream.toByteArray()
    }
}