package com.basim.block.core.common.result

import com.basim.block.core.common.error.AppError
import com.basim.block.core.common.error.toAppError
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeCall(
    mapError: (Throwable) -> AppError = { it.toAppError() },
    block: suspend () -> T,
): DataResult<T> = try {
    DataResult.Success(block())
} catch (e: CancellationException) {
    throw e
} catch (t: Throwable) {
    DataResult.Error(mapError(t))
}
