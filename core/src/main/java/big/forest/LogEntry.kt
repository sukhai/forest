package big.forest

import big.forest.context.Land

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