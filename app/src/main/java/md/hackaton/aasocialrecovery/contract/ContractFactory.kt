package md.hackaton.aasocialrecovery.contract

import md.hackaton.aasocialrecovery.Constants
import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.tx.gas.DefaultGasProvider

class ContractFactory(val web3j: Web3j) {

    fun getSocialRecoveryAccountFactoryContract(credentials: Credentials): SocialRecoveryAccountFactory {
        return SocialRecoveryAccountFactory.load(
            Constants.FACTORY_CONTRACT_ADDRESS,
            web3j,
            credentials,
            DefaultGasProvider()
        )
    }

    fun getSocialRecoveryAccountContract(address: String, credentials: Credentials): SocialRecoveryAccount {
        return SocialRecoveryAccount.load(
            address,
            web3j,
            credentials,
            DefaultGasProvider()
        )
    }
}