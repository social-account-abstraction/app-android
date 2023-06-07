package md.hackaton.aasocialrecovery.screen.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import md.hackaton.aasocialrecovery.Constants
import md.hackaton.aasocialrecovery.domain.usecase.DeployAbstractionAddressUseCase
import md.hackaton.aasocialrecovery.domain.usecase.FreezeAccountUseCase
import md.hackaton.aasocialrecovery.domain.usecase.GetAccountAbstractionUseCase
import md.hackaton.aasocialrecovery.domain.usecase.GetAccountDetailsUseCase
import md.hackaton.aasocialrecovery.screen.base.BaseViewModelImpl
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.EthFilter
import org.web3j.protocol.http.HttpService

class HomeViewModelImpl(
    private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    private val getAccountAbstractionUseCase: GetAccountAbstractionUseCase,
    private val deployAbstractionAddressUseCase: DeployAbstractionAddressUseCase,
    private val freezeAccountUseCase: FreezeAccountUseCase
): BaseViewModelImpl(), HomeViewModel {

    override val details: MutableSharedFlow<GetAccountDetailsUseCase.Result> = MutableSharedFlow()
    override val errorEvent: MutableSharedFlow<Exception> = MutableSharedFlow()
    override val deployResult: MutableSharedFlow<String> = MutableSharedFlow()
    override val freezeResult: MutableSharedFlow<String> = MutableSharedFlow()
    override val loading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        onRefresh()

//        viewModelScope.launch(Dispatchers.IO) {
//            val account = getAccountAbstractionUseCase.invoke()
//            val web3j =  Web3j.build(HttpService(Constants.NODE_URL))
//
//            web3j.ethLogFlowable(
//                EthFilter(
//                    DefaultBlockParameterName.EARLIEST,
//                    DefaultBlockParameterName.LATEST,
//                    account.contractAddress
//                )
//            ).subscribe(
//                { result ->
//                    println("eth log event = ${result.type}")
//                }, { error ->
//                    error.printStackTrace()
//                })
//        }
    }

    override fun onDeployPress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading.emit(true)
                deployAbstractionAddressUseCase.invoke().collect { transactionHash ->
                    deployResult.emit(transactionHash ?: "null")
                }
            } catch (e: Exception) {
                errorEvent.emit(e)
                e.printStackTrace()
            } finally {
                loading.emit(false)
            }
        }
    }

    override fun onFreezePress() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading.emit(true)
                freezeAccountUseCase.invoke().collect { transactionHash ->
                    freezeResult.emit(transactionHash ?: "null")
                }
            } catch (e: Exception) {
                errorEvent.emit(e)
                e.printStackTrace()
            } finally {
                loading.emit(false)
            }
        }
    }

    override fun onRefresh() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loading.emit(true)
                getAccountDetailsUseCase.invoke().collect {data -> details.emit(data)}
            } catch (e: Exception) {
                errorEvent.emit(e)
            } finally {
                loading.emit(false)
            }
        }
    }
}