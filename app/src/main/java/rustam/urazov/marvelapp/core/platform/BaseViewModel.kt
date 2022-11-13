package rustam.urazov.marvelapp.core.platform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import rustam.urazov.marvelapp.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    private val mutableFailure = MutableStateFlow<Failure>(Failure.NoError)
    val failure: StateFlow<Failure> = mutableFailure.asStateFlow()

    fun handleFailure(failure: Failure) {
        mutableFailure.value = failure
    }
}