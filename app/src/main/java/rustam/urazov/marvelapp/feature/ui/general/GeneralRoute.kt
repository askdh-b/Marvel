package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import rustam.urazov.marvelapp.feature.ui.ErrorDialog
import rustam.urazov.marvelapp.feature.ui.ErrorDialogState
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations
import rustam.urazov.marvelapp.feature.ui.theme.Background

@Composable
fun GeneralRoute(
    generalViewModel: GeneralViewModel,
    navController: NavController
) {
    val uiState by generalViewModel.state.collectAsState()
    val dialogState by generalViewModel.dialogState.collectAsState()
    GeneralRoute(
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
    uiState: GeneralUiState,
    dialogState: ErrorDialogState,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (Int) -> Unit,
    onMoreLoad: (Int) -> Unit,
    onErrorDismiss: () -> Unit,
) {
    val generalListLazyListState = rememberLazyListState()

    Surface(color = Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            GeneralRoute(
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
                uiState = uiState,
                charactersLazyListState = charactersLazyListState,
                onChangeVisibleCharacter = onChangeVisibleHero,
                onCharacterClick = onHeroClick,
                onMoreLoad = onMoreLoad
            )
        }
        GeneralUiState.Loading -> GeneralLoadingScreen()
        GeneralUiState.NoCharacters -> GeneralScreenWithoutCharacters()
    }
    ErrorDialog(state = dialogState, onOkClick = onErrorDismiss, onDismiss = onErrorDismiss)
}