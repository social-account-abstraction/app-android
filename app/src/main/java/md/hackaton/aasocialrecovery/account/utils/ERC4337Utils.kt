package md.hackaton.aasocialrecovery.account.utils

import md.hackaton.aasocialrecovery.account.AbiCoder
import md.hackaton.aasocialrecovery.account.UserOperationStructure
import org.web3j.crypto.Hash
import org.web3j.utils.Numeric
import java.math.BigInteger


class ERC4337Utils {

    companion object {
        fun packUserOp(op: UserOperationStructure, forSignature: Boolean = true): ByteArray {
            if (forSignature) {
                return AbiCoder.encode(
                    listOf(
                        "address",
                        "uint256",
                        "bytes32",
                        "bytes32",
                        "uint256",
                        "uint256",
                        "uint256",
                        "uint256",
                        "uint256",
                        "bytes32"
                    ),
                    listOf(
                        op.sender,
                        Numeric.toBigInt(op.nonce),
                        Numeric.hexStringToByteArray(Hash.sha3(op.initCode)), // keccak256
                        Numeric.hexStringToByteArray(Hash.sha3(op.callData)), // keccak256
                        Numeric.toBigInt(op.callGasLimit),
                        Numeric.toBigInt(op.verificationGasLimit),
                        Numeric.toBigInt(op.preVerificationGas),
                        Numeric.toBigInt(op.maxFeePerGas),
                        Numeric.toBigInt(op.maxPriorityFeePerGas),
                        Numeric.hexStringToByteArray(Hash.sha3(op.paymasterAndData)) // keccak256
                    )
                )
            } else {
                // for the purpose of calculating gas cost encode also signature (and no keccak of bytes)
                return AbiCoder.encode(
                    listOf(
                        "address",
                        "uint256",
                        "bytes",
                        "bytes",
                        "uint256",
                        "uint256",
                        "uint256",
                        "uint256",
                        "uint256",
                        "bytes",
                        "bytes",
                    ),
                    listOf(
                        op.sender,
                        Numeric.toBigInt(op.nonce),
                        Numeric.hexStringToByteArray(op.initCode),
                        Numeric.hexStringToByteArray(op.callData),
                        Numeric.toBigInt(op.callGasLimit),
                        Numeric.toBigInt(op.verificationGasLimit),
                        Numeric.toBigInt(op.preVerificationGas),
                        Numeric.toBigInt(op.maxFeePerGas),
                        Numeric.toBigInt(op.maxPriorityFeePerGas),
                        Numeric.hexStringToByteArray(op.paymasterAndData),
                        Numeric.hexStringToByteArray(op.signature)
                    )
                )
            }
        }


        fun getUserOpHash(userOp: UserOperationStructure, entryPoint: String, chainId: Int): ByteArray {
            val userOpHash = Hash.sha3(packUserOp(userOp, true))
            val enc = AbiCoder.encode(
                listOf("bytes32", "address", "uint256"),
                listOf(userOpHash, entryPoint, chainId)
            )
            return Hash.sha3(enc)
        }

        fun getHashFromHash(hash: ByteArray, salt: ByteArray): ByteArray {
            val enc = AbiCoder.encode(
                listOf("bytes32", "bytes32", "bytes32"),
                listOf(Numeric.hexStringToByteArray(Hash.sha3("SocialRecovery(bytes32 salt,bytes32 key)")), salt, hash)
            )

            println("getHashFromHash.encode = ${Numeric.toHexString(enc)}")
            return Hash.sha3(enc)
        }
    }


}