package md.hackaton.aasocialrecovery.account

import java.math.BigInteger

data class TransactionDetailsForUserOp(
    val target: String,
    val data: String,
    val value: BigInteger? = null,
    val gasLimit: BigInteger? = null,
    val maxFeePerGas: BigInteger = BigInteger.valueOf(2900000000),
    val maxPriorityFeePerGas: BigInteger = BigInteger.valueOf(2900000000),
    val nonce: BigInteger? = null,
)