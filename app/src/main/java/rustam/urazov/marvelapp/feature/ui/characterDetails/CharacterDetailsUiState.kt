package rustam.urazov.marvelapp.feature.ui.characterDetails

import rustam.urazov.marvelapp.core.platform.UiState
import rustam.urazov.marvelapp.feature.ui.general.CharacterView

sealed interface CharacterDetailsUiState : UiState {
    object Loading : CharacterDetailsUiState
    object Closed : CharacterDetailsUiState
    object NoData : CharacterDetailsUiState
    data class HasData(val character: CharacterView) : CharacterDetailsUiState
}