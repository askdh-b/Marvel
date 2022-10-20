package rustam.urazov.marvelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import rustam.urazov.marvelapp.ui.theme.*

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSnapperApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelAppTheme {
                val systemUiController = rememberSystemUiController()

                Surface(modifier = Modifier.fillMaxSize()) {
                    val lazyListState = rememberLazyListState()

                    val heroes = arrayOf(
                        R.drawable.spider_man_bg,
                        R.drawable.captain_america_bg,
                        R.drawable.shang_chi_bg,
                        R.drawable.loki_bg,
                        R.drawable.daredevil_bg
                    )

                    val colors = arrayOf(
                        SpiderMan,
                        CaptainAmerica,
                        ShangChi,
                        Loki,
                        Daredevil
                    )

                    val position =
                        remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }.value

                    systemUiController.apply {
                        setStatusBarColor(
                            color = Background
                        )
                        setNavigationBarColor(color = colors[position])
                    }

                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(2f, 2f),
                        painter = painterResource(id = heroes[position]),
                        contentDescription = null
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier.padding(vertical = 48.dp),
                            painter = painterResource(id = R.drawable.marvel_256),
                            contentDescription = null
                        )

                        Text(
                            text = CHOOSE_YOUR_HERO,
                            fontFamily = FontFamily.Monospace,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 36.dp),
                            state = lazyListState,
                            flingBehavior = rememberSnapperFlingBehavior(lazyListState),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            item {
                                HeroesListItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth(),
                                    imageResource = R.drawable.spider_man,
                                    text = "Spider-Man"
                                )
                            }
                            item {
                                HeroesListItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth(),
                                    imageResource = R.drawable.captain_america,
                                    text = "Captain America"
                                )
                            }
                            item {
                                HeroesListItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth(),
                                    imageResource = R.drawable.shang_chi,
                                    text = "Shang-Chi"
                                )
                            }
                            item {
                                HeroesListItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth(),
                                    imageResource = R.drawable.loki,
                                    text = "Loki"
                                )
                            }
                            item {
                                HeroesListItem(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth(),
                                    imageResource = R.drawable.daredevil,
                                    text = "Daredevil"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroesListItem(modifier: Modifier, imageResource: Int, text: String) {
    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = imageResource),
            contentDescription = null,
            alignment = Alignment.Center
        )
        Text(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(start = 36.dp, bottom = 24.dp),
            text = text,
            fontFamily = FontFamily.Monospace,
            color = TextColor,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
    }
}

private const val CHOOSE_YOUR_HERO = "Choose your hero"