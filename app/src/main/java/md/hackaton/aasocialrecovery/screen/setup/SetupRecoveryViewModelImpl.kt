package md.hackaton.aasocialrecovery.screen.setup

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.domain.usecase.SetupSocialRecoveryUseCase
import md.hackaton.aasocialrecovery.screen.base.BaseViewModel
import md.hackaton.aasocialrecovery.screen.base.BaseViewModelImpl
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryDataListAdapter

class SetupRecoveryViewModelImpl(
    private val setupSocialRecoveryUseCase: SetupSocialRecoveryUseCase
): BaseViewModelImpl(), SetupRecoveryViewModel {

    override val data: MutableStateFlow<List<RecoveryDataListAdapter.ItemModel>> = MutableStateFlow(emptyList())
    override val error: MutableSharedFlow<Exception?> = MutableSharedFlow()
    override val success: MutableSharedFlow<String> = MutableSharedFlow()

    private val agents: MutableList<String> = ArrayList()
    private val freezers: MutableList<String> = ArrayList()

    override fun onAddFreezer(address: String) {
        freezers.add(address)
        updateData()
    }

    override fun onAddAgent(signature: String) {
        agents.add(signature)
        updateData()
    }


    override fun onSubmitPress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                setupSocialRecoveryUseCase.invoke(SetupSocialRecoveryUseCase.Params(
                    address = freezers,
                    agentHash = agents
                )).collect { result ->
                    if (result.error != null) {
                        throw IllegalStateException(result.error.message)
                    } else {
                        success.emit(result.result ?: "TX hash is missing")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                error.emit(e)
            }
        }
    }

    private fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            val tmp: MutableList<RecoveryDataListAdapter.ItemModel> = ArrayList()

            for (index in agents.indices) {
                tmp.add(
                    RecoveryDataListAdapter.ItemModel(
                        "agent_$index",
                        "Agent #$index",
                        agents[index]
                    ))
            }

            for (index in freezers.indices) {
                tmp.add(RecoveryDataListAdapter.ItemModel(
                    "freezer_$index",
                    "Freezer #$index",
                    freezers[index]
                ))
            }

            data.emit(tmp)
        }
    }


}