package big.forest

import big.forest.context.Land

/**
 * An object that holds the information of a log.
 *
 * @param level The logging level of the log.
 * @param threadId The calling thread ID.
 * @param timestamp The timestamp when this log is logged. This timestamp is recorded when
 * the [Forest] received a logging call.
 * @param land The [Land] that contains the data from the [Forest].
 * @param message The message to be logged.
 * @param tag The tag. The value is either the name of a [Forest] set from [getForest]
 * or the class name of the first stacktrace from the [throwable] if any.
 * @param throwable The [Throwable] to be logged.
 * @param attributes A collection of attributes to be added to this log.
 */
data class LogEntry(
    val level: Forest.Level,
    val threadId: Long,
    val timestamp: Long,
    val land: Land = Forest.land,
    val message: String? = null,
    val tag: String? = null,
    val throwable: Throwable? = null,
    val attributes: Map<String, Any> = emptyMap()
)