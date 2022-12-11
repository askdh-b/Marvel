package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.UiState

sealed interface GeneralUiState : UiState {
    object Loading : GeneralUiState
    object NoCharacters : GeneralUiState
    data class HasCharacters(
        val characters: List<CharacterView>,
        val visibleCharacter: CharacterView
    ) : GeneralUiState
}