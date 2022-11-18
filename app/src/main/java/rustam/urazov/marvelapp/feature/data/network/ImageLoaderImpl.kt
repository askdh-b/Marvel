package rustam.urazov.marvelapp.feature.data.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoaderImpl @Inject constructor() : ImageLoader {

    override suspend fun loadImage(uri: String): Bitmap? {
        val url = URL(uri)
        val connection: HttpURLConnection?
        return try {
            connection = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpURLConnection
            val bytes = withContext(Dispatchers.IO) {
                connection.connect()
                connection.inputStream.readBytes()
            }
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        } catch (e: Exception) {
            null
        }
    }

}