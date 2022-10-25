package rustam.urazov.marvelapp.core.exception

sealed class Failure {
    object DataError : Failure()
}