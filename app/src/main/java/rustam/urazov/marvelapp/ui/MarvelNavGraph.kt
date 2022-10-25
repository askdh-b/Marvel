package rustam.urazov.marvelapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import rustam.urazov.marvelapp.ui.general.GeneralRoute
import rustam.urazov.marvelapp.ui.general.GeneralViewModel

@Composable
fun MarvelNavGraph(
    systemUiController: SystemUiController,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = MarvelDestinations.GENERAL_ROUTE
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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