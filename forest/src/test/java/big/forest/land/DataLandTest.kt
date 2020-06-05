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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DataLandTest {
    private lateinit var land: DataLand
    private val expectedSize = 2

    @BeforeEach
    fun setup() {
        land = Land.createDataLand()

        for (i in 1..expectedSize) {
            land["key$i"] = "value$i"
        }
    }

    @Test
    fun `size will return correct value`() {
        assertEquals(expectedSize, land.size)
    }

    @Test
    fun `entries will return correct value`() {
        assertEquals(expectedSize, land.entries.size)
    }

    @Test
    fun `keys will return correct value`() {
        assertEquals(expectedSize, land.keys.size)
        assertTrue(land.keys.contains("key1"))
        assertTrue(land.keys.contains("key2"))
    }

    @Test
    fun `values will return correct value`() {
        assertEquals(expectedSize, land.values.size)
        assertTrue(land.values.contains("value1"))
        assertTrue(land.values.contains("value2"))
    }

    @Test
    fun `containsKey will return correct value`() {
        assertTrue(land.containsKey("key1"))
        assertTrue(land.containsKey("key2"))
        assertFalse(land.containsKey("key3"))
    }

    @Test
    fun `containsValue will return correct value`() {
        assertTrue(land.containsValue("value1"))
        assertTrue(land.containsValue("value2"))
        assertFalse(land.containsValue("value3"))
    }

    @Test
    fun `isEmpty will return correct value`() {
        assertFalse(land.isEmpty())
        assertEquals(expectedSize, land.size)

        val anotherLand = Land.createDataLand()
        assertTrue(anotherLand.isEmpty())
        assertEquals(0, anotherLand.size)
    }

    @Test
    fun `clear will remove all data`() {
        assertTrue(land.isNotEmpty())

        land.clear()
        assertTrue(land.isEmpty())
        assertEquals(0, land.size)
    }

    @Test
    fun `putAll will add all data into the land`() {
        val testLand = Land.createDataLand()
        val map = mapOf(
            "key a" to "value a",
            "key b" to 2
        )

        assertTrue(testLand.isEmpty())
        assertEquals(0, testLand.size)

        testLand.putAll(map)

        assertTrue(testLand.isNotEmpty())
        assertEquals(2, testLand.size)
    }

    @Test
    fun `remove will remove a data from the land`() {
        assertTrue(land.containsKey("key1"))

        land.remove("key1")

        assertFalse(land.containsKey("key1"))
    }
}