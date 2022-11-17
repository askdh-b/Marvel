package rustam.urazov.marvelapp.feature.ui.general

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import rustam.urazov.marvelapp.feature.ui.ErrorDialog
import rustam.urazov.marvelapp.feature.ui.theme.Background
import rustam.urazov.marvelapp.feature.utils.RefreshProgressBarState

@Composable
fun GeneralRoute(
    generalViewModel: GeneralViewModel,
) {
    val uiState by generalViewModel.uiState.collectAsState()

    val imageOffset = remember { mutableStateOf(0f) }

    val startAnimation =
        remember { mutableStateOf<RefreshProgressBarState>(RefreshProgressBarState.Up) }

    GeneralRoute(
        uiState = uiState,
        imageOffset = imageOffset,
        startAnimation = startAnimation,
        onChangeVisibleHero = { generalViewModel.changeVisibleHero(it) },
        onHeroClick = { generalViewModel.heroDetailsOpen(it) },
        onBackClick = { generalViewModel.heroDetailsClose() },
        onMoreLoad = { offset, isReloading -> generalViewModel.loadFeed(offset, isReloading) },
        onErrorDismiss = { generalViewModel.errorShown(it) }
    )
}

@Composable
private fun GeneralRoute(
    uiState: GeneralUiState,
    imageOffset: MutableState<Float>,
    startAnimation: MutableState<RefreshProgressBarState>,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onMoreLoad: (Int, Boolean) -> Unit,
    onErrorDismiss: (Long) -> Unit,
) {

    val transition = updateTransition(startAnimation, label = "")
    val infiniteTransition = rememberInfiniteTransition()

    val yOffset = transition.animateFloat(
        transitionSpec = { tween(500) },
        label = ""
    ) {
        when (it.value) {
            is RefreshProgressBarState.Up -> 0f
            is RefreshProgressBarState.Stay -> 60f
            is RefreshProgressBarState.Down -> imageOffset.value
        }
    }

    val alpha = transition.animateFloat(
        transitionSpec = { tween(500) },
        label = ""
    ) {
        when (it.value) {
            is RefreshProgressBarState.Up -> 0f
            is RefreshProgressBarState.Stay -> 1f
            is RefreshProgressBarState.Down -> imageOffset.value / 100
        }
    }

    val rotate = when (startAnimation.value) {
        is RefreshProgressBarState.Stay -> infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = InfiniteRepeatableSpec(
                tween(durationMillis = 1500),
                repeatMode = RepeatMode.Restart
            )
        )
        else -> transition.animateFloat(
            transitionSpec = { tween(500) },
            label = ""
        ) {
            when (it.value) {
                is RefreshProgressBarState.Up -> 0f
                is RefreshProgressBarState.Stay -> 360f
                is RefreshProgressBarState.Down -> imageOffset.value * 1.8f
            }
        }
    }

    val size = transition.animateDp(
        transitionSpec = { tween(500) },
        label = ""
    ) {
        when (it.value) {
            is RefreshProgressBarState.Up -> 20.dp
            is RefreshProgressBarState.Stay -> 60.dp
            is RefreshProgressBarState.Down -> (imageOffset.value / 2.5 + 20).dp
        }
    }

    val generalListLazyListState = rememberLazyListState()

    Surface(color = Background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            startAnimation.value =
                                if (imageOffset.value >= 60) RefreshProgressBarState.Stay
                                else RefreshProgressBarState.Up
                            imageOffset.value = 0f
                        },
                        onVerticalDrag = { change, _ ->
                            startAnimation.value = RefreshProgressBarState.Down
                            val dY = change.position.y - change.previousPosition.y
                            if (imageOffset.value <= 100) imageOffset.value += dY / 8
                        })
                }
        ) {
            GeneralRoute(
                uiState = uiState,
                charactersLazyListState = generalListLazyListState,
                onChangeVisibleHero = onChangeVisibleHero,
                onHeroClick = onHeroClick,
                onBackClick = onBackClick,
                onMoreLoad = onMoreLoad,
                onErrorDismiss = onErrorDismiss,
            )
        }
        RefreshProgressBar(
            size = size.value,
            yOffset = yOffset.value,
            alpha = alpha.value,
            rotate = rotate.value
        )
    }

    if (startAnimation.value is RefreshProgressBarState.Stay) onMoreLoad.invoke(0, true)

    if (!uiState.isLoading) startAnimation.value = RefreshProgressBarState.Up
}

