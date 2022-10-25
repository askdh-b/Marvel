package rustam.urazov.marvelapp.ui.general

import rustam.urazov.marvelapp.model.Hero
import rustam.urazov.marvelapp.model.HeroesFeed
import rustam.urazov.marvelapp.utils.ErrorMessage

sealed interface GeneralUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoHeroes(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>,
    ) : GeneralUiState

    data class HasHeroes(
        val heroesFeed: HeroesFeed,
        val visibleHero: Hero,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState
}