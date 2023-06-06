package md.hackaton.aasocialrecovery.screen.recovery

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.domain.usecase.RecoverAccountUseCase
import md.hackaton.aasocialrecovery.screen.base.BaseViewModelImpl

class RecoveryViewModelImpl(
    private val recoverAccountUseCase: RecoverAccountUseCase
): BaseViewModelImpl(), RecoveryViewModel {

    override val data: MutableSharedFlow<List<RecoveryDataListAdapter.ItemModel>> = MutableSharedFlow()
    override val submitEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val successEvent: MutableSharedFlow<String> = MutableSharedFlow()
    override val errorEvent: MutableSharedFlow<Exception> = MutableSharedFlow()

    private val currentAgentHashes: MutableList<String> = ArrayList()
    private val newAgentHashes: MutableList<String> = ArrayList()

    override fun addCurrentAgentHash(hash: String) {
        currentAgentHashes.add(hash)
        updateDataList()
    }

    override fun addNewAgentHash(hash: String) {
        newAgentHashes.add(hash)
        updateDataList()
    }


    private fun updateDataList() {
        viewModelScope.launch {
            val tmp: MutableList<RecoveryDataListAdapter.ItemModel> = ArrayList()
            for (index in currentAgentHashes.indices) {
                tmp.add(
                    RecoveryDataListAdapter.ItemModel(
                        id = "recovery_hash_$index",
                        label = "Recovery hash #$index",
                        value = currentAgentHashes[index]
                    ))
            }

            for (index in newAgentHashes.indices) {
                tmp.add(RecoveryDataListAdapter.ItemModel(
                    id = "new_agent_hash_$index",
                    label = "New Agent hash #$index",
                    value = newAgentHashes[index]
                ))
            }

            data.emit(tmp)

        }
    }
    override fun onSubmitPress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recoverAccountUseCase.invoke(RecoverAccountUseCase.Params(
                    currentAgentsHashSet = currentAgentHashes,
                    newAgentsHashSet = newAgentHashes
                )).collect { result ->
                    if (result.error != null) {
                        throw IllegalStateException(result.error.message)
                    } else {
                        successEvent.emit(result.result ?: "TX hash missing")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorEvent.emit(e)
            }
        }
    }
}