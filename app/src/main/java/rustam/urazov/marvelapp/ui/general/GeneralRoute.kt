package rustam.urazov.marvelapp.ui.general

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.systemuicontroller.SystemUiController

@Composable
fun GeneralRoute(
    generalViewModel: GeneralViewModel,
    systemUiController: SystemUiController
) {
    val uiState by generalViewModel.uiState.collectAsState()

    GeneralRoute(
        uiState = uiState,
        systemUiController = systemUiController,
        onErrorDismiss = { generalViewModel.errorShown(it) }
    )
}

@Composable
private fun GeneralRoute(
    uiState: GeneralUiState,
    systemUiController: SystemUiController,
    onErrorDismiss: (Long) -> Unit
) {
    val generalListLazyState = rememberLazyListState()
    GeneralScreen(
        uiState = uiState,
        systemUiController = systemUiController,
        onErrorDismiss = onErrorDismiss,
        generalListLazyListState = generalListLazyState
    )
}