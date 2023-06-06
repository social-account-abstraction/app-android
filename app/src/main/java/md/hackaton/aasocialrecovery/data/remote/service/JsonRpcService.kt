package md.hackaton.aasocialrecovery.data.remote.service

import md.hackaton.aasocialrecovery.data.remote.model.request.JsonRpcCallRequest
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface JsonRpcService {

    @POST("rpc")
    suspend fun callRpc(@Body body: JsonRpcCallRequest): JsonRpcCallResponse
}