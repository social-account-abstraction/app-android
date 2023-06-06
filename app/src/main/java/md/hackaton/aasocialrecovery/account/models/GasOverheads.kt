package md.hackaton.aasocialrecovery.account.models

data class GasOverheads(
    /**
     * fixed overhead for entire handleOp bundle.
     */
    val fixed: Long = 21000,

    /**
     * per userOp overhead, added on top of the above fixed per-bundle.
     */
    val perUserOp: Long = 19500,

    /**
     * overhead for userOp word (32 bytes) block
     */
    val perUserOpWord: Long = 4,

    // perCallDataWord: number

    /**
     * zero byte cost, for calldata gas cost calculations
     */
    val zeroByte: Byte = 4,

    /**
     * non-zero byte cost, for calldata gas cost calculations
     */
    val nonZeroByte: Byte = 16,

    /**
     * expected bundle size, to split per-bundle overhead between all ops.
     */
    val bundleSize: Int = 1,

    /**
     * expected length of the userOp signature.
     */
    val sigSize: Int = 65,
)
