package md.hackaton.aasocialrecovery.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import org.web3j.crypto.Credentials

class GenerateCredentialsUseCase(
    private val walletRepository: WalletRepository
): AbstractUseCaseWithoutParams<Credentials>() {

    override fun invoke(): Flow<Credentials> = flow {
        val credentials = walletRepository.generateCredentials()
        emit(credentials)
    }
}