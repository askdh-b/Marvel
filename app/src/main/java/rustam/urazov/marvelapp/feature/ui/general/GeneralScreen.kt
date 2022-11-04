package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.feature.model.CharacterView
import rustam.urazov.marvelapp.feature.ui.theme.Background
import rustam.urazov.marvelapp.feature.ui.theme.TextColor

@Composable
fun GeneralScreen(
    uiState: GeneralUiState,
    systemUiController: SystemUiController,
    onChangeVisibleHero: (Int) -> Unit,
    onErrorDismiss: (Long) -> Unit,
    onHeroClick: (String) -> Unit,
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
                    characters = uiState.characters,
                    onChangeVisibleHero = onChangeVisibleHero,
                    onHeroClick = onHeroClick,
                    generalListLazyListState = generalListLazyListState
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
    characters: List<CharacterView>,
    onChangeVisibleHero: (Int) -> Unit,
    onHeroClick: (String) -> Unit,
    generalListLazyListState: LazyListState
) {
    val position =
        remember { derivedStateOf { generalListLazyListState.firstVisibleItemIndex } }.value

    systemUiController.apply {
        setStatusBarColor(color = Background)
        setNavigationBarColor(color = Background)
    }

    onChangeVisibleHero.invoke(characters[position].id)

    //Triangle(color = characters[position].backgroundColor)
    GeneralWithHeroesFeed(
        characters = characters,
        generalListLazyListState = generalListLazyListState,
        snapperFlingBehavior = rememberSnapperFlingBehavior(generalListLazyListState),
        onHeroClick = onHeroClick
    )
}

@Composable
fun Triangle(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
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
    characters: List<CharacterView>,
    generalListLazyListState: LazyListState,
    snapperFlingBehavior: FlingBehavior,
    onHeroClick: (String) -> Unit
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
            characters = characters,
            generalListLazyListState = generalListLazyListState,
            snapperFlingBehavior = snapperFlingBehavior,
            onHeroClick = onHeroClick
        )
    }
}

@Composable
fun HeroesFeed(
    characters: List<CharacterView>,
    generalListLazyListState: LazyListState,
    snapperFlingBehavior: FlingBehavior,
    onHeroClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxSize(),
        state = generalListLazyListState,
        flingBehavior = snapperFlingBehavior,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(characters) { character ->
            Hero(
                onHeroClick = onHeroClick,
                imageResource = character.thumbnail,
                text = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .fillParentMaxWidth()
            )
        }
    }
}

@Composable
fun Hero(
    onHeroClick: (String) -> Unit,
    imageResource: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.clickable { onHeroClick.invoke(text) }) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 36.dp)
                .clip(shape = RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop,
            model = imageResource,
            contentDescription = null,
            alignment = Alignment.Center
        )
        Text(
            modifier = Modifier
                .align(alignment = Alignment.BottomStart)
                .padding(start = 36.dp, bottom = 60.dp),
            text = text,
            fontFamily = FontFamily.Monospace,
            color = TextColor,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        )
    }
}