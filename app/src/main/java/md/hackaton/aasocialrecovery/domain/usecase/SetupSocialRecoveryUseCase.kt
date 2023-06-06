package md.hackaton.aasocialrecovery.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.account.TransactionDetailsForUserOp
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.data.remote.model.request.JsonRpcCallRequest
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.utils.Bytes32Utils
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Numeric
import java.math.BigInteger

class SetupSocialRecoveryUseCase(
    private val walletRepository: WalletRepository,
    private val rpcService: JsonRpcService,
    private val contractFactory: ContractFactory
): AbstractUseCaseWithParams<SetupSocialRecoveryUseCase.Params, JsonRpcCallResponse>() {

    data class Params(
        val address: List<String>,
        val agentHash: List<String>
    )

    override fun invoke(p: Params): Flow<JsonRpcCallResponse> = flow {
        val credentials = walletRepository.getCredentials()

        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val accountAddress = simpleAccountApi.getAccountAddress()

        val accountContract = contractFactory.getSocialRecoveryAccountContract(accountAddress, credentials);

        val newSocialRecoveryAgents = p.agentHash.map { Numeric.hexStringToByteArray(Hash.sha3(it)) }

        println("newSocialRecoveryAgents = ${newSocialRecoveryAgents.map { Numeric.toHexString(it) }.joinToString { ", " }}")
        val newAlertAgents = p.address

        val data = accountContract.initSocialRecovery(
            newSocialRecoveryAgents,
            newAlertAgents
        ).encodeFunctionCall()

        val ethCallResult = contractFactory.web3j.ethCall(
            Transaction.createEthCallTransaction(
                simpleAccountApi.getAccountAddress(),
                accountContract.contractAddress,
                data
            ), DefaultBlockParameterName.LATEST
        ).send()

        println("isReverted = " + ethCallResult.isReverted)
        println("revertReason = " + ethCallResult.revertReason)
        println("value = " + ethCallResult.value)

        if (ethCallResult.isReverted) {
            throw IllegalArgumentException(ethCallResult.revertReason)
        }

        val defaultMaxPriorityFeePerGas =  BigInteger.valueOf(2900000000)

        val transactionDetails = TransactionDetailsForUserOp(
            target = accountContract.contractAddress, // eth wallet owner address
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
        emit(response)
    }
}