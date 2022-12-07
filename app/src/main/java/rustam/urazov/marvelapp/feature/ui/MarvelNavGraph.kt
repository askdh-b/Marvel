package rustam.urazov.marvelapp.feature.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import rustam.urazov.marvelapp.feature.ui.characterDetails.CharacterDetailsRoute
import rustam.urazov.marvelapp.feature.ui.characterDetails.CharacterDetailsViewModel
import rustam.urazov.marvelapp.feature.ui.general.GeneralRoute
import rustam.urazov.marvelapp.feature.ui.general.GeneralViewModel

@Composable
fun MarvelNavGraph(
    navController: NavHostController,
    startDestination: String = MarvelDestinations.GENERAL_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MarvelDestinations.GENERAL_ROUTE) {
            val generalViewModel: GeneralViewModel = hiltViewModel()

            GeneralRoute(
                generalViewModel = generalViewModel,
                navController = navController
            )
        }
        composable("${MarvelDestinations.CHARACTER_DETAILS_ROUTE}/{${CharacterDetailsScreenArguments.CHARACTER_ID}}",
            arguments = listOf(navArgument(CharacterDetailsScreenArguments.CHARACTER_ID) {
                type = NavType.IntType
            }),
            deepLinks = listOf( navDeepLink { uriPattern = "marvel://characterDetails/{${CharacterDetailsScreenArguments.CHARACTER_ID}}" })
        ) { navBackStackEntry ->
            val characterId =
                navBackStackEntry.arguments?.getInt(CharacterDetailsScreenArguments.CHARACTER_ID)

            val characterDetailsViewModel: CharacterDetailsViewModel = hiltViewModel()

            CharacterDetailsRoute(
                characterId = characterId ?: 0,
                viewModel = characterDetailsViewModel,
                navController = navController
            )
        }
    }
}