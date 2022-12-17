package rustam.urazov.marvelapp.feature.ui.characterDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import rustam.urazov.marvelapp.feature.ui.ErrorDialog
import rustam.urazov.marvelapp.feature.ui.ErrorDialogState
import rustam.urazov.marvelapp.feature.ui.theme.Background

@Composable
fun CharacterDetailsRoute(
    viewModel: CharacterDetailsViewModel,
    characterId: Int,
    navController: NavController,
    isExpanded: Boolean,
    rtl: Boolean
) {
    val uiState by viewModel.state.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    CharacterDetailsRoute(
        isExpanded = isExpanded,
        rtl = rtl,
        uiState = uiState,
        dialogState = dialogState,
        onLoad = { viewModel.load(characterId) },
        onCLose = { navController.navigateUp() },
        onErrorDismiss = { viewModel.closeDialog() }
    )
}

@Composable
fun CharacterDetailsRoute(
    isExpanded: Boolean,
    rtl: Boolean,
    uiState: CharacterDetailsUiState,
    dialogState: ErrorDialogState,
    onLoad: () -> Unit,
    onCLose: () -> Unit,
    onErrorDismiss: () -> Unit,
) {
    Surface(color = Background) {
        Column(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                CharacterDetailsUiState.Closed -> onCLose.invoke()
                is CharacterDetailsUiState.HasData -> CharacterDetailsScreen(isExpanded = isExpanded, rtl = rtl, character = uiState.character) {
                    onCLose.invoke()
                }
                CharacterDetailsUiState.Loading -> CharacterDetailsLoadingScreen(rtl = rtl, onLoad = onLoad) {
                    onCLose.invoke()
                }
                CharacterDetailsUiState.NoData -> CharacterDetailsScreenWithoutContent(rtl = rtl) {
                    onCLose.invoke()
                }
            }
        }
    }
    ErrorDialog(state = dialogState, onOkClick = onErrorDismiss, onDismiss = onErrorDismiss)
}