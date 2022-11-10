package rustam.urazov.marvelapp.core.exception

sealed class Failure {
    object NoError : Failure()
    object ConnectionError : Failure()

    data class ServerError(val message: String) : Failure()
}