package big.forest.context

class NoOpContext internal constructor() : ForestContext {
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