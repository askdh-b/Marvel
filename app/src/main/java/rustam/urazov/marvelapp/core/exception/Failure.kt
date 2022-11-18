package rustam.urazov.marvelapp.core.exception

sealed class Failure {
    object NoError : Failure()
    object ConnectionError : Failure()
    object UnexpectedError : Failure()
    object NoDataError : Failure()

    data class Error(val message: String) : Failure()
}