package com.basim.block.core.common.error

// TODO: Check whether we need to inherit Exception if not needed remove message and cause and convert the AppError to sealed interface.
sealed class AppError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {

    /**
     * Anything that goes wrong talking to a server: transport failures (couldn't reach/hear back)
     * and error responses the server returned.
     */
    sealed class Network(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {

        /** No / unstable connectivity — the request never reached the server. */
        data object Unavailable : Network()

        /** The request reached out but took too long to complete. */
        data object Timeout : Network()

        /** Not authenticated, or credentials were rejected — wrong password, expired session (401). */
        data object Unauthorized : Network()

        /** Authenticated, but not allowed to perform this action (403). */
        data object Forbidden : Network()

        /** The requested resource doesn't exist on the server (404). */
        data object NotFound : Network()

        /** The thing being created already exists — e.g. the email is already registered (409). */
        data object Conflict : Network()

        /** The server responded with an error. [code] is the HTTP status when known (typically 5xx). */
        data class Server(val code: Int? = null, val serverMessage: String? = null) : Network()

        /** A server response couldn't be parsed into the expected shape. */
        data object Serialization : Network()
    }

    /** Local persistence failures — Room / SQLite / disk read or write. */
    sealed class Local(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {

        /** A local database read or write failed. */
        data object Database : Local()
    }

    /** Caller-side input was rejected before (or instead of) hitting a data source. */
    sealed class Validation(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {

        /**
         * Input failed a rule. [reason] is a stable, non-localised code (e.g. "weak_password")
         * the UI maps to a user-facing string — never a raw message meant for display as-is.
         */
        data class Invalid(val reason: String? = null) : Validation()
    }

    /** Anything we couldn't classify. Retains the [original] throwable for logging/crash reporting. */
    data class Unknown(val original: Throwable? = null) : AppError(cause = original)
}
