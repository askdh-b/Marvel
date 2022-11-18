package rustam.urazov.marvelapp.feature.data.network

import android.graphics.Bitmap
import javax.inject.Singleton

@Singleton
interface ImageLoader {

    suspend fun loadImage(uri: String): Bitmap?

}