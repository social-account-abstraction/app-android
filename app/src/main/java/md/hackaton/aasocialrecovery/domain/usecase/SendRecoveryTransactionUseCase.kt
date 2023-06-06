package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.data.remote.model.response.JsonRpcCallResponse
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository

class SendRecoveryTransactionUseCase(
    private val walletRepository: WalletRepository
): AbstractUseCaseWithParams<SendRecoveryTransactionUseCase.Params, JsonRpcCallResponse>() {

    data class Params(val agentSecret: String)

    override fun invoke(p: Params): Flow<JsonRpcCallResponse> = flow {

    }
}