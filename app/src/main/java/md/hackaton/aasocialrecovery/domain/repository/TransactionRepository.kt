package md.hackaton.aasocialrecovery.domain.repository

import md.hackaton.aasocialrecovery.Constants
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.account.TransactionDetailsForUserOp
import md.hackaton.aasocialrecovery.data.remote.model.request.JsonRpcCallRequest
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.exception.TransactionException
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class TransactionRepository(
    private val web3j: Web3j,
    private val rpcService: JsonRpcService,
) {

    fun sendTransaction(credentials: Credentials, to: String, data: String): String? {
        val tm = RawTransactionManager(web3j, credentials, Constants.CHAIN_ID)
        val sendTransaction = tm.sendTransaction(
            DefaultGasProvider.GAS_PRICE,
            DefaultGasProvider.GAS_LIMIT,
            to,
            data,
            BigInteger.ZERO,
        )

        if (sendTransaction.hasError()) {
            val error = sendTransaction.error
            throw TransactionException(error.message, error.code, error.data)
        }

        val txHash = sendTransaction.transactionHash
        println("TransactionRepository.sendTransaction.transactionHash = $txHash")
        return txHash
    }

    suspend fun sendBundlerRequest(credentials: Credentials, abstractionAddress: String?, data: String): String? {

        val simpleAccountApi = SimpleAccountApi(web3j, credentials, abstractionAddress)
        val accountAddress = simpleAccountApi.getAccountAddress()
        val defaultMaxPriorityFeePerGas =  BigInteger.valueOf(2900000000)

        val transactionDetails = TransactionDetailsForUserOp(
            target = accountAddress, // eth wallet owner address
            data = data, // encoded contract method call
            maxPriorityFeePerGas = defaultMaxPriorityFeePerGas,
            maxFeePerGas = defaultMaxPriorityFeePerGas
        )

        val userOp = simpleAccountApi.createSignedUserOp(transactionDetails)

        val requestBody = JsonRpcCallRequest(
            method = "eth_sendUserOperation",
            params = listOf(
                userOp,
                SimpleAccountApi.entryPointAddress
            )
        )

        val response = rpcService.callRpc(requestBody)
        if (response.error != null) {
            val error = response.error
            throw TransactionException(error.message, error.code, data)
        }

        return response.result
    }
}