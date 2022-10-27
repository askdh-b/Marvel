package rustam.urazov.marvelapp.feature.ui

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import rustam.urazov.marvelapp.feature.ui.theme.MarvelAppTheme

@Composable
fun MarvelApp() {
    MarvelAppTheme {
        val systemUiController = rememberSystemUiController()

        MarvelNavGraph(systemUiController = systemUiController)
    }
}