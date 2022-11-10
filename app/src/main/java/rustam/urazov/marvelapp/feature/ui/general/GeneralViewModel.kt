package rustam.urazov.marvelapp.feature.ui.general

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.extention.empty
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
        loadFeed(offset = 0, isReloading = false)
    }

    fun loadFeed(offset: Int, isReloading: Boolean) {
        val characters = mutableListOf<rustam.urazov.marvelapp.feature.model.Character>()

        if (!isReloading) characters.addAll(viewModelState.value.characters)

        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = heroesRepository.getCharacters(offset)
            viewModelState.update {
                when (result) {
                    is Either.Right -> {
                        characters.addAll(result.b)
                        it.copy(characters = characters, isLoading = false)
                    }
                    is Either.Left -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = when (result.a) {
                                is Failure.NoError -> String.empty()
                                is Failure.ConnectionError -> "Failed to load page. Please try again later"
                                is Failure.ServerError -> result.a.message
                            }
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
            it.copy(
                characters = characters,
                visibleCharacterId = visibleCharacterId,
                isLoading = false
            )
        }
    }

    fun heroDetailsOpen(selectedCharacter: Int) {
        viewModelState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val result = heroesRepository.getCharacterDetails(selectedCharacter)
            viewModelState.update {
                when (result) {
                    is Either.Right -> it.copy(
                        visibleCharacterId = selectedCharacter,
                        isCharacterDetailsOpen = true,
                        isLoading = false
                    )
                    is Either.Left -> {
                        val errorMessages = it.errorMessages + ErrorMessage(
                            id = UUID.randomUUID().mostSignificantBits,
                            message = when (result.a) {
                                is Failure.NoError -> String.empty()
                                is Failure.ConnectionError -> "Failed to load page. Please try again later"
                                is Failure.ServerError -> result.a.message
                            }
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