package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.UiState
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

sealed interface GeneralUiState : UiState {
    object Loading : GeneralUiState
    object NoCharacters : GeneralUiState
    data class HasCharacters(
        val characters: List<CharacterView>,
        val visibleCharacter: CharacterView,
        val characterDetailsUiState: CharacterDetailsUiState
    ) : GeneralUiState
}

sealed interface CharacterDetailsUiState : UiState {
    object Loading : CharacterDetailsUiState
    object Closed : CharacterDetailsUiState
    object NoData : CharacterDetailsUiState
    data class Open(val character: CharacterView) : CharacterDetailsUiState
}