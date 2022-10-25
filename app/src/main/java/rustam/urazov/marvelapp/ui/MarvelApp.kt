package rustam.urazov.marvelapp.ui

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import rustam.urazov.marvelapp.ui.theme.MarvelAppTheme

@Composable
fun MarvelApp() {
    MarvelAppTheme {
        val systemUiController = rememberSystemUiController()

        MarvelNavGraph(systemUiController = systemUiController)
    }
}