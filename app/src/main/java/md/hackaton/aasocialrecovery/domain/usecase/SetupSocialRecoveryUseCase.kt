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
import md.hackaton.aasocialrecovery.domain.repository.TransactionRepository
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.utils.Bytes32Utils
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.utils.Numeric
import java.math.BigInteger

class SetupSocialRecoveryUseCase(
    private val walletRepository: WalletRepository,
    private val contractFactory: ContractFactory,
    private val transactionRepository: TransactionRepository,
): AbstractUseCaseWithParams<SetupSocialRecoveryUseCase.Params, String?>() {

    data class Params(
        val address: List<String>,
        val agentHash: List<String>
    )

    override fun invoke(p: Params): Flow<String?> = flow {
        val credentials = walletRepository.getCredentials()

        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val accountAddress = simpleAccountApi.getAccountAddress()
        val accountContract = contractFactory.getSocialRecoveryAccountContract(accountAddress, credentials);
        val newSocialRecoveryAgents = p.agentHash.map { Numeric.hexStringToByteArray(Hash.sha3(it)) }
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

        if (ethCallResult.isReverted) {
            throw IllegalArgumentException(ethCallResult.revertReason)
        }

        val hash = transactionRepository.sendBundlerRequest(credentials, walletRepository.getAbstractionAddress(), data)
        emit(hash)
    }
}