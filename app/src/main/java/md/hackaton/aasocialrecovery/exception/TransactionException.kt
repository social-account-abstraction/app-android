package md.hackaton.aasocialrecovery.exception

class TransactionException(message: String?, val code: Int = UNKNOWN_ERROR, val data: String? = null): Exception(message) {
    companion object {
        const val UNKNOWN_ERROR: Int = -1
    }
}