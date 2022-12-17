package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import rustam.urazov.marvelapp.feature.ui.ErrorDialog
import rustam.urazov.marvelapp.feature.ui.ErrorDialogState
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations

@Composable
fun GeneralRoute(
    generalViewModel: GeneralViewModel,
    navController: NavController,
    isExpanded: Boolean
) {
    val uiState by generalViewModel.state.collectAsState()
    val dialogState by generalViewModel.dialogState.collectAsState()
    GeneralRoute(
        isExpanded = isExpanded,
        uiState = uiState,
        dialogState = dialogState,
        onChangeVisibleHero = { generalViewModel.changeVisibleHero(it) },
        onHeroClick = { navController.navigate("${MarvelDestinations.CHARACTER_DETAILS_ROUTE}/${it}") },
        onMoreLoad = { generalViewModel.load(it) },
        onErrorDismiss = { generalViewModel.closeDialog() }
    )

}

@Composable
private fun GeneralRoute(
    isExpanded: Boolean,
    uiState: GeneralUiState,
    dialogState: ErrorDialogState,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (Int) -> Unit,
    onMoreLoad: (Int) -> Unit,
    onErrorDismiss: () -> Unit,
) {
    val generalListLazyListState = rememberLazyListState()

    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            GeneralRoute(
                isExpanded = isExpanded,
                uiState = uiState,
                dialogState = dialogState,
                charactersLazyListState = generalListLazyListState,
                onChangeVisibleHero = onChangeVisibleHero,
                onHeroClick = onHeroClick,
                onMoreLoad = onMoreLoad,
                onErrorDismiss = onErrorDismiss,
            )
        }
    }
}

@Composable
private fun GeneralRoute(
    isExpanded: Boolean,
    uiState: GeneralUiState,
    dialogState: ErrorDialogState,
    charactersLazyListState: LazyListState,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (Int) -> Unit,
    onMoreLoad: (Int) -> Unit,
    onErrorDismiss: () -> Unit,
) {
    when (uiState) {
        is GeneralUiState.HasCharacters -> {
            GeneralScreenWithCharacters(
                isExpanded = isExpanded,
                characters = uiState.characters,
                charactersLazyListState = charactersLazyListState,
                onChangeVisibleCharacter = onChangeVisibleHero,
                onCharacterClick = onHeroClick,
                onMoreLoad = onMoreLoad
            )
        }
        GeneralUiState.Loading -> GeneralLoadingScreen(isExpanded)
        GeneralUiState.NoCharacters -> GeneralScreenWithoutCharacters(isExpanded)
    }
    ErrorDialog(state = dialogState, onOkClick = onErrorDismiss, onDismiss = onErrorDismiss)
}