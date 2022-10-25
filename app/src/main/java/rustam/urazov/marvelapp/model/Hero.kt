package rustam.urazov.marvelapp.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class Hero(
    val id: String,
    val backgroundColor: Color,
    @DrawableRes val imageId: Int,
    @StringRes val nameId: Int
)