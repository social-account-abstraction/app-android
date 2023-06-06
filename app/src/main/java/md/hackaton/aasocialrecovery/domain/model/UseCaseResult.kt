package md.hackaton.aasocialrecovery.domain.model

sealed class UseCaseResult {
    data class Success<T>(val data: T): UseCaseResult()
    data class Failure(val error: Exception): UseCaseResult()
    object Pending: UseCaseResult()

    companion object {
        fun <T> success(data: T): Success<T> {
            return Success(data)
        }

        fun fail(error: Exception): Failure {
            return Failure(error)
        }

        fun pending() = Pending
    }
}