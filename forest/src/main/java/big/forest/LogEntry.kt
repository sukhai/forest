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

import big.forest.context.Context

/**
 * An object that holds the information of a log.
 *
 * @param level The logging level of the log.
 * the [Forest] received a logging call.
 * @param context The [Context] that contains the data from the [Forest].
 * @param message The message to be logged.
 * @param tag The tag. The value is either the name of a [Forest] set from [getForest]
 * or the class name of the first stacktrace from the [throwable], if any.
 * @param throwable The [Throwable] to be logged.
 * @param attributes A collection of attributes to be added to this log.
 */
data class LogEntry(
    val level: Forest.Level,
    val context: Context = Forest.context,
    val message: String? = null,
    val tag: String? = null,
    val throwable: Throwable? = null,
    val attributes: Map<String, Any> = emptyMap()
)