package rustam.urazov.marvelapp.core.platform

import rustam.urazov.marvelapp.feature.utils.ErrorMessage

interface BaseUiState {

    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

}