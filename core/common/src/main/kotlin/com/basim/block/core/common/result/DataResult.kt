package com.basim.block.core.common.result

import com.basim.block.core.common.error.AppError

sealed interface DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>
    data class Error(val error: AppError) : DataResult<Nothing>
}

inline fun <T, R> DataResult<T>.map(transform: (T) -> R): DataResult<R> = when (this) {
    is DataResult.Success -> DataResult.Success(transform(data))
    is DataResult.Error -> this
}

inline fun <T> DataResult<T>.onSuccess(action: (T) -> Unit): DataResult<T> {
    if (this is DataResult.Success) action(data)
    return this
}

inline fun <T> DataResult<T>.onError(action: (AppError) -> Unit): DataResult<T> {
    if (this is DataResult.Error) action(error)
    return this
}

fun <T> DataResult<T>.getOrNull(): T? = (this as? DataResult.Success)?.data
