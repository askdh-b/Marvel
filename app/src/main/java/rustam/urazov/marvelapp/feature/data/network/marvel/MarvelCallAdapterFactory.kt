package rustam.urazov.marvelapp.feature.data.network.marvel

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.*
import rustam.urazov.marvelapp.core.exception.Failure
import rustam.urazov.marvelapp.core.platform.Either
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MarvelCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<out Any, Call<Either<Failure, *>>>? {

        if (returnType !is ParameterizedType) {
            return null
        }

        val containerType = getParameterUpperBound(0, returnType)

        if (getRawType(containerType) != Either::class.java)
            return null

        if (containerType !is ParameterizedType) return null

        val errorType = getParameterUpperBound(0, containerType)
        if (getRawType(errorType) != Failure::class.java) return null

        val resultType = getParameterUpperBound(1, containerType)
        return ResultCallAdapter(resultType)

    }
}

private class ResultCallAdapter<R, T>(private val resultType: Type) :
    CallAdapter<R, Call<Either<Failure, T>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<R>): Call<Either<Failure, T>> = ResultCallWrapper(call)
}

@Suppress("UNCHECKED_CAST")
private class ResultCallWrapper<F, T>(
    private val delegate: Call<T>
) : Call<Either<Failure, F>> {

    override fun clone(): Call<Either<Failure, F>> = ResultCallWrapper(delegate.clone())

    override fun execute(): Response<Either<Failure, F>> = wrapResponse(delegate.execute())

    override fun enqueue(callback: Callback<Either<Failure, F>>) = try {
        delegate.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) = callback.onResponse(this@ResultCallWrapper, wrapResponse(response))

            override fun onFailure(call: Call<T>, t: Throwable) =
                callback.onResponse(
                    this@ResultCallWrapper,
                    Response.success(Either.Left(Failure.Error(t.message.orEmpty())) as Either<Failure, F>)
                )
        })
    } catch (e: Exception) {
        callback.onResponse(
            this@ResultCallWrapper,
            Response.success(Either.Left(Failure.UnexpectedError) as Either<Failure, F>)
        )
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

    private fun wrapResponse(response: Response<T>): Response<Either<Failure, F>> = try {
        when (response.isSuccessful) {
            true -> {
                val responseBody = response.body()
                when (responseBody != null) {
                    true -> Response.success(Either.Right(responseBody) as Either<Failure, F>)
                    false -> Response.success(Either.Left(Failure.NoDataError) as Either<Failure, F>)
                }
            }
            false -> {
                val failure = parseErrorBody(response.errorBody())
                when (failure != null) {
                    true -> Response.success(Either.Left(Failure.Error(failure)))
                    false -> Response.success(Either.Left(Failure.UnexpectedError))
                }
            }
        }
    } catch (e: Exception) {
        Response.success(Either.Left(Failure.UnexpectedError) as Either<Failure, F>)
    }

    private fun parseErrorBody(responseBody: ResponseBody?): String? =
        responseBody?.string()
}