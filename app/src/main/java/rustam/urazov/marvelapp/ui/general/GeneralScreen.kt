package rustam.urazov.marvelapp.ui.general

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.model.HeroesFeed
import rustam.urazov.marvelapp.ui.theme.*

@Composable
fun GeneralScreen(
    uiState: GeneralUiState,
    systemUiController: SystemUiController,
    onErrorDismiss: (Long) -> Unit,
    generalListLazyListState: LazyListState
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Background
    ) {
        when (uiState) {
            is GeneralUiState.HasHeroes -> {
                GeneralWithHeroesFeed(
                    systemUiController = systemUiController,
                    heroesFeed = uiState.heroesFeed,
                    generalListLazyListState = generalListLazyListState,
                )
            }
            is GeneralUiState.NoHeroes -> {
                if (uiState.errorMessages.isEmpty()) {
                    TODO()
                } else {
                    TODO()
                }
            }
        }
    }
}

@OptIn(ExperimentalSnapperApi::class)
@Composable
fun GeneralWithHeroesFeed(
    systemUiController: SystemUiController,
    heroesFeed: HeroesFeed,
    generalListLazyListState: LazyListState
) {
    val position =
        remember { derivedStateOf { generalListLazyListState.firstVisibleItemIndex } }.value

    systemUiController.apply {
        setStatusBarColor(color = Background)
        setNavigationBarColor(color = heroesFeed.heroes[position].backgroundColor)
    }

    Triangle(modifier = Modifier, color = heroesFeed.heroes[position].backgroundColor)
    GeneralWithHeroesFeed(
        heroesFeed = heroesFeed,
        generalListLazyListState = generalListLazyListState,
        snapperFlingBehavior = rememberSnapperFlingBehavior(generalListLazyListState)
    )
}

@Composable
fun Triangle(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val trianglePath = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, (size.height * 0.4).toFloat())
            lineTo(size.width, size.height)
            close()
        }
        drawPath(trianglePath, color)
    }
}

@Composable
fun GeneralWithHeroesFeed(
    heroesFeed: HeroesFeed,
    generalListLazyListState: LazyListState,
    snapperFlingBehavior: FlingBehavior
) {
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
            text = stringResource(id = R.string.choose_your_hero),
            fontFamily = FontFamily.Monospace,
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
        HeroesFeed(
            heroesFeed = heroesFeed,
            generalListLazyListState = generalListLazyListState,
            snapperFlingBehavior = snapperFlingBehavior
        )
    }
}

@Composable
fun HeroesFeed(
    heroesFeed: HeroesFeed,
    generalListLazyListState: LazyListState,
    snapperFlingBehavior: FlingBehavior
) {
    LazyRow(
        modifier = Modifier
            .fillMaxSize(),
        state = generalListLazyListState,
        flingBehavior = snapperFlingBehavior,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(heroesFeed.heroes) { hero ->
            Hero(
                modifier = Modifier
                    .fillMaxSize()
                    .fillParentMaxWidth(),
                imageResource = hero.imageId,
                text = stringResource(id = hero.nameId)
            )
        }
    }
}

@Composable
fun Hero(modifier: Modifier, imageResource: Int, text: String) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp,vertical = 36.dp)
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