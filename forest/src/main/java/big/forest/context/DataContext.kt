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

    override fun toString(): String {
        return map.toString()
    }
}