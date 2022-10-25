package rustam.urazov.marvelapp.ui.general

import rustam.urazov.marvelapp.model.HeroesFeed
import rustam.urazov.marvelapp.utils.ErrorMessage

data class GeneralViewModelState(
    val heroesFeed: HeroesFeed? = null,
    val visibleHeroId: String? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {

    fun toUiState(): GeneralUiState =
        if (heroesFeed == null || heroesFeed.heroes.isEmpty()) {
            GeneralUiState.NoHeroes(isLoading, errorMessages)
        } else {
            GeneralUiState.HasHeroes(
                heroesFeed = heroesFeed,
                visibleHero = heroesFeed.heroes.find { it.id == visibleHeroId } ?: heroesFeed.firstHero,
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        }
}