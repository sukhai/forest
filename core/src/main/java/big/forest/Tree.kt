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

package big.forest

/**
 * A handler to all the logs that sent to [Forest].
 *
 * The implementation class will just have to implement the only method in this interface,
 * which is [log].
 *
 * Whenever [Forest.log]-type method is called, it will construct a [LogEntry] and delegate
 * the actual handling to the [log] method in this class.
 */
interface Tree {
    /**
     * A method to handle the given log [entry] that was sent from [Forest] or caller of
     * this class.
     *
     * @param entry The [LogEntry] that contains the information of the log.
     */
    fun log(entry: LogEntry)
}