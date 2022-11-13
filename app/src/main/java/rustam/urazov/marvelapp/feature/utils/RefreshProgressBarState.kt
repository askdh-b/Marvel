package rustam.urazov.marvelapp.feature.utils

sealed class RefreshProgressBarState {
    object Down: RefreshProgressBarState()
    object Stay: RefreshProgressBarState()
    object Up: RefreshProgressBarState()
}