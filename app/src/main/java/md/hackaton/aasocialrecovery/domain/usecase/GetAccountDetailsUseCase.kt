package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository

class GetAccountDetailsUseCase(
    private val walletRepository: WalletRepository,
    private val contractFactory: ContractFactory
): AbstractUseCaseWithoutParams<GetAccountDetailsUseCase.Result>() {

    data class Result(
        val ownerAddress: String,
        val publicKey: String,
        val abstractionAddress: String,
        val isAbstractionDeployed: Boolean,
    )

    override fun invoke(): Flow<Result> = flow {
        val credentials = walletRepository.getCredentials()
        val simpleAccountApi = SimpleAccountApi(contractFactory.web3j, credentials, walletRepository.getAbstractionAddress())
        val abstractionAddress = simpleAccountApi.getAccountAddress()
        val isDeployed = !simpleAccountApi.checkAccountPhantom()
        val ownerAddress = credentials.address

        emit(Result(
            ownerAddress = ownerAddress,
            publicKey = credentials.ecKeyPair.publicKey.toString(16),
            abstractionAddress = abstractionAddress,
            isAbstractionDeployed = isDeployed
        ))

    }
}