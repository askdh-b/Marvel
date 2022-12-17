package rustam.urazov.marvelapp.feature.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations.BASE_ROUTE
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations.CHARACTER_DETAILS_ROUTE
import rustam.urazov.marvelapp.feature.ui.MarvelDestinations.GENERAL_ROUTE
import rustam.urazov.marvelapp.feature.ui.characterDetails.CharacterDetailsRoute
import rustam.urazov.marvelapp.feature.ui.characterDetails.CharacterDetailsViewModel
import rustam.urazov.marvelapp.feature.ui.general.GeneralRoute
import rustam.urazov.marvelapp.feature.ui.general.GeneralViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MarvelNavGraph(
    navController: NavHostController,
    isExpanded: Boolean,
    rtl: Boolean,
    startDestination: String = GENERAL_ROUTE
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(GENERAL_ROUTE) {
            val generalViewModel: GeneralViewModel = hiltViewModel()

            GeneralRoute(
                generalViewModel = generalViewModel,
                navController = navController,
                isExpanded = isExpanded
            )
        }
        composable("${CHARACTER_DETAILS_ROUTE}/{${CharacterDetailsScreenArguments.CHARACTER_ID}}",
            arguments = listOf(navArgument(CharacterDetailsScreenArguments.CHARACTER_ID) {
                type = NavType.IntType
            }),
            deepLinks = listOf(navDeepLink {
                uriPattern =
                    "${BASE_ROUTE}${CHARACTER_DETAILS_ROUTE}/{${CharacterDetailsScreenArguments.CHARACTER_ID}}"
            }),
            enterTransition = {
                when (initialState.destination.route) {
                    GENERAL_ROUTE -> if (rtl) slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    GENERAL_ROUTE -> if (rtl) slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    GENERAL_ROUTE -> if (rtl) slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            },
            popExitTransition = {
                when (targetState.destination.route) {
                    GENERAL_ROUTE -> if (rtl) slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                    else slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                    else -> null
                }
            }
        ) { navBackStackEntry ->
            val characterId =
                navBackStackEntry.arguments?.getInt(CharacterDetailsScreenArguments.CHARACTER_ID)

            val characterDetailsViewModel: CharacterDetailsViewModel = hiltViewModel()

            CharacterDetailsRoute(
                characterId = characterId ?: 0,
                viewModel = characterDetailsViewModel,
                navController = navController,
                isExpanded = isExpanded,
                rtl = rtl
            )
        }
    }
}