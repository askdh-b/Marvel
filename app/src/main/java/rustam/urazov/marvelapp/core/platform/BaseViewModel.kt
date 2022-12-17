package rustam.urazov.marvelapp.core.platform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.feature.ui.ErrorDialogEvent
import rustam.urazov.marvelapp.feature.ui.ErrorDialogState

abstract class BaseViewModel<T : UiState, in E : UiEvent> : ViewModel() {

    abstract val state: Flow<T>

    private val reducer = MainReducer(ErrorDialogState.Invisible)

    val dialogState: StateFlow<ErrorDialogState>
        get() = reducer.state

    fun openDialog(failure: Failure) = sendEvent(ErrorDialogEvent.Open(failure))

    fun closeDialog() = sendEvent(ErrorDialogEvent.Close)

    fun sendEvent(event: ErrorDialogEvent) = reducer.sendEvent(event)

    private class MainReducer(initial: ErrorDialogState) :
        Reducer<ErrorDialogState, ErrorDialogEvent>(initial) {

        override fun reduce(oldState: ErrorDialogState, event: ErrorDialogEvent) =
            when (event) {
                ErrorDialogEvent.Close -> setState(ErrorDialogState.Invisible)
                is ErrorDialogEvent.Open -> setState(
                    if (event.failure !is Failure.Error) {
                        ErrorDialogState.Visible(
                            messageId = when (event.failure) {
                                Failure.ConnectionError -> R.string.connection_error
                                Failure.NoDataError -> R.string.no_data_error
                                else -> R.string.unexpected_error
                            }
                        )
                    } else {
                        ErrorDialogState.Visible(message = event.failure.message)
                    }
                )
            }

    }

}