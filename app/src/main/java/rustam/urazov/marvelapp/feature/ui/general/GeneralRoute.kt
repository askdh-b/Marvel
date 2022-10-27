package rustam.urazov.marvelapp.feature.ui.general

import androidx.activity.compose.BackHandler
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
        onChangeVisibleHero = { generalViewModel.changeVisibleHero(it) },
        onErrorDismiss = { generalViewModel.errorShown(it) },
        onHeroClick = { generalViewModel.heroDetailsOpen(it) },
        onBackClick = { generalViewModel.heroDetailsClose() }
    )
}

@Composable
private fun GeneralRoute(
    uiState: GeneralUiState,
    systemUiController: SystemUiController,
    onChangeVisibleHero: (Int) -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onHeroClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val generalListLazyListState = rememberLazyListState()
    when (getGeneralScreenType(uiState = uiState)) {
        GeneralScreenTypes.GeneralScreenWithHeroesList -> {
            GeneralScreen(
                uiState = uiState,
                systemUiController = systemUiController,
                onChangeVisibleHero = onChangeVisibleHero,
                onErrorDismiss = onErrorDismiss,
                onHeroClick = onHeroClick,
                generalListLazyListState = generalListLazyListState
            )
        }
        GeneralScreenTypes.GeneralScreen -> {
            TODO()
        }
        GeneralScreenTypes.HeroDetailsScreen -> {
            HeroDetailsScreen(
                heroDetails = (uiState as GeneralUiState.HasHeroes).visibleHero.heroDetails,
                onBackClick = onBackClick
            )

            BackHandler { onBackClick.invoke() }
        }
    }
}

@Composable
private fun getGeneralScreenType(uiState: GeneralUiState): GeneralScreenTypes =
    when (uiState) {
        is GeneralUiState.HasHeroes -> {
            if (uiState.isHeroDetailsOpen) {
                GeneralScreenTypes.HeroDetailsScreen
            } else {
                GeneralScreenTypes.GeneralScreenWithHeroesList
            }
        }
        is GeneralUiState.NoHeroes -> {
            GeneralScreenTypes.GeneralScreen
        }
    }

private enum class GeneralScreenTypes {
    GeneralScreenWithHeroesList,
    GeneralScreen,
    HeroDetailsScreen
}