package md.hackaton.aasocialrecovery.screen.auth

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import md.hackaton.aasocialrecovery.screen.base.BaseViewModel
import org.web3j.crypto.Credentials

interface AuthViewModel: BaseViewModel {

    val generatedPkEvent: SharedFlow<String>
    val completedEvent: SharedFlow<Credentials>
    val error: StateFlow<Exception?>

    fun onGeneratePress()
    fun onSubmitPress(pk: String, address: String)
}