package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.BaseViewModelState
import rustam.urazov.marvelapp.feature.model.HeroesFeed
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

data class GeneralViewModelState(
    val heroesFeed: HeroesFeed? = null,
    val visibleHeroId: Int? = null,
    val isHeroDetailsOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) : BaseViewModelState {

    override fun toUiState(): GeneralUiState =
        if (heroesFeed == null || heroesFeed.heroes.isEmpty()) {
            GeneralUiState.NoHeroes(isLoading, errorMessages)
        } else {
            GeneralUiState.HasHeroes(
                heroesFeed = heroesFeed,
                visibleHero = heroesFeed.heroes.find { it.nameId == visibleHeroId }
                    ?: heroesFeed.firstHero,
                isHeroDetailsOpen = isHeroDetailsOpen,
                isLoading = isLoading,
                errorMessages = errorMessages
            )
        }
}