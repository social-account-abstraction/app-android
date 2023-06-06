package md.hackaton.aasocialrecovery

import jnr.constants.ConstantSet
import md.hackaton.aasocialrecovery.account.AbiCoder
import md.hackaton.aasocialrecovery.account.utils.ERC4337Utils
import md.hackaton.aasocialrecovery.encryption.CryptographyUtils
import org.junit.Test
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.crypto.Hash
import org.web3j.protocol.Network
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.nio.charset.StandardCharsets

class CryptographyUtilsTest {

    @Test
    fun encrypt_message_test() {
        val message = "Hello World"
        val result = CryptographyUtils.encryptMessage(message)
        println("result = $result")
    }


    @Test()
    fun hash_test() {
        val original = "d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42"
        val hash = Hash.sha3(original)
        println("original hash  = " +original)
        println("hash from hash = " +Numeric.cleanHexPrefix(hash))
    }

    @Test()
    fun hash_test_2() {
        val original = "d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42"

        val data = Hash.sha3("SocialRecovery(bytes32 salt,bytes32 key)".toByteArray())
        val salt = Hash.sha3("default".toByteArray())
        val key = salt

        val enc = AbiCoder.encode(
            listOf("bytes32", "bytes32", "bytes32"),
            listOf(data, salt, key)
        )
        println("hash = ${Numeric.toHexString(Hash.sha3(enc))}")
    }


    @Test
    fun test() {
        val hash = Numeric.hexStringToByteArray("0xd46f75ce4472f7591b11a46251586d42703e13b6f327b0a225b99cf00b4efde0")
        val salt = Numeric.hexStringToByteArray("d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42")
        val result = ERC4337Utils.getHashFromHash(hash, salt)
        println(Numeric.toHexString(result))
        val web3j = Web3j.build(HttpService(Constants.NODE_URL))
        println(web3j.ethChainId().send().chainId)
        //0xa68049c64aea908a02595f298a52b44791e4fdc5023a4d7119ea82234a9a7b02d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42703e13b6f327b0a225b99cf00b4efde0
        //0x7240ce1baff942424bdf7b138123116cb962771b6b4514a4796f36109d5b2784d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42d46f75ce4472f7591b11a46251586d42703e13b6f327b0a225b99cf00b4efde0
    }
}