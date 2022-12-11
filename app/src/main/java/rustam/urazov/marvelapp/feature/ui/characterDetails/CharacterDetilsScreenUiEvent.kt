package rustam.urazov.marvelapp.feature.ui.characterDetails

import rustam.urazov.marvelapp.core.platform.UiEvent
import rustam.urazov.marvelapp.feature.ui.general.CharacterView

sealed class CharacterDetailsScreenUiEvent : UiEvent {
    object LoadCharacterDetails : CharacterDetailsScreenUiEvent()
    object CloseCharacterDetails : CharacterDetailsScreenUiEvent()
    object HideCharacterDetails : CharacterDetailsScreenUiEvent()
    data class ShowCharacterDetails(val character: CharacterView) : CharacterDetailsScreenUiEvent()
}