package rustam.urazov.marvelapp.feature.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rustam.urazov.marvelapp.feature.data.messages.MessagesRepository
import rustam.urazov.marvelapp.feature.data.token.TokenRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor (
    private val messagesRepository: MessagesRepository,
    private val tokenRepository: TokenRepository
) : ViewModel() {

    fun sendMessage(message: String) = viewModelScope.launch {
        messagesRepository.sendMessage(getToken(), message).body()
    }

    private fun getToken(): String = tokenRepository.getToken()

    fun saveToken(token: String) {
        tokenRepository.saveToken(token)
    }

}