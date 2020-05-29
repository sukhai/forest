package big.forest

import big.forest.Forest.Global.land
import big.forest.context.Land
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

    fun deforest()

    fun preProcessLog(callback: PreProcessLogCallback)

    fun v(message: String, attributes: Map<String, Any> = emptyMap())

    fun v(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun v(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun d(message: String, attributes: Map<String, Any> = emptyMap())

    fun d(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun d(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun i(message: String, attributes: Map<String, Any> = emptyMap())

    fun i(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun i(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun w(message: String, attributes: Map<String, Any> = emptyMap())

    fun w(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun w(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun e(message: String, attributes: Map<String, Any> = emptyMap())

    fun e(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun e(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun f(message: String, attributes: Map<String, Any> = emptyMap())

    fun f(message: String, throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun f(throwable: Throwable, attributes: Map<String, Any> = emptyMap())

    fun log(
        level: Level,
        message: String?,
        throwable: Throwable?,
        attributes: Map<String, Any> = emptyMap()
    )

    companion object Global : AbstractForest({ land }) {
        var land: Land = Land.createDataLand()
            private set
        private val forests = ConcurrentHashMap<String, AbstractForest>()

        fun getForest(name: String = "", configure: (ForestConfig.() -> Unit) = {}): Forest {
            val forest = forests.getOrPut(name) {
                RealForest().also { newForest ->
                    newForest.level = level
                    newForest.name = name
                    newForest.preProcessLogCallback = preProcessLogCallback
                    trees.forEach { tree -> newForest.plant(tree) }
                }
            }

            val config = ForestConfig(
                forest.level,
                forest.preProcessLogCallback,
                forest.trees.toMutableList()
            )
            configure(config)
            forest.updateWithConfig(config)

            return forest
        }

        fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
            return getForest(clazz.name(), configure)
        }

        fun changeContext(newContext: Land) {
            land = newContext
        }

        fun land(update: Land.() -> Unit) {
            update(land)
        }

        override fun deforest() {
            super.deforest()
            forests.clear()
        }

        override fun plant(tree: Tree) {
            super.plant(tree)
            forests.values.forEach { forest ->
                val t = forest.allTrees.toMutableList()
                if (!t.contains(tree)) {
                    t.add(tree)
                }
                forest.allTrees = t
            }
        }

        override fun cut(tree: Tree) {
            super.cut(tree)
            forests.values.forEach { forest ->
                val t = forest.allTrees.toMutableList()
                t.remove(tree)
                forest.allTrees = t
            }
        }

        private fun Class<*>.name(): String {
            var name = canonicalName
            if (!name.isNullOrBlank()) {
                return name
            }
            name = `package`?.name ?: ""
            name += if (name.isBlank()) simpleName else ""
            return if (name.length <= 1) "" else name
        }

        internal class RealForest : AbstractForest({ land })
    }
}

fun getForest(name: String = "", configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(name, configure)
}

fun getForest(clazz: Class<*>, configure: (ForestConfig.() -> Unit) = {}): Forest {
    return Forest.getForest(clazz, configure)
}