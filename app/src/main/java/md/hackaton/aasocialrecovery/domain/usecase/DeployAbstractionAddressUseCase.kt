package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.TransactionRepository
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository

class DeployAbstractionAddressUseCase(
    private val walletRepository: WalletRepository,
    private val contractFactory: ContractFactory,
    private val transactionRepository: TransactionRepository
): AbstractUseCaseWithoutParams<String?>() {
    override fun invoke(): Flow<String?> = SetupSocialRecoveryUseCase(walletRepository, contractFactory, transactionRepository).invoke(SetupSocialRecoveryUseCase.Params(emptyList(), emptyList()))
}