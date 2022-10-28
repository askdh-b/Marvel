package rustam.urazov.marvelapp.feature.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class Hero(
    val backgroundColor: Color,
    val heroDetails: HeroDetails,
    @DrawableRes val imageId: Int,
    @StringRes val nameId: Int
)