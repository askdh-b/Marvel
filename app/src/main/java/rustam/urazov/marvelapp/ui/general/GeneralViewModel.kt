package rustam.urazov.marvelapp.ui.general

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.data.Either
import rustam.urazov.marvelapp.data.heroesFeed.HeroesRepository
import rustam.urazov.marvelapp.utils.ErrorMessage
import java.util.*
import javax.inject.Inject

@HiltViewModel
class GeneralViewModel @Inject constructor(private val heroesRepository: HeroesRepository) : ViewModel() {

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
            val result = heroesRepository.getHeroFeed()
            viewModelState.update {
                when (result) {
                    is Either.Right -> it.copy(heroesFeed = result.b, isLoading = false)
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

    fun errorShown(errorId: Long) {
        viewModelState.update { currentUiState ->
            val errorMessages = currentUiState.errorMessages.filterNot { it.id == errorId }
            currentUiState.copy(errorMessages = errorMessages)
        }
    }
}