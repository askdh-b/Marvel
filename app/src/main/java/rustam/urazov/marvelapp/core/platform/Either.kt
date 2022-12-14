package rustam.urazov.marvelapp.core.platform

sealed class Either<out L, out R> {

    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)

    fun <R> right(b: R) = Right(b)
}

fun <L, R, Ro> Either<L, R>.map(
    success: (R) -> Ro
): Either<L, Ro> = when (this) {
    is Either.Left -> Either.Left(this.a)
    is Either.Right -> Either.Right(success(this.b))
}