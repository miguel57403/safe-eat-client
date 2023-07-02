package mb.safeEat.functions

fun errorMessageFromThrowable(throwable: Throwable): String {
    return if (throwable is retrofit2.HttpException) {
        "Error: ${throwable.response()?.errorBody()?.string()}"
    } else {
        "Error: ${throwable.message}"
    }
}