package rustam.urazov.marvelapp.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.feature.data.messages.MessagesRepository
import rustam.urazov.marvelapp.feature.data.token.TokenRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val tokenMutableState = MutableStateFlow<TokenState>(TokenState.None)
    val tokenState: StateFlow<TokenState> = tokenMutableState.asStateFlow()

    fun sendMessage(message: String, token: String) = viewModelScope.launch {
        if (token.isNotEmpty()) messagesRepository.sendMessage(token, message).body()
    }

    fun getToken() {
        val token = tokenRepository.getToken()
        when (token.isNotEmpty()) {
            true -> tokenMutableState.tryEmit(TokenState.Token(token))
            false -> tokenMutableState.tryEmit(TokenState.None)
        }
    }

    fun saveToken(token: String) {
        tokenRepository.saveToken(token)
    }

}

sealed class TokenState {
    object None : TokenState()
    data class Token(val value: String) : TokenState()
}