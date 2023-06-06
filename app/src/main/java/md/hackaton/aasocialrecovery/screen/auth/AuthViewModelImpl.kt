package md.hackaton.aasocialrecovery.screen.auth

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.screen.base.BaseViewModelImpl
import org.web3j.crypto.Credentials

class AuthViewModelImpl(private val walletRepository: WalletRepository) : BaseViewModelImpl(),
    AuthViewModel {

    override val generatedPkEvent: MutableSharedFlow<String> = MutableSharedFlow()
    override val completedEvent: MutableSharedFlow<Credentials> = MutableSharedFlow()
    override val error: MutableStateFlow<Exception?> = MutableStateFlow(null)

    override fun onGeneratePress() {
        viewModelScope.launch(Dispatchers.IO) {
            val credentials = walletRepository.generateCredentials()
            generatedPkEvent.emit(credentials.ecKeyPair.privateKey.toString(16))
        }
    }

    override fun onSubmitPress(pk: String, address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                error.emit(null)
                val credentials = walletRepository.initCredentials(pk, address)
                completedEvent.emit(credentials)
            } catch (e: Exception) {
                e.printStackTrace()
                error.emit(e)
            }
        }
    }
}