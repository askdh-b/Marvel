package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.BaseUiState
import rustam.urazov.marvelapp.feature.model.Hero
import rustam.urazov.marvelapp.feature.model.HeroesFeed
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

sealed interface GeneralUiState : BaseUiState {

    data class NoHeroes(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState

    data class HasHeroes(
        val heroesFeed: HeroesFeed,
        val visibleHero: Hero,
        val isHeroDetailsOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState
}