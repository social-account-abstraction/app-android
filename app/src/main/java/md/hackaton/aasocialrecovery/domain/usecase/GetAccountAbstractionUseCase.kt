package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.contract.SocialRecoveryAccount
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository

class GetAccountAbstractionUseCase(
    private val walletRepository: WalletRepository,
    private val contractFactory: ContractFactory
) {

    fun invoke(): SocialRecoveryAccount {
        val credentials = walletRepository.getCredentials()
        val account = contractFactory.getSocialRecoveryAccountContract(walletRepository.getAbstractionAddress()!!, credentials)
        return account
    }
}