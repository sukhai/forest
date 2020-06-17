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

import big.forest.Forest.Global.context
import big.forest.Forest.Level.DEBUG
import big.forest.Forest.Level.ERROR
import big.forest.Forest.Level.FATAL
import big.forest.Forest.Level.INFO
import big.forest.Forest.Level.VERBOSE
import big.forest.Forest.Level.WARN
import big.forest.context.ForestContext
import java.util.concurrent.ConcurrentHashMap

/**
 * A callback that will be invoked before processing a [LogEntry].
 *
 * @return A [LogEntry] if you want to log this [LogEntry], otherwise return
 * `null` to not log this [LogEntry].
 */
typealias PreProcessLogCallback = (LogEntry) -> LogEntry?

/**
 * An object that allows the user to send any log entry to a group of [Tree]s.
 *
 * You can create or get an implementation of this interface through
 * [Forest.getForest].
 */
interface Forest {
    /**
     * A class that represents the logging level. A [Forest] will use the level
     * to determine if it should log a given [LogEntry].
     *
     * The order of the levels is (except [Level.OFF]):
     * 1. [Level.FATAL]
     * 2. [Level.ERROR]
     * 3. [Level.WARN]
     * 4. [Level.INFO]
     * 5. [Level.DEBUG]
     * 6. [Level.VERBOSE]
     */
    enum class Level {
        /**
         * Turn off logging.
         */
        OFF,

        /**
         * Indicates a serious failure that could potentially prevent
         * user from continuing using the program.
         */
        FATAL,

        /**
         * Indicates a failure but the user is able to continue using the
         * program, or the program is capable of automatically recover from
         * it.
         */
        ERROR,

        /**
         * Indicates a potential problem.
         */
        WARN,

        /**
         * Indicates the log is for informational purposes.
         */
        INFO,

        /**
         * Indicates a tracing information log.
         */
        DEBUG,

        /**
         * Indicates a highly detailed tracing log.
         */
        VERBOSE
    }

    /**
     * The logging level of this [Forest]. This level will be used to
     * determine if it should log a given [LogEntry].
     *
     * The order of the levels is (except [Level.OFF]:
     * 1. [FATAL]
     * 2. [ERROR]
     * 3. [WARN]
     * 4. [INFO]
     * 5. [DEBUG]
     * 6. [VERBOSE]
     *
     * This [Forest] will only log a [LogEntry] if the given level is
     * less than this [level]. For example, if this [level] is set to
     * [VERBOSE], then calling [Forest.i] will log a given [LogEntry]
     * because [VERBOSE] is higher level than [INFO].
     * In contrast, if this [level] is set to [INFO] and [Forest.v] is
     * called, then the [LogEntry] will not be logged because [INFO]
     * level is lower than [VERBOSE].
     *
     * Setting this value to [Level.OFF] will not log any [LogEntry] and none
     * of the [Tree] this [Forest] holds will receive a [Tree.log] call.
     */
    var level: Level

    /**
     * The name of the [Forest]. This value will be used as the [LogEntry.tag]
     * if this value is not `null`. This value is set when [Forest.getForest]
     * is called.
     */
    val name: String?

    /**
     * A collection of [Tree]s in this [Forest] that were planted through
     * [plant] method.
     */
    val trees: List<Tree>

    /**
     * Plant the [tree] to this [Forest]. This [tree] will handle the log
     * from this [Forest].
     */
    fun plant(tree: Tree)

    /**
     * Cut the [tree] from this [Forest]. This [tree] will no longer handle
     * the log coming from this [Forest].
     */
    fun cut(tree: Tree)

    /**
     * Clear up all the [Tree]s in this [Forest].
     */
    fun deforest()

    /**
     * Set the [PreProcessLogCallback] to this [Forest]. This will allow the
     * [Forest] to send the [LogEntry] every time before it's sent to the
     * [Tree]s.
     * You can use this callback to filter or modify a [LogEntry] before the
     * [LogEntry] is being sent to the [Tree]s in this [Forest].
     *
     * @param callback The [PreProcessLogCallback] to be set into this [Forest].
     */
    fun preProcessLog(callback: PreProcessLogCallback)

    /**
     * Log a message with [Level.VERBOSE] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun v(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.VERBOSE] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun v(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.VERBOSE] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun v(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.DEBUG] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun d(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.DEBUG] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun d(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.DEBUG] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun d(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.INFO] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun i(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.INFO] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun i(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.INFO] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun i(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.WARN] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun w(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.WARN] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun w(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.WARN] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun w(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.ERROR] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun e(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.ERROR] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun e(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.ERROR] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun e(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.FATAL] level.
     *
     * @param message The message to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun f(message: String, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.FATAL] level.
     *
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun f(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message with [Level.FATAL] level.
     *
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun f(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    /**
     * Log a message.
     *
     * @param level The level of this log message.
     * @param message The message to be logged.
     * @param throwable The [Throwable] to be logged.
     * @param attributes A collection of additional data to be added to this log.
     */
    fun log(
        level: Level,
        message: String?,
        throwable: Throwable?,
        attributes: Map<String, Any> = emptyMap()
    )

