package rustam.urazov.marvelapp.feature.ui.general

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.core.platform.BaseViewModel
import rustam.urazov.marvelapp.core.platform.Either
import rustam.urazov.marvelapp.core.platform.Reducer
import rustam.urazov.marvelapp.feature.data.characters.CharactersRepository
import rustam.urazov.marvelapp.feature.ui.ErrorDialogEvent
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(private val charactersRepository: CharactersRepository) :
    BaseViewModel<GeneralUiState, GeneralScreenUiEvent>() {

    private val reducer = GeneralReducer(GeneralUiState.Loading)

    override val state: StateFlow<GeneralUiState>
        get() = reducer.state

    init {
        load()
    }

    private fun loadFeed(offset: Int) {
        viewModelScope.launch {
            when (val result = charactersRepository.getCharacters(offset)) {
                is Either.Right -> sendEvent(GeneralScreenUiEvent.AddCharacters(result.b.map { it.toCharacterView() }))
                is Either.Left -> {
                    sendEvent(GeneralScreenUiEvent.HideCharactersList)
                    sendEvent(ErrorDialogEvent.Open(result.a))
                }
            }
        }
    }

    fun changeVisibleHero(visibleCharacterId: Int) =
        sendEvent(GeneralScreenUiEvent.ChangeVisibleCharacter(visibleCharacterId))


    private fun load() {
        sendEvent(GeneralScreenUiEvent.LoadCharacters)
        loadFeed(0)
    }

    fun load(offset: Int) = loadFeed(offset)

    private fun sendEvent(event: GeneralScreenUiEvent) = reducer.sendEvent(event)

    private class GeneralReducer(initial: GeneralUiState) :
        Reducer<GeneralUiState, GeneralScreenUiEvent>(initial) {

        override fun reduce(oldState: GeneralUiState, event: GeneralScreenUiEvent) {
            when (event) {
                GeneralScreenUiEvent.LoadCharacters -> setState(GeneralUiState.Loading)
                is GeneralScreenUiEvent.ChangeVisibleCharacter -> setState(
                    (oldState as GeneralUiState.HasCharacters).copy(
                        visibleCharacter = oldState.characters.find { it.id == event.characterId }
                            ?: oldState.characters.first())
                )
                is GeneralScreenUiEvent.AddCharacters -> {
                    val characters: MutableList<CharacterView> = mutableListOf()
                    if (oldState is GeneralUiState.HasCharacters) characters.addAll(oldState.characters)
                    characters.addAll(event.characters)

                    setState(
                        GeneralUiState.HasCharacters(
                            characters = characters,
                            visibleCharacter = characters.first()
                        )
                    )
                }
                GeneralScreenUiEvent.HideCharactersList -> setState(GeneralUiState.NoCharacters)
            }
        }

    }

}