package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.feature.model.CharacterView
import rustam.urazov.marvelapp.feature.utils.ErrorMessage

data class GeneralViewModelState(
    val characters: List<rustam.urazov.marvelapp.feature.model.Character> = emptyList(),
    val visibleCharacterId: Int? = null,
    val isCharacterDetailsOpen: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
) {

    fun toUiState(): GeneralUiState =
        if (characters.isEmpty()) GeneralUiState.NoCharacters(
            isLoading,
            errorMessages
        ) else GeneralUiState.HasCharacters(
            characters = characters.map { it.toCharacterView() },
            visibleCharacter = characters.find { it.id == visibleCharacterId }
                ?.toCharacterView() ?: CharacterView.empty,
            isCharacterDetailsOpen = isCharacterDetailsOpen,
            isLoading = isLoading,
            errorMessages = errorMessages
        )
}