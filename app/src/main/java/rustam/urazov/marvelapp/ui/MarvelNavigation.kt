package rustam.urazov.marvelapp.ui

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object MarvelDestinations {
    const val GENERAL_ROUTE = "general"
}

class MarvelNavigationActions(private val navController: NavHostController) {
    fun navigateToGeneral() {
        navController.navigate(MarvelDestinations.GENERAL_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}