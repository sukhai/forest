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

import big.forest.Forest
import big.forest.Tree

/**
 * A modifiable collection that holds pairs of objects that use by [Forest] and [Tree].
 *
 * @sample DataLand
 */
interface Land : MutableMap<String, Any> {
    /**
     * A factory class that create different types of [Land].
     */
    companion object Factory {
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