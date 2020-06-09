package big.forest.trees

import android.util.Log
import big.forest.Forest
import big.forest.LogEntry
import big.forest.Tree

/**
 * A debug tree that uses Android logging to log messages.
 */
class DebugTree : Tree {
    companion object {
        internal const val DEFAULT_TAG = "big.forest.trees.DebugTree"
        private const val FOREST_PACKAGE_NAME = "big.forest"
        private const val THREAD_PACKAGE_NAME = "java.lang.Thread"
    }

    override fun log(entry: LogEntry) {
        when (entry.level) {
            Forest.Level.VERBOSE -> Log.v(entry.getTag(), entry.buildMessage(), entry.throwable)
            Forest.Level.DEBUG -> Log.d(entry.getTag(), entry.buildMessage(), entry.throwable)
            Forest.Level.INFO -> Log.i(entry.getTag(), entry.buildMessage(), entry.throwable)
            Forest.Level.WARN -> Log.w(entry.getTag(), entry.buildMessage(), entry.throwable)
            Forest.Level.ERROR -> Log.e(entry.getTag(), entry.buildMessage(), entry.throwable)
            Forest.Level.FATAL -> Log.wtf(entry.getTag(), entry.buildMessage(), entry.throwable)
            else -> {
            }
        }
    }

    private fun LogEntry.getTag(): String {
        if (!tag.isNullOrBlank()) {
            return tag
        }

        val stackTrace = Thread.currentThread().stackTrace
        if (stackTrace.size <= 1) {
            return DEFAULT_TAG
        }

        for (i in 1 until stackTrace.size) {
            val stackTraceElement = stackTrace[i]
            if (stackTraceElement.className.indexOf(FOREST_PACKAGE_NAME) != 0 &&
                stackTraceElement.className.indexOf(THREAD_PACKAGE_NAME) != 0
            ) {
                return stackTraceElement.className
            }
        }

        return DEFAULT_TAG
    }

    private fun LogEntry.buildMessage(): String {
        val m = message ?: ""

        val contextString = if (!context.isEmpty()) {
            val output = if (m.isNotEmpty()) "\n" else ""
            "${output}Context:\n$context"
        } else {
            ""
        }

        val attributesString = if (attributes.isNotEmpty()) {
            val output = if (m.isNotEmpty() || contextString.isNotEmpty()) "\n" else ""
            "${output}Attributes:\n$attributes"
        } else {
            ""
        }

        return "$m$contextString$attributesString"
    }
}