    /**
     * A singleton class that represents the global [Forest]. This global [Forest]
     * holds a collection of [Forest]s that are created through [getForest].
     *
     * Any changes to this global [Forest], except logging through this global
     * [Forest], will be forwarded to the collection of [Forest]s that this
     * global [Forest] holds. You can however set [Forest.allowGlobalOverride]
     * to `false` to disable this setting. By default [Forest.allowGlobalOverride]
     * is `true`.
     *
     * This global [Forest] has a special property called [context], which holds a
     * sharable data across all the [Tree]s every [Forest]s hold. This [context] can
     * be used to set global data that will be passed to every [Tree] every
     * [Forest]s this object hold.
     * You can add data to the [context] like the following example:
     * ```
     * Forest.context["key1"] = "value1"
     * Forest.context["key2"] = 123
     * ```
     * both examples do the same thing.
     */
    companion object Global : AbstractForest({ context }) {
        override var level: Level = VERBOSE
            set(value) {
                field = value
                forests.values.forEach { it.level = value }
            }

        /**
         * Gets the [ForestContext] this [Forest] is in.
         * The data that gets set in this context will be available to all the [trees] globally.
         */
        var context: ForestContext = ForestContext.createDataContext()
            private set

        /**
         * `true` to allow properties override from global [Forest] to other
         * [Forest]s, `false` otherwise.
         */
        var allowGlobalOverride = true

        private val forests = ConcurrentHashMap<String, AbstractForest>()

        /**
         * Get or create a [Forest] with the given [name].
         *
         * You can configure the [Forest] by using [configure] if this method is
         * creating the [Forest], otherwise the [configure] will not apply the
         * configuration to the returning [Forest].
         *
         * @param name The name of the [Forest] to be returned.
         * @param configure To configure the returning [Forest] if it's being
         * created from this method.
         * @return A [Forest] that has name equals to the [name] parameter.
         */
        @JvmOverloads
        fun getForest(name: String = "", configure: (ForestConfig.() -> Unit) = {}): Forest {
            val forest = forests.getOrPut(name) {
                RealForest(name).also { newForest ->
                    newForest.level = level
                    newForest.preProcessLogCallback = preProcessLogCallback
                    trees.forEach { tree -> newForest.plant(tree) }
                }
            }

            val config = ForestConfig(
                forest.level,
                forest.preProcessLogCallback,
                allowGlobalOverride,
                forest.trees.toMutableList()
            )
            configure(config)
            forest.updateWithConfig(config)

            return forest
        }

        /**
         * Get or create a [Forest] with the given [clazz].
         * This method will use the canonical name of the [clazz] if it is not
         * `null` or empty, otherwise it will use the package name of the [clazz],
         * such as from an anonymous class.
         *
         * You can configure the [Forest] by using [configure] if this method is
         * creating the [Forest], otherwise the [configure] will not apply the
         * configuration to the returning [Forest].
         *
         * @param clazz The class that is going to use the returning [Forest].
         * @param configure To configure the returning [Forest] if it's being
         * created from this method.
         * @return A [Forest] that has a name registered with the given [clazz].
         */
        @JvmOverloads
        fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
            return getForest(clazz.name, configure)
        }

        /**
         * Change this global [Forest]'s [ForestContext].
         *
         * @param newContext The new [ForestContext] that store the data that are passed
         * to all the [Tree]s.
         */
        fun changeContext(newContext: ForestContext) {
            context = newContext
        }

        override fun deforest() {
            super.deforest()
            forests.clear()
        }

        override fun plant(tree: Tree) {
            super.plant(tree)
            forests.values.forEach { forest -> forest.tryPlant(tree) }
        }

        override fun cut(tree: Tree) {
            super.cut(tree)
            forests.values.forEach { forest -> forest.tryCut(tree) }
        }

        private class RealForest(name: String?) : AbstractForest({ context }, name)
    }
}

/**
 * Get or create a [Forest] with the given [name].
 *
 * You can configure the [Forest] by using [configure] if this method is
 * creating the [Forest], otherwise the [configure] will not apply the
 * configuration to the returning [Forest].
 *
 * @param name The name of the [Forest] to be returned.
 * @param configure To configure the returning [Forest] if it's being
 * created from this method.
 * @return A [Forest] that has name equals to the [name] parameter.
 */
fun getForest(name: String = "", configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(name, configure)
}

/**
 * Get or create a [Forest] with the given [clazz].
 * This method will use the canonical name of the [clazz] if it is not
 * `null` or empty, otherwise it will use the package name of the [clazz],
 * such as from an anonymous class.
 *
 * You can configure the [Forest] by using [configure] if this method is
 * creating the [Forest], otherwise the [configure] will not apply the
 * configuration to the returning [Forest].
 *
 * @param clazz The class that is going to use the returning [Forest].
 * @param configure To configure the returning [Forest] if it's being
 * created from this method.
 * @return A [Forest] that has a name registered with the given [clazz].
 */
fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(clazz, configure)
}