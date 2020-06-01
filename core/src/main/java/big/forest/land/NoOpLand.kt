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

package big.forest.land

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