package md.hackaton.aasocialrecovery.data.remote.model.response

data class JsonRpcCallResponse(
    val jsonrpc: String,
    val id: Long,
    val result: String?,
    val error: Error?
) {
    data class Error(val message: String?, val code: Int)
}