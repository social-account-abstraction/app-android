package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import org.web3j.crypto.Hash
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import java.math.BigInteger

class FreezeAccountUseCase(
    private val contractFactory: ContractFactory,
    private val walletRepository: WalletRepository,
): AbstractUseCaseWithoutParams<String>() {

    override fun invoke(): Flow<String> = flow {
        val credentials = walletRepository.getCredentials()

        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val accountAddress = simpleAccountApi.getAccountAddress()

        val accountContract = contractFactory.getSocialRecoveryAccountContract(accountAddress, credentials);

        val data = accountContract.freeze("НАДОЕЛО!!!").encodeFunctionCall()

        val from = simpleAccountApi.getAccountAddress()
        val to = walletRepository.getAbstractionAddress()

        println("FreezeAccountUseCase.FROM: $from")
        println("FreezeAccountUseCase.TO: $to")

        val ethCallResult = contractFactory.web3j.ethCall(
            Transaction.createEthCallTransaction(
                from,
                to,
                data
            ), DefaultBlockParameterName.LATEST
        ).send()

        println("isReverted = " + ethCallResult.isReverted)
        println("revertReason = " + ethCallResult.revertReason)
        println("value = " + ethCallResult.value)

        if (ethCallResult.isReverted) {
            throw IllegalArgumentException(ethCallResult.revertReason)
        }

        val tm = RawTransactionManager(contractFactory.web3j, credentials, 48992)
        val sendTransaction = tm.sendTransaction(
            DefaultGasProvider.GAS_PRICE,
            DefaultGasProvider.GAS_LIMIT,
            to, //?: simpleAccountApi.getAccountAddress(),
            data,
            BigInteger.ZERO,
        )

        if (sendTransaction.error != null) {
            throw IllegalArgumentException(sendTransaction.error.message)
        }

        emit(sendTransaction.transactionHash)
    }
}