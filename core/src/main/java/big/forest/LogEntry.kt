package big.forest

import big.forest.context.ForestContext

data class LogEntry(
    val level: Forest.Level,
    val threadId: Long,
    val timestamp: Long,
    val context: ForestContext,
    val message: String? = null,
    val tag: String? = null,
    val throwable: Throwable? = null,
    val attributes: Map<String, Any> = emptyMap()
)