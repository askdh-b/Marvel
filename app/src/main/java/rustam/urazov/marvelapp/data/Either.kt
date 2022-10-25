package rustam.urazov.marvelapp.data

sealed class Either<out L, out R> {

    data class Left<out L>(val a: L) : Either<L, Nothing>()
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)

    fun <R> right(b: R) = Right(b)

    fun fold(fL: (L) -> Any, fR: (R) -> Any): Any =
        when (this) {
            is Left -> fL(a)
            is Right -> fR(b)
        }
}

fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatmap(f: (R) -> Either<L, T>): Either<L, T> = when (this) {
    is Either.Left -> Either.Left(a)
    is Either.Right -> f(b)
}

fun <T, L, R> Either<L, R>.map(f: (R) -> (T)): Either<L, T> = this.flatmap(f.compose(::right))

fun <L, R> Either<L, R>.onFailure(f: (failure: L) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Left) f(a) }

fun <L, R> Either<L, R>.onSuccess(f: (success: R) -> Unit): Either<L, R> =
    this.apply { if (this is Either.Right) f(b) }