@Composable
private fun GeneralRoute(
    uiState: GeneralUiState,
    charactersLazyListState: LazyListState,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onMoreLoad: (Int, Boolean) -> Unit,
    onErrorDismiss: (Long) -> Unit,
) {
    when (getGeneralScreenType(uiState = uiState)) {
        GeneralScreenTypes.GeneralScreenWithCharacters -> {
            GeneralScreenWithCharacters(
                uiState = uiState,
                charactersLazyListState = charactersLazyListState,
                onChangeVisibleCharacter = onChangeVisibleHero,
                onCharacterClick = onHeroClick,
                onMoreLoad = onMoreLoad
            )
        }
        GeneralScreenTypes.GeneralScreenWithoutCharacters -> {
            GeneralScreenWithoutCharacters()
        }
        GeneralScreenTypes.GeneralLoadingScreen -> GeneralLoadingScreen()
        GeneralScreenTypes.CharacterDetailsScreenWithCharacter -> {
            CharacterDetailsScreen(
                character = (uiState as GeneralUiState.HasCharacters).visibleCharacter,
                onBackClick = onBackClick
            )

            BackHandler { onBackClick.invoke() }
        }
        GeneralScreenTypes.CharacterDetailsScreenWithoutCharacter -> {
            CharacterDetailsScreenWithoutContent(onBackClick)

            BackHandler { onBackClick.invoke() }
        }
        GeneralScreenTypes.CharacterDetailsLoadingScreen -> {
            CharacterDetailsLoadingScreen(onBackClick)

            BackHandler { onBackClick.invoke() }
        }
    }

    if (uiState.errorMessages.isNotEmpty()) ErrorDialog(
        errorMessage = uiState.errorMessages.first(),
        onOkClick = { onErrorDismiss.invoke(uiState.errorMessages.first().id) },
        onDismiss = { onErrorDismiss.invoke(uiState.errorMessages.first().id) })
}

@Composable
private fun RefreshProgressBar(
    size: Dp,
    yOffset: Float,
    alpha: Float,
    rotate: Float
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Icon(
            modifier = Modifier
                .size(size)
                .offset(y = yOffset.dp)
                .alpha(alpha)
                .rotate(rotate)
                .align(Alignment.CenterHorizontally),
            imageVector = Icons.Outlined.Refresh,
            contentDescription = null,
            tint = Color.White,
        )
    }
}

@Composable
private fun getGeneralScreenType(uiState: GeneralUiState): GeneralScreenTypes =
    if (uiState.isLoading && uiState !is GeneralUiState.HasCharacters) GeneralScreenTypes.GeneralLoadingScreen
    else when (uiState) {
        is GeneralUiState.HasCharacters -> {
            if (uiState.isCharacterDetailsOpen) {
                if (uiState.isLoading) GeneralScreenTypes.CharacterDetailsLoadingScreen
                else {
                    if (uiState.visibleCharacter == CharacterView.empty) GeneralScreenTypes.CharacterDetailsScreenWithoutCharacter
                    else GeneralScreenTypes.CharacterDetailsScreenWithCharacter
                }

            } else {
                GeneralScreenTypes.GeneralScreenWithCharacters
            }
        }

        is GeneralUiState.NoCharacters -> {
            GeneralScreenTypes.GeneralScreenWithoutCharacters
        }
    }

private enum class GeneralScreenTypes {
    GeneralScreenWithCharacters,
    GeneralScreenWithoutCharacters,
    GeneralLoadingScreen,
    CharacterDetailsScreenWithCharacter,
    CharacterDetailsScreenWithoutCharacter,
    CharacterDetailsLoadingScreen,
}