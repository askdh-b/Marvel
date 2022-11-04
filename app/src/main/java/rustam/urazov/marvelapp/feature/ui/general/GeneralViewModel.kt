package rustam.urazov.marvelapp.feature.ui.general

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.platform.BaseViewModel
import rustam.urazov.marvelapp.feature.data.Either
import rustam.urazov.marvelapp.feature.data.heroes.HeroesRepository
import rustam.urazov.marvelapp.feature.utils.ErrorMessage
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(private val heroesRepository: HeroesRepository) :
    BaseViewModel() {

    private val viewModelState = MutableStateFlow(GeneralViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    init {
        loadFeed()
    }

    private fun loadFeed() {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = heroesRepository.getCharacters()
            viewModelState.update {
                when (result) {
                    is Either.Right -> it.copy(characters = result.b, isLoading = false)
                    is Either.Left -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    fun changeVisibleHero(visibleCharacterId: Int) {
        viewModelState.update {
            val characters = it.characters
            it.copy(characters = characters, visibleCharacterId = visibleCharacterId, isLoading = false)
        }
    }

    fun heroDetailsOpen(selectedHero: Int) {
        viewModelScope.launch {
            val result = heroesRepository.getCharacterDetails(selectedHero)
            viewModelState.update {
                when (result) {
                    is Either.Right -> it.copy(
                        visibleCharacterId = selectedHero,
                        isCharacterDetailsOpen = true
                    )
                    is Either.Left -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            messageId = R.string.load_error
                        )
                        it.copy(errorMessages = errorMessages, isLoading = false)
                    }
                }
            }
        }
    }

    fun heroDetailsClose() {
        viewModelState.update {
            it.copy(isCharacterDetailsOpen = false)
        }
    }

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}