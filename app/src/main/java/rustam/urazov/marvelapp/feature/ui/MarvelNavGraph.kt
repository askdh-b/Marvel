package rustam.urazov.marvelapp.feature.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import rustam.urazov.marvelapp.feature.ui.general.GeneralRoute
import rustam.urazov.marvelapp.feature.ui.general.GeneralViewModel

@Composable
fun MarvelNavGraph(
    systemUiController: SystemUiController,
    navController: NavHostController = rememberNavController(),
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
                systemUiController = systemUiController
            )
        }
    }
}