package md.hackaton.aasocialrecovery.account.models

import java.math.BigInteger

data class EncodeUserOpCallDataAndGasLimitResult(
    val callData: String,
    val callGasLimit: BigInteger,
)