package big.forest

import big.forest.context.ForestContext

abstract class AbstractForest(
    private val context: () -> ForestContext
) : Forest {

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

    override fun deforest() {
        val t = allTrees.toMutableList()
        t.clear()
        allTrees = t
    }

    override fun preProcessLog(callback: PreProcessLogCallback) {
        preProcessLogCallback = callback
    }

    override fun v(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.VERBOSE, message, null, attributes)
    }

    override fun v(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.VERBOSE, message, throwable, attributes)
    }

    override fun v(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.VERBOSE, null, throwable, attributes)
    }

    override fun d(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.DEBUG, message, null, attributes)
    }

    override fun d(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.DEBUG, message, throwable, attributes)
    }

    override fun d(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.DEBUG, null, throwable, attributes)
    }

    override fun i(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.INFO, message, null, attributes)
    }

    override fun i(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.INFO, message, throwable, attributes)
    }

    override fun i(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.INFO, null, throwable, attributes)
    }

    override fun w(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.WARN, message, null, attributes)
    }

    override fun w(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.WARN, message, throwable, attributes)
    }

    override fun w(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.WARN, null, throwable, attributes)
    }

    override fun e(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.ERROR, message, null, attributes)
    }

    override fun e(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.ERROR, message, throwable, attributes)
    }

    override fun e(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.ERROR, null, throwable, attributes)
    }

    override fun f(message: String, attributes: Map<String, Any>) {
        log(Forest.Level.FATAL, message, null, attributes)
    }

    override fun f(message: String, throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.FATAL, message, throwable, attributes)
    }

    override fun f(throwable: Throwable, attributes: Map<String, Any>) {
        log(Forest.Level.FATAL, null, throwable, attributes)
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
        attributes: Map<String, Any>
    ): LogEntry {
        val tag = name ?: getTag(throwable)

        return LogEntry(
            level,
            Thread.currentThread().id,
            System.currentTimeMillis(),
            context.invoke(),
            message,
            tag,
            throwable,
            attributes
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