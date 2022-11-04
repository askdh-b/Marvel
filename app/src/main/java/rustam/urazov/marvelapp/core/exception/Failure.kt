package rustam.urazov.marvelapp.core.exception

sealed class Failure {
    object NoError : Failure()
    data class ServerError(val message: String) : Failure()
    object ConnectionError : Failure()
}