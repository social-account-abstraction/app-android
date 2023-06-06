package md.hackaton.aasocialrecovery.screen.setup

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import md.hackaton.aasocialrecovery.screen.base.BaseViewModel
import md.hackaton.aasocialrecovery.screen.base.BaseViewModelImpl
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryDataListAdapter

interface SetupRecoveryViewModel: BaseViewModel {

    val data: StateFlow<List<RecoveryDataListAdapter.ItemModel>>
    val error: SharedFlow<Exception?>
    val success: SharedFlow<String>

    fun onAddFreezer(address: String)

    fun onAddAgent(signature: String)

    fun onSubmitPress()
}