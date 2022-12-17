package rustam.urazov.marvelapp.feature.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint
import rustam.urazov.marvelapp.core.platform.NetworkHandler

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavHostController

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val networkHandler = NetworkHandler(applicationContext)
        setContent {
            var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
            val configuration = LocalConfiguration.current
            navController = rememberAnimatedNavController()
            viewModel = hiltViewModel()
            val tokenState by viewModel.tokenState.collectAsState()
            if (networkHandler.isNetworkAvailable()) {
                Firebase.messaging.token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) return@addOnCompleteListener
                    viewModel.saveToken(task.result)
                    viewModel.getToken()
                    Log.e("tag", task.result)
                }
                SendMessage(viewModel, tokenState)
            }

            LaunchedEffect(configuration) {
                snapshotFlow { configuration.orientation }
                    .collect { orientation = it }
            }

            WindowCompat.setDecorFitsSystemWindows(window, false)
            if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    MarvelApp(navController, orientation == Configuration.ORIENTATION_LANDSCAPE, rtl = true)
                }
            } else {
                MarvelApp(navController, orientation == Configuration.ORIENTATION_LANDSCAPE, rtl = false)
            }

            EdgeToEdgeOn()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }

}

@Composable
fun SendMessage(
    viewModel: MainActivityViewModel,
    token: TokenState
) {
    when (token) {
        TokenState.None -> {}
        is TokenState.Token -> viewModel.sendMessage("1011334", token.value)
    }
}

@Composable
fun EdgeToEdgeOn() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = false,
            isNavigationBarContrastEnforced = false,
            transformColorForLightContent = { BlackScrim.compositeOver(it) }
        )
    }
}

private val BlackScrim = Color(0f, 0f, 0f, 0.3f)