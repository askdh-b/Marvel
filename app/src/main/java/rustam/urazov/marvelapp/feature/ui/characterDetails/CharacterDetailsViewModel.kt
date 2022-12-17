package rustam.urazov.marvelapp.feature.ui.characterDetails

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.core.platform.BaseViewModel
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.core.platform.Reducer
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.ui.ErrorDialogEvent
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : BaseViewModel<CharacterDetailsUiState, CharacterDetailsScreenUiEvent>() {

    private val reducer = CharacterDetailsReducer(CharacterDetailsUiState.Loading)
    override val state: StateFlow<CharacterDetailsUiState>
        get() = reducer.state

    fun load(characterId: Int) {
        sendEvent(CharacterDetailsScreenUiEvent.LoadCharacterDetails)
        loadCharacterDetails(characterId)
    }

    private fun loadCharacterDetails(selectedCharacter: Int) {
        viewModelScope.launch {
            when (val result = charactersRepository.getCharacterDetails(selectedCharacter)) {
                is Either.Right -> sendEvent(CharacterDetailsScreenUiEvent.ShowCharacterDetails(result.b.toCharacterView()))
                is Either.Left -> {
                    sendEvent(CharacterDetailsScreenUiEvent.HideCharacterDetails)
                    sendEvent(ErrorDialogEvent.Open(result.a))
                }
            }
        }
    }

    private fun sendEvent(event: CharacterDetailsScreenUiEvent) = reducer.sendEvent(event)

    private class CharacterDetailsReducer(initial: CharacterDetailsUiState) :
        Reducer<CharacterDetailsUiState, CharacterDetailsScreenUiEvent>(initial) {

        override fun reduce(
            oldState: CharacterDetailsUiState,
            event: CharacterDetailsScreenUiEvent
        ) {
            when (event) {
                is CharacterDetailsScreenUiEvent.ShowCharacterDetails -> setState(
                    CharacterDetailsUiState.HasData(event.character)
                )
                CharacterDetailsScreenUiEvent.CloseCharacterDetails -> setState(
                    CharacterDetailsUiState.Closed
                )
                CharacterDetailsScreenUiEvent.LoadCharacterDetails -> setState(
                    CharacterDetailsUiState.Loading
                )
                CharacterDetailsScreenUiEvent.HideCharacterDetails -> setState(
                    CharacterDetailsUiState.NoData
                )
            }
        }

    }

}