package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.BaseUiState
import rustam.urazov.marvelapp.feature.model.CharacterView
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

sealed interface GeneralUiState : BaseUiState {

    data class NoCharacters(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState

    data class HasCharacters(
        val characters: List<CharacterView>,
        val visibleCharacter: CharacterView,
        val isCharacterDetailsOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState
}