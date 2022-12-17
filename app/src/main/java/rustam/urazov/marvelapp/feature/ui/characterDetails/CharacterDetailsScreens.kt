package rustam.urazov.marvelapp.feature.ui.characterDetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import rustam.urazov.marvelapp.R
import rustam.urazov.marvelapp.feature.ui.general.CentralProgressIndicator
import rustam.urazov.marvelapp.feature.ui.general.CharacterView
import rustam.urazov.marvelapp.feature.ui.theme.Background
import rustam.urazov.marvelapp.feature.ui.theme.Shapes
import rustam.urazov.marvelapp.feature.ui.theme.Typography

@Composable
fun CharacterDetailsScreen(
    isExpanded: Boolean,
    rtl: Boolean,
    character: CharacterView,
    onBackClick: () -> Unit
) {
    Surface {
        if (isExpanded) ExpandedCharacterDetails(character = character) else CharacterDetails(character)
        ArrowBack(rtl, onBackClick)
    }

    BackHandler { onBackClick.invoke() }
}

@Composable
fun CharacterDetailsLoadingScreen(
    rtl: Boolean,
    onLoad: () -> Unit,
    onBackClick: () -> Unit,
) {
    onLoad.invoke()
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            CentralProgressIndicator()
        }
        ArrowBack(rtl, onBackClick)
    }

    BackHandler { onBackClick.invoke() }
}

@Composable
fun CharacterDetailsScreenWithoutContent(
    rtl: Boolean,
    onBackClick: () -> Unit
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.no_character_details),
                style = Typography.h1,
                color = MaterialTheme.colors.secondary
            )
        }
        ArrowBack(rtl, onBackClick)
    }

    BackHandler { onBackClick.invoke() }
}

@Composable
fun CharacterDetails(character: CharacterView) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = character.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(modifier = Modifier.align(alignment = Alignment.BottomStart)) {
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = character.name,
                style = Typography.h1,
                color = MaterialTheme.colors.primary
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = character.description,
                style = Typography.body1,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
fun ExpandedCharacterDetails(character: CharacterView) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column (modifier = Modifier.fillMaxWidth(0.4f)) {
            Box(modifier = Modifier.fillMaxSize().padding(vertical = 18.dp, horizontal = 36.dp).clip(shape = Shapes.large)) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = character.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }

        }
        Box(modifier = Modifier.fillMaxSize()) {
            Column (modifier = Modifier.align(alignment = Alignment.TopStart)) {
                Text(
                    modifier = Modifier
                        .padding(top = 18.dp, end = 18.dp),
                    text = character.name,
                    style = Typography.h1,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    modifier = Modifier
                        .padding(bottom = 18.dp, end = 18.dp),
                    text = character.description,
                    style = Typography.body1,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
fun ArrowBack(rtl: Boolean, onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(72.dp)
            .padding(top = 24.dp),
        onClick = { onClick.invoke() }
    ) {
        Icon(
            imageVector = if (rtl) Icons.Filled.ArrowForward else Icons.Filled.ArrowBack,
            tint = MaterialTheme.colors.primary,
            contentDescription = null
        )
    }
}