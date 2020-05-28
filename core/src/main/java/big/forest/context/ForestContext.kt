package big.forest.context

interface ForestContext : MutableMap<String, Any> {
    companion object Factory {
        fun newNoOpsContext() = NoOpContext()
        fun newDataContext() = DataContext()
    }

    infix fun String.to(that: Any) {
        put(this, that)
    }
}