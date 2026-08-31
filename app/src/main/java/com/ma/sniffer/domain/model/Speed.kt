package com.ma.sniffer.domain.model

data class Speed(
    val download: NetworkValue = NetworkValue.zero(),
    val upload: NetworkValue = NetworkValue.zero(),
    val total: NetworkValue = NetworkValue.zero()
) {
    companion object {
        val ZERO = Speed()

        fun fromBytes(downloadBytes: Long, uploadBytes: Long): Speed {
            return Speed(
                download = NetworkValue.fromBytes(downloadBytes),
                upload = NetworkValue.fromBytes(uploadBytes),
                total = NetworkValue.fromBytes(downloadBytes + uploadBytes)
            )
        }
    }
}