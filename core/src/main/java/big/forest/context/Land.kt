package big.forest.context

import big.forest.Forest
import big.forest.Tree

/**
 * A modifiable collection that holds pairs of objects that use by [Forest] and [Tree].
 */
interface Land : MutableMap<String, Any> {
    /**
     * A factory class that create different types of [Land].
     */
    companion object Factory {
        /**
         * Create an instance of [NoOpLand].
         *
         * @return A new instance of [NoOpLand].
         */
        fun createNoOpsLand() = NoOpLand()

        /**
         * Create an instance of [DataLand].
         *
         * @return A new instance of [DataLand].
         */
        fun createDataLand() = DataLand()
    }

    /**
     * Associates the specified key to the specified value in the map.
     *
     * @param that The value this key is associating to in the map.
     */
    infix fun String.to(that: Any) {
        put(this, that)
    }
}