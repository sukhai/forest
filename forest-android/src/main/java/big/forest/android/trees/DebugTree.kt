package big.forest.android.trees

import android.util.Log
import big.forest.Forest
import big.forest.LogEntry
import big.forest.Tree

/**
 * A debug tree that uses Android logger to log messages.
 */
class DebugTree : Tree {
    companion object {
        private const val DEFAULT_TAG = "big.forest.android.trees.DebugTree"
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
        val logTag = tag
        if (!logTag.isNullOrBlank()) {
            return logTag
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
        val contextString = if (!context.isEmpty()) {
            "\nContext:\n$context"
        } else {
            ""
        }

        val attributesString = if (attributes.isNotEmpty()) {
            "\nAttributes:\n$attributes"
        } else {
            ""
        }

        return "$message$contextString$attributesString"
    }
}