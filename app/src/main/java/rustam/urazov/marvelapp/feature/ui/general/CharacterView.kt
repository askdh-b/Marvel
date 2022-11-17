package rustam.urazov.marvelapp.feature.ui.general

import android.graphics.Bitmap
import rustam.urazov.marvelapp.core.extention.empty

data class CharacterView(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Bitmap?
) {

    companion object {
        val empty = CharacterView(
            id = -1,
            name = String.empty(),
            description = String.empty(),
            thumbnail = null
        )
    }

}