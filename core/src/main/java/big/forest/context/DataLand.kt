package big.forest.context

import java.util.concurrent.ConcurrentHashMap

class DataLand internal constructor() : Land {
    private val map = ConcurrentHashMap<String, Any>()

    override val size: Int
        get() = map.size

    override val entries: MutableSet<MutableMap.MutableEntry<String, Any>>
        get() = map.entries

    override val keys: MutableSet<String>
        get() = map.keys

    override val values: MutableCollection<Any>
        get() = map.values

    override fun containsKey(key: String): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: Any): Boolean {
        return map.containsValue(value)
    }

    override fun get(key: String): Any? {
        return map[key]
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun clear() {
        map.clear()
    }

    override fun put(key: String, value: Any): Any? {
        return map.put(key, value)
    }

    override fun putAll(from: Map<out String, Any>) {
        map.putAll(from)
    }

    override fun remove(key: String): Any? {
        return map.remove(key)
    }
}