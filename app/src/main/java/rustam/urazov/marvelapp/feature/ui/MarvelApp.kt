package rustam.urazov.marvelapp.feature.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import rustam.urazov.marvelapp.feature.ui.theme.MarvelAppTheme

@Composable
fun MarvelApp(navController: NavHostController) {
    MarvelAppTheme {
        MarvelNavGraph(navController)
    }
}