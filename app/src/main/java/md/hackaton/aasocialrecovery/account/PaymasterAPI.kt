package md.hackaton.aasocialrecovery.account

interface PaymasterAPI {
    fun getPaymasterAndData(userOp: UserOperationStructure): String
}