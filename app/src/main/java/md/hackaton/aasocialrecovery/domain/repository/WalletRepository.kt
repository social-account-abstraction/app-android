package md.hackaton.aasocialrecovery.domain.repository

import md.hackaton.aasocialrecovery.account.SimpleAccountApi
import md.hackaton.aasocialrecovery.utils.StringUtils
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.protocol.Web3j

class WalletRepository(
    private val web3j: Web3j
) {

    companion object {
        private const val password = "^h12(H92@#!hasco=s"
    }

    private lateinit var credentials: Credentials;
    private var address: String? = null;


    fun initCredentials(pk: String, address: String): Credentials {
        credentials = Credentials.create(pk)
        if (address.isNotEmpty()) {
            this.address = address
        } else {
            this.address = null
        }
        return credentials;
    }

    fun getCredentials() = credentials;

    suspend fun generateCredentials(): Credentials {
        val entropy = StringUtils.randomString(32).encodeToByteArray()
        val mnemonic = MnemonicUtils.generateMnemonic(entropy)
        val seed = MnemonicUtils.generateSeed(mnemonic, password)

        val masterKeypair = Bip32ECKeyPair.generateKeyPair(seed)
        val path = intArrayOf(44 or Bip32ECKeyPair.HARDENED_BIT, 60 or Bip32ECKeyPair.HARDENED_BIT, 0 or Bip32ECKeyPair.HARDENED_BIT, 0, 0)
        val childKeypair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
        val credentials = Credentials.create(childKeypair)
        return credentials;
    }

    fun getAbstractionAddress(): String {
        val accountApi = SimpleAccountApi(web3j, credentials, address)
        return accountApi.getAccountAddress();
    }
}