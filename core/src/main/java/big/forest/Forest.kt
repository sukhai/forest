package big.forest

import java.util.concurrent.ConcurrentHashMap

typealias PreProcessLogCallback = (LogEntry) -> LogEntry?

interface Forest {
    enum class Level {
        OFF,
        FATAL,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        VERBOSE
    }

    var level: Level
    var name: String?
    val trees: List<Tree>

    fun plant(tree: Tree)

    fun cut(tree: Tree)

    fun clearTrees()

    fun preProcessLog(callback: PreProcessLogCallback)

    fun v(message: String, attributes: Map<String, Any> = emptyMap())

    fun v(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun v(throwable: Throwable, attributes: Map<String, Any>)

    fun d(message: String, attributes: Map<String, Any> = emptyMap())

    fun d(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun d(throwable: Throwable, attributes: Map<String, Any>)

    fun i(message: String, attributes: Map<String, Any> = emptyMap())

    fun i(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun i(throwable: Throwable, attributes: Map<String, Any>)

    fun w(message: String, attributes: Map<String, Any> = emptyMap())

    fun w(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun w(throwable: Throwable, attributes: Map<String, Any>)

    fun e(message: String, attributes: Map<String, Any> = emptyMap())

    fun e(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun e(throwable: Throwable, attributes: Map<String, Any>)

    fun f(message: String, attributes: Map<String, Any> = emptyMap())

    fun f(message: String, throwable: Throwable, attributes: Map<String, Any>)

    fun f(throwable: Throwable, attributes: Map<String, Any>)

    fun log(
        level: Level,
        message: String?,
        throwable: Throwable?,
        attributes: Map<String, Any> = emptyMap()
    )

    companion object : AbstractForest() {
        private val forests = ConcurrentHashMap<String, Forest>()

        fun getForest(name: String, configure: (ForestConfig.() -> Unit) = {}): Forest {
            val forest = forests.getOrPut(name) {
                RealForest().also { newForest ->
                    newForest.level = level
                    newForest.name = name
                    newForest.preProcessLogCallback = preProcessLogCallback
                    trees.forEach { tree -> newForest.plant(tree) }
                }
            } as AbstractForest

            val config = ForestConfig(
                forest.level,
                forest.preProcessLogCallback,
                forest.trees.toMutableList()
            )
            configure(config)
            forest.apply(config)

            return forest
        }

        fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
            val name = clazz.canonicalName ?: clazz.`package`.name ?: ""
            return getForest(name, configure)
        }

        fun clearForests() {
            forests.clear()
        }

        override fun plant(tree: Tree) {
            super.plant(tree)
            forests.values.forEach {
                val forest = it as AbstractForest
                val t = forest.allTrees.toMutableList()
                if (!t.contains(tree)) {
                    t.add(tree)
                }
                forest.allTrees = t
            }
        }

        override fun cut(tree: Tree) {
            super.cut(tree)
            forests.values.forEach {
                val forest = it as AbstractForest
                val t = forest.allTrees.toMutableList()
                t.remove(tree)
                forest.allTrees = t
            }
        }

        private fun Forest.apply(config: ForestConfig) {
            level = config.level
            preProcessLogCallback = config.preProcessLog
            allTrees = config.trees.toList()
        }

        internal class RealForest : AbstractForest()
    }
}

fun getForest(name: String, configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(name, configure)
}

fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(clazz, configure)
}