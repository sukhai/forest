package big.forest.context

/**
 * The state of the [Context] modification.
 *
 * @param key The key with which the specified value is to be associated.
 */
sealed class ModifiedState(open val key: String) {
    /**
     * A new key-value data has been added to the [Context].
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The new value which is added to the [Context] and
     * is associated with the [key].
     */
    data class New(
        override val key: String,
        val value: Any
    ) : ModifiedState(key)

    /**
     * A key-value data has been updated from the [Context].
     *
     * @param key The key with which the specified value is to be associated.
     * @param oldValue The previous value which is replaced with the
     * [newValue] and is associated with the [key].
     * @param newValue The new value which is replacing the [oldValue] and
     * is associated with the [key].
     */
    data class Updated(
        override val key: String,
        val oldValue: Any,
        val newValue: Any
    ) : ModifiedState(key)

    /**
     * A key-value data has been removed from the [Context].
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The previous value which is removed from the [Context] and
     * is associated with the [key].
     */
    data class Removed(
        override val key: String,
        val value: Any
    ) : ModifiedState(key)
}