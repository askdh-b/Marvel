package rustam.urazov.marvelapp.feature.ui.general

import rustam.urazov.marvelapp.core.platform.UiEvent

sealed class GeneralScreenUiEvent : UiEvent {
    data class AddCharacters(val characters: List<CharacterView>) : GeneralScreenUiEvent()
    data class ChangeVisibleCharacter(val characterId: Int) : GeneralScreenUiEvent()
    object LoadCharacters : GeneralScreenUiEvent()
    object HideCharactersList : GeneralScreenUiEvent()
}