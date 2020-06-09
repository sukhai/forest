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

package big.forest.context

import big.forest.Forest
import big.forest.Tree

/**
 * A modifiable collection that holds pairs of objects that use by [Forest] and [Tree].
 *
 * @sample DataContext
 */
interface Context {
    /**
     * A factory class that create different types of [Context].
     */
    companion object Factory {
        /**
         * Create an instance of [DataContext].
         *
         * @return A new instance of [DataContext].
         */
        fun createDataContext() = DataContext()
    }

    /**
     * Return the number of data in the context.
     */
    val size: Int

    /**
     * Return all the keys from the context.
     */
    val keys: Set<String>

    /**
     * Return all the values from the context.
     */
    val values: Collection<Any>

    /**
     * Check if this context contains the given [key].
     *
     * @param key The key to be checked.
     * @return `true` if this context contains a data with the given [key],
     * `false` otherwise.
     */
    fun containsKey(key: String): Boolean

    /**
     * Check if this context contains the given [value].
     *
     * @param value The value to be checked.
     * @return `true` if this context contains a data with the given [value],
     * `false` otherwise.
     */
    fun containsValue(value: Any): Boolean

    /**
     * Return a value with the given [key] found in the context.
     *
     * @param key The key to the value.
     * @return The value that is associated with the given [key] if found,
     * otherwise return `null`.
     */
    fun get(key: String): Any?

    /**
     * Check if this context is empty.
     *
     * @return `true` if this context is empty and contains no data, `false`
     * otherwise.
     */
    fun isEmpty(): Boolean

    /**
     * Clear all the data from the context.
     */
    fun clear()

    /**
     * Maps the given [key] to the specified value in this context.
     * Neither the key nor the value can be null.
     * The value can be retrieved by calling the get method with a key that
     * is equal to the original key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value which is associated with the [key].
     * @return The previous value associated with key, or null if there was
     * no mapping for key.
     */
    fun put(key: String, value: Any): Any?

    /**
     * Removes the [key] (and its corresponding value) from this context.
     * This method does nothing if the [key] is not in the context.
     *
     * @param key The key and its corresponding value to be removed from
     * this context.
     * @return The previous value associated with key, or null if there was
     * no mapping for key.
     */
    fun remove(key: String): Any?

    /**
     * Set a callback to the [Context].
     *
     * @param listener The callback listener to be set.
     */
    fun setOnModifiedListener(listener: (ModifiedState) -> Unit)

    /**
     * Remove the modified callback listener from the [Context].
     */
    fun removeOnModifiedListener()

    /**
     * Maps the given [key] to the specified value in this context.
     * Neither the key nor the value can be null.
     * The value can be retrieved by calling the get method with a key that
     * is equal to the original key.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value which is associated with the [key].
     * @return The previous value associated with key, or null if there was
     * no mapping for key.
     */
    operator fun set(key: String, value: Any) {
        put(key, value)
    }
}