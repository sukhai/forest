package big.forest.context

/**
 * A type of [Land] that does not provide any operation on any of its methods.
 * You can create an instance of this class through [Land.createNoOpsLand].
 */
class NoOpLand internal constructor() : Land {
    override val size = 0

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any>> = mutableSetOf()

    override val keys: MutableSet<String> = mutableSetOf()

    override val values: MutableCollection<Any> = mutableListOf()

    override fun containsKey(key: String) = false

    override fun containsValue(value: Any) = false

    override fun get(key: String): Any? = null

    override fun isEmpty() = false

    override fun clear() {}

    override fun put(key: String, value: Any): Any? = null

    override fun putAll(from: Map<out String, Any>) {}

    override fun remove(key: String): Any? = null
}