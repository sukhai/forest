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
interface Context : MutableMap<String, Any> {
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
}