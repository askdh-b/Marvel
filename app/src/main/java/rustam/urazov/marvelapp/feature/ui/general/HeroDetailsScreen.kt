package rustam.urazov.marvelapp.feature.ui.general

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import rustam.urazov.marvelapp.feature.model.HeroDetails
import rustam.urazov.marvelapp.feature.ui.theme.Background
import rustam.urazov.marvelapp.feature.ui.theme.TextColor

@Composable
fun HeroDetailsScreen(
    heroDetails: HeroDetails,
    onBackClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Background) {
        HeroDetails(heroDetails)
        NavigationBar(containerColor = Color(0x00000000)) {
            IconButton(onClick = { onBackClick.invoke() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = TextColor
                )
            }
        }
    }
}

@Composable
fun HeroDetails(heroDetails: HeroDetails) {
    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = stringResource(id = heroDetails.imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.align(alignment = Alignment.BottomStart)) {
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = stringResource(id = heroDetails.nameId),
                fontFamily = FontFamily.Monospace,
                color = TextColor,
                fontSize = 32.sp
            )
            Text(
                modifier = Modifier
                    .padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
                text = stringResource(id = heroDetails.descId),
                fontFamily = FontFamily.Monospace,
                color = TextColor,
                fontSize = 24.sp
            )
        }
    }
}