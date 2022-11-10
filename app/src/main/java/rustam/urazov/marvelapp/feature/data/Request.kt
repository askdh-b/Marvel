package rustam.urazov.marvelapp.feature.data

import okhttp3.ResponseBody
import retrofit2.Response
import rustam.urazov.marvelapp.core.exception.Failure

suspend fun <T, R> request(
    call: suspend () -> Response<T>,
    transform: (T) -> R,
    default: T
): Either<Failure, R> = try {

    val response = call.invoke()

    when (response.isSuccessful) {
        true -> Either.Right(transform(response.body() ?: default))
        false -> Either.Left(Failure.ServerError(parseErrorBody(response.errorBody())))
    }

} catch (exception: Throwable) {
    Either.Left(Failure.ServerError(exception.message.orEmpty()))
}

private fun parseErrorBody(responseBody: ResponseBody?): String {
    return responseBody?.string() ?: "Unexpected error"
}