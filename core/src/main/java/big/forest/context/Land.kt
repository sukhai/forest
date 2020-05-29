package big.forest.context

interface Land : MutableMap<String, Any> {
    companion object Factory {
        fun createNoOpsLand() = NoOpLand()
        fun createDataLand() = DataLand()
    }

    infix fun String.to(that: Any) {
        put(this, that)
    }
}