package md.hackaton.aasocialrecovery.account

import android.util.Log
import md.hackaton.aasocialrecovery.account.models.EncodeUserOpCallDataAndGasLimitResult
import md.hackaton.aasocialrecovery.account.models.GasOverheads
import md.hackaton.aasocialrecovery.account.utils.ERC4337Utils
import md.hackaton.aasocialrecovery.contract.ContractFactory
import md.hackaton.aasocialrecovery.contract.SocialRecoveryAccount
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.DynamicBytes
import org.web3j.crypto.Credentials
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Numeric
import java.lang.Exception
import java.math.BigInteger

class SimpleAccountApi(
    private val web3j: Web3j,
    private val credentials: Credentials,
    private val accountAddress: String? = null,
    private val paymasterAPI: PaymasterAPI? = object : PaymasterAPI {
        override fun getPaymasterAndData(userOp: UserOperationStructure): String {
            return paymasterAddress
        }
    }
) {

    companion object {
        const val entryPointAddress = "0x5FF137D4b0FDCD49DcA30c7CF57E578a026d2789"
        const val paymasterAddress = "0xb082103E4FadB9F512a99178940deb992FecdDd3"
        const val zeroAddress = "0x0000000000000000000000000000000000000000"
    }

    private var senderAddress: String? = null
    private var isPhantom: Boolean = true;
    private val contractFactory = ContractFactory(web3j);

    private val overheads = GasOverheads();

    fun createSignedUserOp(info: TransactionDetailsForUserOp): UserOperationStructure {
        val unsignedObj = createUnsignedUserOp(info)
        Log.d("SimpleAccountApi", "createSignedUserOp.unsigned = $unsignedObj")
        val signed =  signUserOp(unsignedObj)
        Log.d("SimpleAccountApi", "createSignedUserOp.signed = $unsignedObj")
        return signed
    }

    private fun hexify(value: BigInteger): String {
        return "0x0" + value.toString(16)
    }

    private fun arraify(string: String): ByteArray {
        return Numeric.hexStringToByteArray(string)
    }

    fun createUnsignedUserOp(info: TransactionDetailsForUserOp): UserOperationStructure {
        val dataEndGasLimit = encodeUserOpCallDataAndGasLimit(info)
        val callData = dataEndGasLimit.callData;
        val callGasLimit = dataEndGasLimit.callGasLimit;
        val initCode = getInitCode();
        val initGas = estimateCreationGas(initCode)

        val verificationGasLimit = BigInteger.valueOf(getVerificationGasLimit()).add(initGas);
        val maxFeePerGas = info.maxFeePerGas;
        val maxPriorityFeePerGas = info.maxPriorityFeePerGas;

        val partialUserOp = UserOperationStructure(
            sender = getAccountAddress(),
            nonce = hexify(info.nonce ?: getNonce()),
            initCode = initCode,
            callData = callData,
            callGasLimit = hexify(callGasLimit),
            verificationGasLimit = hexify(verificationGasLimit),
            maxFeePerGas = hexify(maxFeePerGas),
            maxPriorityFeePerGas = hexify(maxPriorityFeePerGas),
            paymasterAndData = "0x",
            preVerificationGas = null,
            signature = null
        )

        var paymasterAndData: String? = null

        if (paymasterAPI != null) {
            // fill (partial) preVerificationGas (all except the cost of the generated paymasterAndData)
            val userOpForPm = partialUserOp.copy(
                preVerificationGas = ""//hexify(BigInteger.valueOf(getPreVerificationGas(partialUserOp)))
            )
            paymasterAndData = paymasterAPI.getPaymasterAndData(userOpForPm)
        }

        return partialUserOp.copy(
            preVerificationGas = hexify(BigInteger.valueOf(getPreVerificationGas(partialUserOp))),
            paymasterAndData = paymasterAndData ?: "0x",
            signature = ""
        )
    }

    fun getNonce(): BigInteger {
        if (checkAccountPhantom()) {
            return BigInteger.ZERO
        }

        val accountContract: SocialRecoveryAccount = SocialRecoveryAccount.load(
            getAccountAddress(),         // contract address
            web3j,                  // web3j
            credentials,            // credentials
            DefaultGasProvider()    // contract gas provider
        )
        return accountContract.getNonce().send()
    }

    /**
     * return maximum gas used for verification.
     * NOTE: createUnsignedUserOp will add to this value the cost of creation, if the contract is not yet created.
     */
    fun getVerificationGasLimit(): Long {
        return 100000
    }

    /**
     * should cover cost of putting calldata on-chain, and some overhead.
     * actual overhead depends on the expected bundle size
     */
    fun getPreVerificationGas (userOp: UserOperationStructure): Long {
        return BigInteger("48000", 10).toLong() // TODO: remove this hardcode
//        return calcPreVerificationGas(userOp, this.overheads)
    }

    /**
     * calculate the preVerificationGas of the given UserOperation
     * preVerificationGas (by definition) is the cost overhead that can't be calculated on-chain.
     * it is based on parameters that are defined by the Ethereum protocol for external transactions.
     * @param userOp filled userOp to calculate. The only possible missing fields can be the signature and preVerificationGas itself
     * @param overheads gas overheads to use, to override the default values
     */
    fun calcPreVerificationGas(userOp: UserOperationStructure, overheads: GasOverheads = GasOverheads() ): Long {

        val signatureByteArray = ByteArray(overheads.sigSize).apply {
            fill(1)
        }

        val p = userOp.copy(
            preVerificationGas = userOp.preVerificationGas ?: hexify(BigInteger.valueOf(21000L)),
            signature = Numeric.toHexString(signatureByteArray), // hexlify(Buffer.alloc(ov.sigSize, 1)), // dummy signature
        )

        val packed = ERC4337Utils.packUserOp(p, false)
        val lengthInWord = (packed.size + 31) / 32

        val zeroByte: Byte = 0;
        val callData: List<Byte> = packed.map { x -> if (x == zeroByte) overheads.zeroByte else overheads.nonZeroByte }
        var callDataCost = 0
        for (value in callData) {
            callDataCost += value
        }

        val result = Math.round(
            callDataCost +
                    overheads.fixed.toDouble() / overheads.bundleSize.toDouble() +
                    overheads.perUserOp +
                    overheads.perUserOpWord * lengthInWord
        )
        return result
    }

    fun estimateCreationGas(initCode: String?): BigInteger {
        if (initCode == null || initCode == "0x") return BigInteger.ZERO
        val deployerAddress = initCode.substring(0, 42)
        val deployerCallData = "0x" + initCode.substring(42)
        val transaction = Transaction(
            null,         // from
            null,        // nonce
            null,      // gasPrice
            null,      // gasLimit
            deployerAddress,   // to
            null,        // value
            deployerCallData   // data
        )
        return web3j.ethEstimateGas(transaction).send().amountUsed
    }

    fun signUserOp(userOp: UserOperationStructure): UserOperationStructure {
        val userOpHash = getUserOpHash(userOp);
        val signature = signUserOpHash(userOpHash);

        return userOp.copy(
            signature = signature
        )
    }

    fun signUserOpHash(hash: ByteArray): String {
        println(Numeric.toHexString(hash))
        val message = Sign.getEthereumMessageHash(hash)
        //0x15c2eb91aac3349746fced99dbd411720e176f45284f0831f608781fbc1db2bf
        val signature = Sign.signMessage(message, credentials.ecKeyPair, false)
        val hexStringSignature = Numeric.toHexString(signature.r + signature.s + signature.v)
        return hexStringSignature
    }

    fun encodeUserOpCallDataAndGasLimit(detailsForUserOp: TransactionDetailsForUserOp): EncodeUserOpCallDataAndGasLimitResult {

        val value = detailsForUserOp.value ?: BigInteger("0")   //parseNumber(detailsForUserOp.value) ?? BigNumber.from(0)
        val callData = encodeExecute(detailsForUserOp.target, value, detailsForUserOp.data)

        val transaction = Transaction(
            entryPointAddress, // from
            null, // nonce
            null, // gasPrice
            null, // gasLimit
            getAccountAddress(), // to,
            BigInteger.ZERO,
            callData, // data
        )

        val callGasLimit = detailsForUserOp.gasLimit ?: DefaultGasProvider.GAS_LIMIT //web3j.ethEstimateGas(transaction).send().amountUsed

        return EncodeUserOpCallDataAndGasLimitResult(
            callData = callData,
            callGasLimit = BigInteger("800000", 10),
        )
    }


    fun encodeExecute(target: String, value: BigInteger, data: String): String {
        val accountContract: SocialRecoveryAccount = SocialRecoveryAccount.load(
            accountAddress,         // contract address
            web3j,                  // web3j
            credentials,            // credentials
            DefaultGasProvider()    // contract gas provider
        )
        return accountContract.execute(target, value, arraify(data)).encodeFunctionCall()
    }

    /**
     * return the account's address.
     * this value is valid even before deploying the contract.
     */
    fun getAccountAddress(): String {
        if (senderAddress == null) {
            if (accountAddress != null) {
                senderAddress = accountAddress
            } else {
                senderAddress = getCounterFactualAddress()
            }
        }
        return this.senderAddress!!
    }


    /**
     * calculate the account address even before it is deployed
     */
    fun getCounterFactualAddress(): String {
        val accountInitCode = getAccountInitCode()
        println("SimpleAccountApi.initCode = $accountInitCode")
        val initCode = arraify(accountInitCode)
        // contract method;
        val function = org.web3j.abi.datatypes.Function(
            "getSenderAddress",
            listOf(DynamicBytes(initCode)),
            emptyList()
        );
        val encodedFunction = FunctionEncoder.encode(function)
        val transaction = Transaction.createEthCallTransaction(
            zeroAddress,        // from
            entryPointAddress,  // to
            encodedFunction     // data
        )
        // do eth call
        val result = web3j.ethCall(transaction,  DefaultBlockParameterName.LATEST).send()
        if (result.isReverted) {
            // remove quotes "data"
            val data = result.error.data.substring(1, result.error.data.length - 1)
            // covert hex string to byte array
            val dataBytes = arraify(data)
            // get sender address bytes range from data
            val senderBytes = dataBytes.copyOfRange(dataBytes.size - 20, dataBytes.size)
            // convert bytes array to hex string
            return Numeric.toHexString(senderBytes);
        }


        throw Exception("must handle revert")
    }

    /**
     * return initCode value to into the UserOp.
     * (either deployment code, or empty hex if contract already deployed)
     */
    fun getInitCode(): String {
        if (this.checkAccountPhantom()) {
            return this.getAccountInitCode()
        }
        return "0x"
    }

    /**
     * check if the contract is already deployed.
     */
    fun checkAccountPhantom(): Boolean {
        if (!this.isPhantom) {
            // already deployed. no need to check anymore.
            return this.isPhantom;
        }
        val accAddress = this.getAccountAddress();
        Log.d("SimpleAccountApi", "checkAccountPhantom.accAddress = $accAddress")
        val senderAddressCode = web3j.ethGetCode(accAddress, DefaultBlockParameterName.LATEST).send().code;
        Log.d("SimpleAccountApi", "checkAccountPhantom.getCode = $senderAddressCode")
        if (senderAddressCode.length > 2) {
            Log.d("SimpleAccountApi","SimpleAccount Contract already deployed at ${this.senderAddress}")
            this.isPhantom = false;
        }
        else {
             Log.d("SimpleAccountApi", "SimpleAccount Contract is NOT YET deployed at ${this.senderAddress} - working in \"phantom account\" mode.")
        }
        return this.isPhantom;
    }

    /**
     * return the value to put into the "initCode" field, if the account is not yet deployed.
     * this value holds the "factory" address, followed by this account's information
     */
    fun getAccountInitCode(): String {
        val accountFactory = contractFactory.getSocialRecoveryAccountFactoryContract(credentials)

        val encodedCall = accountFactory.createAccount(
            credentials.address,
            BigInteger.ZERO
        ).encodeFunctionCall()
        return "0x${Numeric.cleanHexPrefix(accountFactory.contractAddress)}${Numeric.cleanHexPrefix(encodedCall)}"
    }

    /**
     * return userOpHash for signing.
     * This value matches entryPoint.getUserOpHash (calculated off-chain, to avoid a view call)
     * @param userOp userOperation, (signature field ignored)
     */
    fun getUserOpHash(userOp: UserOperationStructure): ByteArray {
        val chainId = web3j.ethChainId().send().chainId.toInt()
        return ERC4337Utils.getUserOpHash(userOp, entryPointAddress, chainId)
    }
}