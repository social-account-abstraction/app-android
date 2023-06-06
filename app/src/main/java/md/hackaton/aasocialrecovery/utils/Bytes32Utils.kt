package md.hackaton.aasocialrecovery.utils

import org.web3j.abi.datatypes.generated.Bytes32

class Bytes32Utils {

    companion object {

        fun stringToBytes32(string: String): Bytes32 {
            val byteValue = string.toByteArray()
            val byteValueLen32 = ByteArray(32)
            System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.size)
            return Bytes32(byteValueLen32)
        }
    }
}