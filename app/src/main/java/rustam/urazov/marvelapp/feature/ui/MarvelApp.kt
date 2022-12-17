package rustam.urazov.marvelapp.feature.ui

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import rustam.urazov.marvelapp.feature.ui.theme.MarvelAppTheme

@Composable
fun MarvelApp(navController: NavHostController, isExpanded: Boolean, rtl: Boolean) {
    MarvelAppTheme {
        MarvelNavGraph(navController, isExpanded, rtl)
    }
}