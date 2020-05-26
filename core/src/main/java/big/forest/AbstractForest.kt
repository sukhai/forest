package big.forest

abstract class AbstractForest : Forest {
    override var level: Forest.Level = Forest.Level.VERBOSE
    override var name: String? = null
    override val trees: List<Tree>
        get() = allTrees

    internal var allTrees = listOf<Tree>()
    internal var preProcessLogCallback: PreProcessLogCallback? = null

    override fun plant(tree: Tree) {
        val t = allTrees.toMutableList()
        t.add(tree)
        allTrees = t
    }

    override fun cut(tree: Tree) {
        val t = allTrees.toMutableList()
        t.remove(tree)
        allTrees = t
    }

    override fun clearTrees() {
        val t = allTrees.toMutableList()
        t.clear()
        allTrees = t
    }

    override fun preProcessLog(callback: PreProcessLogCallback) {
        preProcessLogCallback = callback
    }

    override fun v(message: String, extras: Map<String, Any>) {
        log(Forest.Level.VERBOSE, message, null, extras)
    }

    override fun v(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.VERBOSE, message, throwable, extras)
    }

    override fun v(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.VERBOSE, null, throwable, extras)
    }

    override fun d(message: String, extras: Map<String, Any>) {
        log(Forest.Level.DEBUG, message, null, extras)
    }

    override fun d(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.DEBUG, message, throwable, extras)
    }

    override fun d(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.DEBUG, null, throwable, extras)
    }

    override fun i(message: String, extras: Map<String, Any>) {
        log(Forest.Level.INFO, message, null, extras)
    }

    override fun i(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.INFO, message, throwable, extras)
    }

    override fun i(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.INFO, null, throwable, extras)
    }

    override fun w(message: String, extras: Map<String, Any>) {
        log(Forest.Level.WARN, message, null, extras)
    }

    override fun w(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.WARN, message, throwable, extras)
    }

    override fun w(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.WARN, null, throwable, extras)
    }

    override fun e(message: String, extras: Map<String, Any>) {
        log(Forest.Level.ERROR, message, null, extras)
    }

    override fun e(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.ERROR, message, throwable, extras)
    }

    override fun e(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.ERROR, null, throwable, extras)
    }

    override fun f(message: String, extras: Map<String, Any>) {
        log(Forest.Level.FATAL, message, null, extras)
    }

    override fun f(message: String, throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.FATAL, message, throwable, extras)
    }

    override fun f(throwable: Throwable, extras: Map<String, Any>) {
        log(Forest.Level.FATAL, null, throwable, extras)
    }

    override fun log(
        level: Forest.Level,
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

    private fun shouldLog(level: Forest.Level): Boolean {
        return this.level != Forest.Level.OFF && level != Forest.Level.OFF &&
                level.ordinal <= this.level.ordinal
    }

    private fun createLogEntry(
        level: Forest.Level,
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