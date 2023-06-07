package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.Constants
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.account.TransactionDetailsForUserOp
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.contract.SocialRecoveryAccount
import md.hackaton.aasocialrecovery.data.remote.model.request.JsonRpcCallRequest
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.TransactionRepository
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.exception.TransactionException
import okhttp3.internal.parseCookie
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class RecoverAccountUseCase(
    private val walletRepository: WalletRepository,
    private val contractFactory: ContractFactory,
    private val transactionRepository: TransactionRepository
): AbstractUseCaseWithParams<RecoverAccountUseCase.Params, String?>() {

    data class Params(
        val currentAgentsHashSet: List<String>,
        val newAgentsHashSet: List<String>
    )

    override fun invoke(p: Params): Flow<String?> = flow {

        val credentials = walletRepository.getCredentials()

        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val accountAddress = simpleAccountApi.getAccountAddress()

        val accountContract = contractFactory.getSocialRecoveryAccountContract(accountAddress, credentials);

        val oneTimeSocialRecoveryAgentsKeys = p.currentAgentsHashSet.map { Numeric.hexStringToByteArray(it)}
        val newSocialRecoveryAgents = p.newAgentsHashSet.map { Numeric.hexStringToByteArray(Hash.sha3(it))}

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

        if (ethCallResult.isReverted) {
            throw IllegalArgumentException(ethCallResult.revertReason)
        }

        val hash = transactionRepository.sendTransaction(credentials, walletRepository.getAbstractionAddress(), data)
        emit(hash)
    }
}