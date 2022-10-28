package rustam.urazov.marvelapp.core.platform

interface BaseViewModelState {

    fun toUiState(): BaseUiState
}