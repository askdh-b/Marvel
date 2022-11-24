package rustam.urazov.marvelapp.feature.ui.general

import android.graphics.Bitmap

data class CharacterView(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Bitmap?
)