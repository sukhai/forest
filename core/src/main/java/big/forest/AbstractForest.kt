/*
 * Copyright 2020 Su Khai Koh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package big.forest

import big.forest.land.Land

/**
 * An abstract class that implements the [Forest].
 *
 * @param land A [Land] that will be passed to the [Tree]s in this [Forest].
 * @param name The name of the [Forest]. This value will be used as the [LogEntry.tag]
 * if this value is not `null`. This value is set when [Forest.getForest]
 * is called.
 */
abstract class AbstractForest(
    private val land: () -> Land,
    override val name: String? = null
) : Forest {

    override var level: Forest.Level = Forest.Level.VERBOSE
        set(value) {
            if (!allowGlobalOverride) {
                return
            }
            field = value
        }
    override val trees: List<Tree>
        get() = allTrees

    /**
     * Update the [preProcessLogCallback]. This method is intended to be used
     * by [Forest.Global] and will only update this property if
     * [allowGlobalOverride] is set to `true`.
     */
    internal var preProcessLogCallback: PreProcessLogCallback? = null
        set(value) {
            if (!allowGlobalOverride) {
                return
            }
            field = value
        }
    private var allTrees = listOf<Tree>()
    private var allowGlobalOverride = true

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

    /**
     * Update this [Forest] with the given [config].
     *
     * @param config The configuration to be applied to this [Forest].
     */
    internal fun updateWithConfig(config: ForestConfig) {
        level = config.level
        preProcessLogCallback = config.preProcessLog
        allTrees = config.trees.toList()
        allowGlobalOverride = config.allowGlobalOverride
    }

    /**
     * Attempt to plant the [tree] into this [Forest].
     * This method is intended to be used by [Forest.Global] and will only
     * plant the [tree] if [allowGlobalOverride] is set to `true`.
     *
     * @param tree The [Tree] to be planted to this [Forest].
     */
    internal fun tryPlant(tree: Tree) {
        if (!allowGlobalOverride) {
            return
        }

        val t = allTrees.toMutableList()
        if (!t.contains(tree)) {
            t.add(tree)
        }
        allTrees = t
    }

    /**
     * Attempt to cut the [tree] from this [Forest].
     * This method is intended to be used by [Forest.Global] and will only
     * cut the [tree] if [allowGlobalOverride] is set to `true`.
     *
     * @param tree The [Tree] to be cut from this [Forest].
     */
    internal fun tryCut(tree: Tree) {
        if (!allowGlobalOverride) {
            return
        }

        val t = allTrees.toMutableList()
        t.remove(tree)
        allTrees = t
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
        val tag = getTag(throwable)

        return LogEntry(
            level,
            Thread.currentThread().id,
            System.currentTimeMillis(),
            land.invoke(),
            message,
            tag,
            throwable,
            attributes
        )
    }

    private fun getTag(throwable: Throwable?): String? {
        if (!name.isNullOrBlank()) {
            return name
        }

        if (throwable == null || throwable.stackTrace.isEmpty()) {
            return null
        }

        return throwable.stackTrace[0].className
    }
}