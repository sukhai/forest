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

import java.util.concurrent.ConcurrentHashMap

/**
 * A type of [Context] that store data in a [ConcurrentHashMap].
 *
 * You can instantiate an instance of this class through [Context.createDataContext].
 */
class DataContext internal constructor() : Context {
    private val map = ConcurrentHashMap<String, Any>()
    private var listener: ((ModifiedState) -> Unit)? = null

    override val size: Int
        get() = map.size

    override val keys: Set<String>
        get() = map.keys

    override val values: Collection<Any>
        get() = map.values

    override fun containsKey(key: String): Boolean {
        return map.containsKey(key)
    }

    override fun containsValue(value: Any): Boolean {
        return map.containsValue(value)
    }

    override operator fun get(key: String): Any? {
        return map[key]
    }

    override fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    override fun clear() {
        map.clear()
    }

    override fun put(key: String, value: Any): Any? {
        val old = map.put(key, value)
        listener?.invoke(
            if (old == null) {
                ModifiedState.New(key, value)
            } else {
                ModifiedState.Updated(key, old, value)
            }
        )
        return old
    }

    override fun remove(key: String): Any? {
        val old = map.remove(key)
        if (old != null && listener != null) {
            listener?.invoke(ModifiedState.Removed(key, old))
        }
        return old
    }

    override fun setOnModifiedListener(listener: (ModifiedState) -> Unit) {
        this.listener = listener
    }

    override fun removeOnModifiedListener() {
        listener = null
    }

    override fun toString(): String {
        return map.toString()
    }
}