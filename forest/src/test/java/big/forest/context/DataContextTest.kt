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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DataContextTest {
    private lateinit var context: DataContext
    private val expectedSize = 2

    @BeforeEach
    fun setup() {
        context = Context.createDataContext()

        for (i in 1..expectedSize) {
            context["key$i"] = "value$i"
        }
    }

    @Test
    fun `size will return correct value`() {
        assertEquals(expectedSize, context.size)
    }

    @Test
    fun `keys will return correct value`() {
        assertEquals(expectedSize, context.keys.size)
        assertTrue(context.keys.contains("key1"))
        assertTrue(context.keys.contains("key2"))
    }

    @Test
    fun `values will return correct value`() {
        assertEquals(expectedSize, context.values.size)
        assertTrue(context.values.contains("value1"))
        assertTrue(context.values.contains("value2"))
    }

    @Test
    fun `containsKey will return correct value`() {
        assertTrue(context.containsKey("key1"))
        assertTrue(context.containsKey("key2"))
        assertFalse(context.containsKey("key3"))
    }

    @Test
    fun `containsValue will return correct value`() {
        assertTrue(context.containsValue("value1"))
        assertTrue(context.containsValue("value2"))
        assertFalse(context.containsValue("value3"))
    }

    @Test
    fun `isEmpty will return correct value`() {
        assertFalse(context.isEmpty())
        assertEquals(expectedSize, context.size)

        val anotherContext = Context.createDataContext()
        assertTrue(anotherContext.isEmpty())
        assertEquals(0, anotherContext.size)
    }

    @Test
    fun `clear will remove all data`() {
        assertFalse(context.isEmpty())

        context.clear()
        assertTrue(context.isEmpty())
        assertEquals(0, context.size)
    }

    @Test
    fun `putAll will add all data into the context`() {
        val testContext = Context.createDataContext()

        assertTrue(testContext.isEmpty())
        assertEquals(0, testContext.size)

        testContext["key a"] = "value a"
        testContext["key b"] = 2

        assertFalse(testContext.isEmpty())
        assertEquals(2, testContext.size)
    }

    @Test
    fun `remove will remove a data from the context`() {
        assertTrue(context.containsKey("key1"))

        context.remove("key1")

        assertFalse(context.containsKey("key1"))
    }

    @Test
    fun `toString will return a string contains all key-value pairs`() {
        val expected = "{key1=value1, key2=123}"

        context["key1"] = "value1"
        context["key2"] = 123

        assertEquals(expected, context.toString())
    }

    @Test
    fun `add new value to the Context will notify listener`() {
        val expectedKey = "key"
        val expectedValue = "new value"
        var actualKey: String? = null
        var actualValue: Any? = null

        context.clear()
        context.setOnModifiedListener {
            when (it) {
                is ModifiedState.New -> {
                    actualKey = it.key
                    actualValue = it.value
                }
            }
        }

        context[expectedKey] = expectedValue

        assertEquals(expectedKey, actualKey)
        assertEquals(expectedValue, actualValue)
    }

    @Test
    fun `update existing value to the Context will notify listener`() {
        val expectedKey = "key"
        val expectedOldValue = "old value"
        val expectedNewValue = "new value"
        var actualKey: String? = null
        var actualOldValue: Any? = null
        var actualNewValue: Any? = null

        context.clear()
        context[expectedKey] = expectedOldValue

        context.setOnModifiedListener {
            when (it) {
                is ModifiedState.Updated -> {
                    actualKey = it.key
                    actualOldValue = it.oldValue
                    actualNewValue = it.newValue
                }
            }
        }

        context[expectedKey] = expectedNewValue

        assertEquals(expectedKey, actualKey)
        assertEquals(expectedNewValue, actualNewValue)
        assertEquals(expectedOldValue, actualOldValue)
    }

    @Test
    fun `remove existing value from the Context will notify listener`() {
        val expectedKey = "key"
        val expectedValue = "old value"
        var actualKey: String? = null
        var actualValue: Any? = null

        context.clear()
        context[expectedKey] = expectedValue

        context.setOnModifiedListener {
            when (it) {
                is ModifiedState.Removed -> {
                    actualKey = it.key
                    actualValue = it.value
                }
            }
        }

        context.remove(expectedKey)

        assertEquals(expectedKey, actualKey)
        assertEquals(expectedValue, actualValue)
    }
}