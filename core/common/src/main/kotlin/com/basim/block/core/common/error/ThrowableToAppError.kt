package com.basim.block.core.common.error

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAppError(): AppError = when (this) {
    is AppError -> this
    is SocketTimeoutException -> AppError.Network.Timeout
    is UnknownHostException -> AppError.Network.Unavailable
    is IOException -> AppError.Network.Unavailable
    else -> AppError.Unknown(this)
}
