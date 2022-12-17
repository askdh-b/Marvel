package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
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
import kotlinx.coroutines.*
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.feature.ui.theme.CharacterColors
import rustam.urazov.marvelapp.feature.ui.theme.Shapes
import rustam.urazov.marvelapp.feature.ui.theme.Typography

@Composable
fun GeneralScreenWithCharacters(
    isExpanded: Boolean,
    characters: List<CharacterView>,
    charactersLazyListState: LazyListState,
    onChangeVisibleCharacter: (Int) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onMoreLoad: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Logo(isExpanded)
        Content(
            isExpanded = isExpanded,
            characters = characters,
            charactersLazyListState = charactersLazyListState,
            onChangeVisibleCharacter = onChangeVisibleCharacter,
            onCharacterClick = onCharacterClick,
            onMoreLoad = onMoreLoad
        )
    }
}

@Composable
fun GeneralLoadingScreen(isExpanded: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        Logo(isExpanded)
        CentralProgressIndicator()
    }
}

@Composable
fun GeneralScreenWithoutCharacters(isExpanded: Boolean) {
    Column(modifier = Modifier.fillMaxSize()) {
        Logo(isExpanded)
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Center),
                text = stringResource(id = R.string.no_characters),
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun Logo(isExpanded: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (isExpanded) {
            true -> ExpandedLogo()
            false -> DefaultLogo()
        }
        Text(
            text = stringResource(id = R.string.choose_your_hero),
            style = Typography.h1,
            color = MaterialTheme.colors.secondary,
        )
    }
}

@Composable
private fun DefaultLogo() {
    Image(
        modifier = Modifier.padding(vertical = 48.dp),
        painter = painterResource(id = R.drawable.marvel_256),
        contentDescription = null
    )
}

@Composable
private fun ExpandedLogo() {
    Image(
        modifier = Modifier.padding(vertical = 12.dp),
        painter = painterResource(id = R.drawable.marvel_256),
        contentDescription = null
    )
}

@Composable
fun CentralProgressIndicator() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Center),
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Content(
    isExpanded: Boolean,
    characters: List<CharacterView>,
    charactersLazyListState: LazyListState,
    onChangeVisibleCharacter: (Int) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onMoreLoad: (Int) -> Unit,
) {

    val firstVisibleCharacterIndex by
    remember { derivedStateOf { charactersLazyListState.firstVisibleItemIndex } }
    val visibleCharactersCount by remember { derivedStateOf { charactersLazyListState.layoutInfo.visibleItemsInfo.size } }

    onChangeVisibleCharacter.invoke(characters[firstVisibleCharacterIndex].id)

    if (firstVisibleCharacterIndex + visibleCharactersCount - 1 == characters.lastIndex) onMoreLoad.invoke(
        firstVisibleCharacterIndex + visibleCharactersCount
    )

    Surface(color = Color.Transparent) {
        Triangle(color = CharacterColors[firstVisibleCharacterIndex % 5])
        CharactersFeed(
            isExpanded = isExpanded,
            characters = characters,
            charactersLazyListState = charactersLazyListState,
            snapperFlingBehavior = rememberSnapFlingBehavior(lazyListState = charactersLazyListState),
            onCharacterClick = onCharacterClick
        )
    }
}

@Composable
fun Triangle(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val trianglePath = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, (size.height * 0.2).toFloat())
            lineTo(size.width, size.height)
            close()
        }
        drawPath(trianglePath, color)
    }
}

@Composable
fun CharactersFeed(
    isExpanded: Boolean,
    characters: List<CharacterView>,
    charactersLazyListState: LazyListState,
    snapperFlingBehavior: FlingBehavior,
    onCharacterClick: (Int) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxSize(),
        state = charactersLazyListState,
        flingBehavior = snapperFlingBehavior
    ) {
        items(characters) { character ->
            when (isExpanded) {
                true -> ExpandedCharacter(
                    characterView = character,
                    onCharacterClick = onCharacterClick,
                    modifier = Modifier
                        .fillParentMaxWidth(0.3f)
                        .padding(vertical = 36.dp, horizontal = 18.dp)
                )
                false -> Character(
                    characterView = character,
                    onCharacterClick = onCharacterClick,
                    modifier = Modifier
                        .fillParentMaxSize()
                        .padding(vertical = 36.dp, horizontal = 18.dp)
                )
            }
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(60.dp)
            ) {
                CentralProgressIndicator()
            }
        }
    }
}

@Composable
fun Character(
    characterView: CharacterView,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .clip(shape = Shapes.large)
        .clickable { onCharacterClick.invoke(characterView.id) }) {

        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = characterView.thumbnail,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, bottom = 36.dp),
            text = characterView.name,
            style = Typography.h1,
            color = MaterialTheme.colors.primary,
        )
    }
}

@Composable
private fun ExpandedCharacter(
    characterView: CharacterView,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier
        .fillMaxHeight()
        .clip(shape = Shapes.large)
        .clickable { onCharacterClick.invoke(characterView.id) }) {

        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            model = characterView.thumbnail,
            contentDescription = null
        )
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 9.dp, bottom = 18.dp),
            text = characterView.name,
            style = Typography.body1,
            color = MaterialTheme.colors.primary
        )
    }
}