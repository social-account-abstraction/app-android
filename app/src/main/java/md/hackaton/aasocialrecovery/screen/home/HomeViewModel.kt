package md.hackaton.aasocialrecovery.screen.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import md.hackaton.aasocialrecovery.domain.usecase.GetAccountDetailsUseCase
import md.hackaton.aasocialrecovery.screen.base.BaseViewModel

interface HomeViewModel : BaseViewModel {

    val details: SharedFlow<GetAccountDetailsUseCase.Result>
    val errorEvent: SharedFlow<Exception>
    val deployResult: SharedFlow<String>
    val freezeResult: SharedFlow<String>
    val loading: StateFlow<Boolean>

    fun onDeployPress()
    fun onFreezePress()
    fun onRefresh()
}