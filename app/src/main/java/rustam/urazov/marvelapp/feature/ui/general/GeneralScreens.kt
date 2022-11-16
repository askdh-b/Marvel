package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.feature.model.CharacterView
import rustam.urazov.marvelapp.feature.ui.theme.Background
import rustam.urazov.marvelapp.feature.ui.theme.CharacterColors

@Composable
fun GeneralScreenWithCharacters(
    uiState: GeneralUiState,
    charactersLazyListState: LazyListState,
    onChangeVisibleCharacter: (Int) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onMoreLoad: (Int, Boolean) -> Unit,
) {
    check(uiState is GeneralUiState.HasCharacters)

    Column(modifier = Modifier.fillMaxSize()) {
        Logo()
        Content(
            isLoading = uiState.isLoading,
            characters = uiState.characters,
            charactersLazyListState = charactersLazyListState,
            onChangeVisibleCharacter = onChangeVisibleCharacter,
            onCharacterClick = onCharacterClick,
            onMoreLoad = onMoreLoad
        )
    }
}

@Composable
fun GeneralLoadingScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Logo()
        CentralProgressIndicator()
    }
}

@Composable
fun GeneralScreenWithoutCharacters() {
    Column(modifier = Modifier.fillMaxSize()) {
        Logo()
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
private fun Logo() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
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
    }
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
    isLoading: Boolean,
    characters: List<CharacterView>,
    charactersLazyListState: LazyListState,
    onChangeVisibleCharacter: (Int) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onMoreLoad: (Int, Boolean) -> Unit,
) {
    val firstVisibleCharacterIndex =
        remember { derivedStateOf { charactersLazyListState.firstVisibleItemIndex } }.value

    onChangeVisibleCharacter.invoke(characters[firstVisibleCharacterIndex].id)

    if (firstVisibleCharacterIndex == characters.lastIndex && !isLoading) onMoreLoad.invoke(
        firstVisibleCharacterIndex + 1,
        false
    )

    Surface(color = Background) {
        Triangle(color = CharacterColors[firstVisibleCharacterIndex % 5])
        CharactersFeed(
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
            Character(
                characterView = character,
                onCharacterClick = onCharacterClick,
                modifier = Modifier
                    .fillParentMaxSize()
                    .padding(vertical = 36.dp, horizontal = 18.dp)
            )
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
        .clip(shape = RoundedCornerShape(16.dp))
        .clickable { onCharacterClick.invoke(characterView.id) }) {

        if (characterView.thumbnail != null ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.Crop,
                bitmap = characterView.thumbnail.asImageBitmap(),
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 18.dp, bottom = 36.dp),
            text = characterView.name,
            fontFamily = FontFamily.Monospace,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
        )
    }
}