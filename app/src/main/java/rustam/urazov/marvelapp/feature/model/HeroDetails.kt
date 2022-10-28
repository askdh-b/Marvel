package rustam.urazov.marvelapp.feature.model

import androidx.annotation.StringRes

data class HeroDetails(
    @StringRes val imageUrl: Int,
    @StringRes val nameId: Int,
    @StringRes val descId: Int
)