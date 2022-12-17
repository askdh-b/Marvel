package rustam.urazov.marvelapp.feature.data.network.marvel

import okhttp3.*
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class MarvelInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val marvelChain = MarvelChain(chain)
        return marvelChain.proceed(marvelChain.request())
    }

}

private class MarvelChain(private val delegate: Interceptor.Chain) : Interceptor.Chain {

    companion object {
        private const val PUBLIC_KEY = "c65bb38ee1ebf27e89cb7093bcfa6d9c"
        private const val PRIVATE_KEY = "556faf4b0e52d037de4b43451aa315013763ed40"
        private const val TS = "ts"
        private const val APIKEY = "apikey"
        private const val HASH = "hash"
        private const val MD5 = "MD5"
    }

    override fun call(): Call = delegate.call()

    override fun connectTimeoutMillis(): Int = delegate.connectTimeoutMillis()

    override fun connection(): Connection? = delegate.connection()

    override fun proceed(request: Request): Response = delegate.proceed(request)

    override fun readTimeoutMillis(): Int = delegate.readTimeoutMillis()

    override fun request(): Request = Request.Builder()
        .url(buildUrl(delegate.request().url))
        .build()

    override fun withConnectTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain =
        delegate.withConnectTimeout(timeout, unit)

    override fun withReadTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain =
        delegate.withReadTimeout(timeout, unit)

    override fun withWriteTimeout(timeout: Int, unit: TimeUnit): Interceptor.Chain =
        delegate.withWriteTimeout(timeout, unit)

    override fun writeTimeoutMillis(): Int = delegate.writeTimeoutMillis()

    private fun md5(input: String): String {
        val md = MessageDigest.getInstance(MD5)
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun compose(ts: String): String {
        return "$ts$PRIVATE_KEY$PUBLIC_KEY"
    }

    private fun buildUrl(url: HttpUrl): HttpUrl {
        val ts = (Math.random() * 1000).toInt().toString()
        return url.newBuilder()
            .addQueryParameter(TS, ts)
            .addQueryParameter(APIKEY, PUBLIC_KEY)
            .addQueryParameter(HASH, md5(compose(ts)))
            .build()
    }

}