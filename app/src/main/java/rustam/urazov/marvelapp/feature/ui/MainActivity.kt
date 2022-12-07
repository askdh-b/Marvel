package rustam.urazov.marvelapp.feature.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navController = rememberNavController()
            viewModel = hiltViewModel()
            viewModel.sendMessage("1011334")
            WindowCompat.setDecorFitsSystemWindows(window, false)
            EdgeToEdgeOn()
            MarvelApp(navController)
            Firebase.messaging.token.addOnCompleteListener { task ->
                if (!task.isSuccessful) return@addOnCompleteListener
                viewModel.saveToken(task.result)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
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