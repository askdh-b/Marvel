package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.BaseUiState
import rustam.urazov.marvelapp.feature.model.CharacterView
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

sealed interface GeneralUiState : BaseUiState {

    data class NoHeroes(
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState

    data class HasHeroes(
        val characters: List<CharacterView>,
        val visibleCharacter: CharacterView,
        val isCharacterDetailsOpen: Boolean,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage>
    ) : GeneralUiState
}