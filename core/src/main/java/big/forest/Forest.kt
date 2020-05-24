package big.forest

import java.util.concurrent.ConcurrentHashMap

abstract class Forest {
    enum class Level {
        OFF,
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        VERBOSE
    }

    companion object : Forest() {
        private val forests = ConcurrentHashMap<String, Forest>()

        fun getForest(name: String): Forest {
            return forests.getOrPut(name) {
                RealForest().also { newForest ->
                    newForest.level = level
                    newForest.name = name
                    newForest.preProcessLogCallback = preProcessLogCallback
                    trees.forEach { tree -> newForest.plant(tree) }
                }
            }
        }

        fun getForest(clazz: Class<*>): Forest {
            val name = clazz.canonicalName ?: clazz.`package`.name ?: ""
            return getForest(name)
        }

        override fun plant(tree: Tree) {
            super.plant(tree)
            forests.values.forEach { forest ->
                val t = forest._trees.toMutableList()
                if (!t.contains(tree)) {
                    t.add(tree)
                }
                forest._trees = t
            }
        }

        override fun cut(tree: Tree) {
            super.cut(tree)
            forests.values.forEach { forest ->
                val t = forest._trees.toMutableList()
                t.remove(tree)
                forest._trees = t
            }
        }
    }

    private class RealForest : Forest()

    var level: Level = Level.VERBOSE
    var name: String? = null
    val trees: List<Tree>
        get() = _trees

    private var _trees = listOf<Tree>()
    internal var preProcessLogCallback: ((LogEntry) -> LogEntry?)? = null

    open fun plant(tree: Tree) {
        val t = _trees.toMutableList()
        t.add(tree)
        _trees = t
    }

    open fun cut(tree: Tree) {
        val t = _trees.toMutableList()
        t.remove(tree)
        _trees = t
    }

    fun preProcessLog(callback: (LogEntry) -> LogEntry?) {
        preProcessLogCallback = callback
    }

    fun v(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.VERBOSE, message, null, extras)
    }

    fun v(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.VERBOSE, message, throwable, extras)
    }

    fun v(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.VERBOSE, null, throwable, extras)
    }

    fun d(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.DEBUG, message, null, extras)
    }

    fun d(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.DEBUG, message, throwable, extras)
    }

    fun d(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.DEBUG, null, throwable, extras)
    }

    fun i(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.INFO, message, null, extras)
    }

    fun i(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.INFO, message, throwable, extras)
    }

    fun i(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.INFO, null, throwable, extras)
    }

    fun w(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.WARN, message, null, extras)
    }

    fun w(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.WARN, message, throwable, extras)
    }

    fun w(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.WARN, null, throwable, extras)
    }

    fun e(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.ERROR, message, null, extras)
    }

    fun e(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.ERROR, message, throwable, extras)
    }

    fun e(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.ERROR, null, throwable, extras)
    }

    fun f(message: String, extras: Map<String, Any> = emptyMap()) {
        log(Level.FATAL, message, null, extras)
    }

    fun f(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Level.FATAL, message, throwable, extras)
    }

    fun f(throwable: Throwable, extras: Map<String, Any>) {
        log(Level.FATAL, null, throwable, extras)
    }

    fun log(
        level: Level,
        message: String?,
        throwable: Throwable?,
        attributes: Map<String, Any>
    ) {
        if (!shouldLog(level)) {
            return
        }

        val temp = createLogEntry(level, message, throwable, attributes)

        val entry = if (preProcessLogCallback != null) {
            preProcessLogCallback?.invoke(temp)
        } else {
            temp
        }

        entry?.let { logEntry ->
            trees.forEach { tree ->
                tree.log(logEntry)
            }
        }
    }

    private fun shouldLog(level: Level): Boolean {
        return level != Level.OFF && level.ordinal <= this.level.ordinal
    }

    private fun createLogEntry(
        level: Level,
        message: String?,
        throwable: Throwable?,
        extras: Map<String, Any>
    ): LogEntry {
        val tag = name ?: getTag(throwable)

        return LogEntry(
            level,
            Thread.currentThread().id,
            System.currentTimeMillis(),
            message,
            tag,
            throwable,
            extras
        )
    }

    private fun getTag(throwable: Throwable?): String? {
        if (throwable == null) {
            return null
        }

        // TODO: Try to get tag from stacktrace
        return null
    }
}