package md.hackaton.aasocialrecovery.account

import com.google.gson.Gson

data class UserOperationStructure(
    val sender: String,
    val nonce: String,
    val initCode: String,
    val callData: String,
    val callGasLimit: String,
    val verificationGasLimit: String,
    val maxFeePerGas: String,
    val maxPriorityFeePerGas: String,
    val paymasterAndData: String,
    val preVerificationGas: String?,
    val signature: String?,
) {

    override fun toString(): String {
        return "UserOperationStructure="+Gson().toJson(this)
    }
}