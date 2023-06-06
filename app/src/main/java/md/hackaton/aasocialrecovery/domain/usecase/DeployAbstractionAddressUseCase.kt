package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository

class DeployAbstractionAddressUseCase(
    private val walletRepository: WalletRepository,
    private val rpcService: JsonRpcService,
    private val contractFactory: ContractFactory
): AbstractUseCaseWithoutParams<JsonRpcCallResponse>() {

    override fun invoke(): Flow<JsonRpcCallResponse> = SetupSocialRecoveryUseCase(walletRepository, rpcService, contractFactory).invoke(SetupSocialRecoveryUseCase.Params(emptyList(), emptyList()))
}