package md.hackaton.aasocialrecovery.data.remote.model.request

import kotlin.random.Random

data class JsonRpcCallRequest(
    val params: List<Any>,
    val method: String,
    val jsonrpc: String = "2.0",
    val id: Long = Random.nextLong()
) {
}