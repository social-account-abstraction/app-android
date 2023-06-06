package md.hackaton.aasocialrecovery.di

import com.google.gson.Gson
import md.hackaton.aasocialrecovery.Constants
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.data.remote.service.JsonRpcService
import md.hackaton.aasocialrecovery.domain.repository.WalletRepository
import md.hackaton.aasocialrecovery.domain.usecase.DeployAbstractionAddressUseCase
import md.hackaton.aasocialrecovery.domain.usecase.FreezeAccountUseCase
import md.hackaton.aasocialrecovery.domain.usecase.GetAccountAbstractionUseCase
import md.hackaton.aasocialrecovery.domain.usecase.GetAccountDetailsUseCase
import md.hackaton.aasocialrecovery.domain.usecase.RecoverAccountUseCase
import md.hackaton.aasocialrecovery.domain.usecase.SetupSocialRecoveryUseCase
import md.hackaton.aasocialrecovery.screen.auth.AuthViewModelImpl
import md.hackaton.aasocialrecovery.screen.home.HomeViewModelImpl
import md.hackaton.aasocialrecovery.screen.recovery.RecoveryViewModelImpl
import md.hackaton.aasocialrecovery.screen.scan.ScanQRViewModelImpl
import md.hackaton.aasocialrecovery.screen.setup.SetupRecoveryViewModelImpl
import md.hackaton.aasocialrecovery.utils.NetworkUtils
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single { NetworkUtils.getUnsafeOkHttpClient() }
    single { Web3j.build(HttpService(Constants.NODE_URL)) }
    single { ContractFactory(get()) }

    val rpcRetrofitQualifier = named("RpcRetrofit")
    single(rpcRetrofitQualifier) {
        Retrofit.Builder()
            .baseUrl("http://93.115.26.187:4337")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    single { get<Retrofit>(rpcRetrofitQualifier).create(JsonRpcService::class.java) }


    single { WalletRepository(get()) }

    viewModel { AuthViewModelImpl(get()) }
    viewModel { RecoveryViewModelImpl(
        recoverAccountUseCase = RecoverAccountUseCase(
            get(), get(), get()
        )
    ) }
    viewModel { ScanQRViewModelImpl() }
    viewModel { SetupRecoveryViewModelImpl(
        setupSocialRecoveryUseCase = SetupSocialRecoveryUseCase(
            get(), get(), get(),
        )
    ) }
    viewModel { HomeViewModelImpl(
        getAccountDetailsUseCase = GetAccountDetailsUseCase(
            get(), get()
        ),
        getAccountAbstractionUseCase = GetAccountAbstractionUseCase(
            get(), get()
        ),
        deployAbstractionAddressUseCase = DeployAbstractionAddressUseCase(
            get(), get(), get(),
        ),
        freezeAccountUseCase = FreezeAccountUseCase(
            get(), get(),
        )
    ) }
}