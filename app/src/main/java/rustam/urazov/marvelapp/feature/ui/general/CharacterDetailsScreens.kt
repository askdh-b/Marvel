package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import rustam.urazov.marvelapp.R

@Composable
fun CharacterDetailsScreen(
    character: CharacterView,
    onBackClick: () -> Unit
) {
    Surface {
        CharacterDetails(character)
        ArrowBack(onBackClick)
    }
}

@Composable
fun CharacterDetailsLoadingScreen(onBackClick: () -> Unit) {
    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            CentralProgressIndicator()
        }
        ArrowBack(onBackClick)
    }
}

@Composable
fun CharacterDetailsScreenWithoutContent(onBackClick: () -> Unit) {
    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.no_character_details),
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        ArrowBack(onBackClick)
    }
}

@Composable
fun CharacterDetails(characters: CharacterView) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = characters.thumbnail,
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
        Column(modifier = Modifier.align(alignment = Alignment.BottomStart)) {
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = characters.name,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 32.sp,
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = characters.description,
                fontFamily = FontFamily.Monospace,
                color = Color.White,
                fontSize = 24.sp,
            )
        }
    }
}

@Composable
fun ArrowBack(onClick: () -> Unit) {
    IconButton(
        modifier = Modifier
            .size(72.dp)
            .padding(top = 24.dp),
        onClick = { onClick.invoke() }
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            tint = Color.White,
            contentDescription = null
        )
    }
}