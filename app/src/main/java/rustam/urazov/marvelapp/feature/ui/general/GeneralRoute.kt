package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import rustam.urazov.marvelapp.feature.ui.ErrorDialog
import rustam.urazov.marvelapp.feature.ui.ErrorDialogState
import rustam.urazov.marvelapp.feature.ui.theme.Background

@Composable
fun GeneralRoute(
    generalViewModel: GeneralViewModel,
) {
    val uiState by generalViewModel.state.collectAsState()
    val dialogState by generalViewModel.dialogState.collectAsState()

    GeneralRoute(
        uiState = uiState,
        dialogState = dialogState,
        onChangeVisibleHero = { generalViewModel.changeVisibleHero(it) },
        onHeroClick = { generalViewModel.characterDetailsOpen(it) },
        onBackClick = { generalViewModel.characterDetailsClose() },
        onLoad = { generalViewModel.load() },
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
    onBackClick: () -> Unit,
    onLoad: () -> Unit,
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
                onBackClick = onBackClick,
                onLoad = onLoad,
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
    onBackClick: () -> Unit,
    onLoad: () -> Unit,
    onMoreLoad: (Int) -> Unit,
    onErrorDismiss: () -> Unit,
) {
    when (uiState) {
        is GeneralUiState.HasCharacters -> {
            when (uiState.characterDetailsUiState) {
                CharacterDetailsUiState.Closed -> GeneralScreenWithCharacters(
                    uiState = uiState,
                    charactersLazyListState = charactersLazyListState,
                    onChangeVisibleCharacter = onChangeVisibleHero,
                    onCharacterClick = onHeroClick,
                    onLoad = onLoad,
                    onMoreLoad = onMoreLoad
                )
                CharacterDetailsUiState.Loading -> CharacterDetailsLoadingScreen(onBackClick)
                CharacterDetailsUiState.NoData -> CharacterDetailsScreenWithoutContent(onBackClick)
                is CharacterDetailsUiState.Open -> CharacterDetailsScreen(character = uiState.visibleCharacter, onBackClick)
            }
        }
        GeneralUiState.Loading -> GeneralLoadingScreen()
        GeneralUiState.NoCharacters -> GeneralScreenWithoutCharacters()
    }
    ErrorDialog(state = dialogState, onOkClick = onErrorDismiss, onDismiss = onErrorDismiss)
}