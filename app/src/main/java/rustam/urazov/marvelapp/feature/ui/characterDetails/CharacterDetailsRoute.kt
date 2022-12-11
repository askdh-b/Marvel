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
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations
import rustam.urazov.marvelapp.feature.ui.theme.Background

@Composable
fun CharacterDetailsRoute(
    viewModel: CharacterDetailsViewModel,
    characterId: Int,
    navController: NavController
) {
    val uiState by viewModel.state.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()
    CharacterDetailsRoute(
        uiState = uiState,
        dialogState = dialogState,
        onLoad = { viewModel.load(characterId) },
        onCLose = { navController.navigate(MarvelDestinations.GENERAL_ROUTE) },
        onErrorDismiss = { viewModel.closeDialog() }
    )
}

@Composable
fun CharacterDetailsRoute(
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
                is CharacterDetailsUiState.HasData -> CharacterDetailsScreen(character = uiState.character) {
                    onCLose.invoke()
                }
                CharacterDetailsUiState.Loading -> CharacterDetailsLoadingScreen(onLoad = onLoad) {
                    onCLose.invoke()
                }
                CharacterDetailsUiState.NoData -> CharacterDetailsScreenWithoutContent {
                    onCLose.invoke()
                }
            }
        }
    }
    ErrorDialog(state = dialogState, onOkClick = onErrorDismiss, onDismiss = onErrorDismiss)
}