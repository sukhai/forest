package big.forest

/**
 * An object that stores the configuration of a [Forest].
 *
 * @param level The logging level. This sets what level of log to be passed to a [Tree].
 * @param preProcessLog A callback to be invoked before passing a [LogEntry] to the
 * [Tree]. This is useful to filter or modify a [LogEntry] before the log is being
 * passed to [Tree.log].
 * @param allowGlobalOverride Setting `true` will allow any configuration change from
 * [Forest.Global] to override this [Forest], `false` will prevent this override.
 * @param trees A collection of [Tree]s to be added to the [Forest].
 */
data class ForestConfig internal constructor(
    var level: Forest.Level,
    var preProcessLog: PreProcessLogCallback?,
    var allowGlobalOverride: Boolean = true,
    internal val trees: MutableList<Tree>
) {
    /**
     * Plant the [tree] to this [Forest]. Any logging method call (i.e [Forest.d],
     * [Forest.i], etc) will be forwarded to this [tree] if the [Forest] determine
     * it should log the given message.
     *
     * @param tree The [Tree] that will handle the logging call.
     */
    fun plant(tree: Tree) {
        trees.add(tree)
    }

    /**
     * Cut the [tree] from this [Forest]. Any logging method call (i.e [Forest.d],
     * [Forest.i], etc) will no longer be forwarded to this [tree].
     *
     * @param tree
     */
    fun cut(tree: Tree) {
        trees.remove(tree)
    }
}
