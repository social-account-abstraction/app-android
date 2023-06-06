package md.hackaton.aasocialrecovery.screen.recovery

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import md.hackaton.aasocialrecovery.screen.base.BaseViewModel

interface RecoveryViewModel: BaseViewModel {

    val data: SharedFlow<List<RecoveryDataListAdapter.ItemModel>>
    val submitEnabled: StateFlow<Boolean>
    val successEvent: SharedFlow<String>
    val errorEvent: SharedFlow<Exception>


    fun addCurrentAgentHash(hash: String)
    fun addNewAgentHash(hash: String)

    fun onSubmitPress()
}