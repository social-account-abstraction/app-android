package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.account.TransactionDetailsForUserOp
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.contract.SocialRecoveryAccount
import md.hackaton.aasocialrecovery.data.remote.model.request.JsonRpcCallRequest
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class RecoverAccountUseCase(
    private val walletRepository: WalletRepository,
    private val rpcService: JsonRpcService,
    private val contractFactory: ContractFactory
): AbstractUseCaseWithParams<RecoverAccountUseCase.Params, JsonRpcCallResponse>() {

    data class Params(
        val currentAgentsHashSet: List<String>,
        val newAgentsHashSet: List<String>
    )

    override fun invoke(p: Params): Flow<JsonRpcCallResponse> = flow {

        val credentials = walletRepository.getCredentials()

        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val accountAddress = simpleAccountApi.getAccountAddress()

        val accountContract = contractFactory.getSocialRecoveryAccountContract(accountAddress, credentials);

        val oneTimeSocialRecoveryAgentsKeys = p.currentAgentsHashSet.map { Numeric.hexStringToByteArray(it)}
        oneTimeSocialRecoveryAgentsKeys.forEach { item ->
            println("oneTimeSocialRecoveryAgentsKeys.item = " + Numeric.toHexString(item))
        }
        val newSocialRecoveryAgents = p.newAgentsHashSet.map { Numeric.hexStringToByteArray(Hash.sha3(it))}
        newSocialRecoveryAgents.forEach { item ->
            println("newSocialRecoveryAgents.item = " + Numeric.toHexString(item))
        }
//        val salt = Numeric.hexStringToByteArray("d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42")


        val data = accountContract.unfreeze(
            oneTimeSocialRecoveryAgentsKeys,
            newSocialRecoveryAgents,
            emptyList(),
            credentials.address // newOwner
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
//
//        val defaultMaxPriorityFeePerGas =  BigInteger.valueOf(2900000000)
//
//        val transactionDetails = TransactionDetailsForUserOp(
//            target = accountContract.contractAddress, // eth wallet owner address
//            data = data, // encoded contract method call
//            maxPriorityFeePerGas = defaultMaxPriorityFeePerGas,
//            maxFeePerGas = defaultMaxPriorityFeePerGas
//        )
//
//        val userOp = simpleAccountApi.createSignedUserOp(transactionDetails)
//
//        val requestBody = JsonRpcCallRequest(
//            method = "eth_sendUserOperation",
//            params = listOf(
//                userOp,
//                SimpleAccountApi.entryPointAddress
//            )
//        )
//
//
//        val response = rpcService.callRpc(requestBody)

        val tm = RawTransactionManager(contractFactory.web3j, credentials, 48992)
        val sendTransaction = tm.sendTransaction(
            DefaultGasProvider.GAS_PRICE,
            DefaultGasProvider.GAS_LIMIT,
            walletRepository.getAbstractionAddress(), //?: simpleAccountApi.getAccountAddress(),
            data,
            BigInteger.ZERO,
        )

        emit(JsonRpcCallResponse(
            "2.0",
            1,
            sendTransaction.transactionHash,
            null
        ))

    }
}