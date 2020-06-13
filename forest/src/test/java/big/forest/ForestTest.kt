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

import big.forest.context.ForestContext
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ForestTest {
    private val expectedMessage = "this is a message"
    private val expectedTag = "a forest"
    private val expectedThrowable = Exception("Test exception")
    private val expectedAttributes = mapOf<String, Any>()

    @BeforeEach
    fun setup() {
        Forest.level = Forest.Level.VERBOSE
    }

    @AfterEach
    fun tearDown() {
        Forest.preProcessLog { it }
        Forest.deforest()
    }

    @Test
    fun `getForest - when passing a class to parameter, then return Forest with canonical name`() {
        val forest1 = getForest(ForestTest::class.java)
        assertEquals("big.forest.ForestTest", forest1.name)

        val forest2 = Forest.getForest(ForestTest::class.java)
        assertEquals("big.forest.ForestTest", forest2.name)
    }

    @Test
    fun `getForest - when passing an anonymous class to parameter, then return Forest with package name`() {
        val anonymous = object : Dummy {}
        val forest = getForest(anonymous::class.java)
        assertEquals(
            "big.forest.ForestTest\$getForest - when passing an anonymous class to parameter, then return Forest with package name\$anonymous$1",
            forest.name
        )
    }

    @Test
    fun `getForest - when passing a name to parameter, then return Forest with the given name`() {
        val name = "a name"
        val forest1 = getForest(name)
        assertEquals(name, forest1.name)

        val forest2 = Forest.getForest(name)
        assertEquals(name, forest2.name)
    }

    @Test
    fun `getForest - when provide different names to multiple calls, then return different Forest`() {
        val forestA = getForest("forestA")
        val forestB = getForest("forestB")
        assertNotEquals(forestA, forestB)
    }

    @Test
    fun `getForest - when provide the same name to multiple calls, then return the same Forest`() {
        val forest = getForest("forest")
        2.count().forEach { _ ->
            val anotherCall = getForest("forest")
            assertEquals(forest, anotherCall)
        }
    }

    @Test
    fun `getForest - when properties are set in global Forest and when create new Forest, then pass properties to the new Forest`() {
        val expectedLevel = Forest.Level.ERROR
        val expectedTree: Tree = mock()
        Forest.plant(expectedTree)

        val forest = getForest { level = expectedLevel }

        assertEquals(expectedLevel, forest.level)
        assertEquals(expectedTree, forest.trees[0])
    }

    @Test
    fun `when plant a tree to global Forest, then the tree is added to all Forests`() {
        val tree: Tree = mock()
        val forestA = getForest("forestA")
        val forestB = getForest("forestB")

        assertFalse(Forest.trees.contains(tree))
        assertFalse(forestA.trees.contains(tree))
        assertFalse(forestB.trees.contains(tree))

        Forest.plant(tree)
        assertTrue(Forest.trees.contains(tree))
        assertTrue(forestA.trees.contains(tree))
        assertTrue(forestB.trees.contains(tree))
    }

    @Test
    fun `we can plant multiple trees to the global Forest`() {
        assertTrue(Forest.trees.isEmpty())

        val trees = List<Tree>(5) { mock() }
            .apply {
                forEach { Forest.plant(it) }
            }
            .apply {
                forEach { tree ->
                    assertTrue(Forest.trees.contains(tree))
                }
            }

        assertEquals(trees.size, Forest.trees.size)
    }

    @Test
    fun `when plant a tree to global Forest, then only add tree to other Forests if the Forest does not have the tree`() {
        val tree: Tree = mock()
        val forest = getForest()

        5.count().forEach { _ -> Forest.plant(tree) }

        assertEquals(1, forest.trees.size)
    }

    @Test
    fun `when cut a tree from a Forest, then the tree will be removed`() {
        val tree: Tree = mock()
        val forest = getForest { plant(tree) }

        assertTrue(forest.trees.contains(tree))

        forest.cut(tree)
        assertFalse(forest.trees.contains(tree))
    }

    @Test
    fun `when cut a tree from the global Forest, then the tree will be removed from all Forests`() {
        val tree: Tree = mock()
        val forest = getForest { plant(tree) }

        assertTrue(forest.trees.contains(tree))

        Forest.cut(tree)
        assertFalse(forest.trees.contains(tree))
    }

    @Test
    fun `when log message using a Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val forest = getForest(expectedTag) {
            level = Forest.Level.VERBOSE
            plant(tree)
        }

        forest.v(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE, throwable = null))

        forest.d(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG, throwable = null))

        forest.i(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO, throwable = null))

        forest.w(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN, throwable = null))

        forest.e(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR, throwable = null))

        forest.f(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL, throwable = null))

        forest.log(Forest.Level.OFF, expectedMessage, expectedThrowable)
        verify(tree, never()).log(createExpectedLogEntry(Forest.Level.OFF))
    }

    @Test
    fun `when log message using the global Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val globalForestTag: String? = null

        Forest.plant(tree)
        Forest.level = Forest.Level.VERBOSE

        Forest.v(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.VERBOSE,
                tag = globalForestTag,
                throwable = null
            )
        )

        Forest.d(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.DEBUG,
                tag = globalForestTag,
                throwable = null
            )
        )

        Forest.i(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.INFO,
                tag = globalForestTag,
                throwable = null
            )
        )

        Forest.w(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.WARN,
                tag = globalForestTag,
                throwable = null
            )
        )

        Forest.e(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.ERROR,
                tag = globalForestTag,
                throwable = null
            )
        )

        Forest.f(expectedMessage)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.FATAL,
                tag = globalForestTag,
                throwable = null
            )
        )
    }

    @Test
    fun `when log throwable using a Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val forest = getForest(expectedTag) {
            level = Forest.Level.VERBOSE
            plant(tree)
        }

        forest.v(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE, message = null))

        forest.d(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG, message = null))

        forest.i(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO, message = null))

        forest.w(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN, message = null))

        forest.e(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR, message = null))

        forest.f(expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL, message = null))
    }

    @Test
    fun `when log throwable using the global Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val globalForestTag = ForestTest::class.java.canonicalName

        Forest.plant(tree)
        Forest.level = Forest.Level.VERBOSE

        Forest.v(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.VERBOSE,
                tag = globalForestTag,
                message = null
            )
        )

        Forest.d(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.DEBUG,
                tag = globalForestTag,
                message = null
            )
        )

        Forest.i(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.INFO,
                tag = globalForestTag,
                message = null
            )
        )

        Forest.w(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.WARN,
                tag = globalForestTag,
                message = null
            )
        )

        Forest.e(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.ERROR,
                tag = globalForestTag,
                message = null
            )
        )

        Forest.f(expectedThrowable)
        verify(tree).log(
            createExpectedLogEntry(
                Forest.Level.FATAL,
                tag = globalForestTag,
                message = null
            )
        )
    }

    @Test
    fun `when log message and throwable using a Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val forest = getForest(expectedTag) {
            level = Forest.Level.VERBOSE
            plant(tree)
        }

        forest.v(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE))

        forest.d(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG))

        forest.i(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO))

        forest.w(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN))

        forest.e(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR))

        forest.f(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL))
    }

    @Test
    fun `when log message and throwable using the global Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val globalForestTag = ForestTest::class.java.canonicalName

        Forest.plant(tree)
        Forest.level = Forest.Level.VERBOSE

        Forest.v(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE, tag = globalForestTag))

        Forest.d(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG, tag = globalForestTag))

        Forest.i(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO, tag = globalForestTag))

        Forest.w(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN, tag = globalForestTag))

        Forest.e(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR, tag = globalForestTag))

        Forest.f(expectedMessage, expectedThrowable)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL, tag = globalForestTag))
    }

    @Test
    fun `when the level is OFF, then the tree will not receive LogEntry`() {
        val tree: Tree = mock()
        val message = "a message"
        Forest.level = Forest.Level.OFF
        Forest.plant(tree)

        Forest.v(message)
        Forest.d(message)
        Forest.i(message)
        Forest.w(message)
        Forest.e(message)
        Forest.f(message)

        verify(tree, never()).log(any())
    }

    @Test
    fun `when log an exception from a class without a logger name, then set tag to first stacktrace class name`() {
        val tree: Tree = mock()
        val forest = getForest {
            level = Forest.Level.VERBOSE
            plant(tree)
        }
        val exception = Exception("an error")

        forest.e(exception)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.ERROR,
                message = exception.message ?: "",
                tag = ForestTest::class.java.canonicalName,
                throwable = exception
            )
        )
    }

    @Test
    fun `when log an exception from a class with a logger name, then set tag to the logger name`() {
        val expectedName = "a name"
        val tree: Tree = mock()
        val forest = getForest(expectedName) {
            level = Forest.Level.VERBOSE
            plant(tree)
        }
        val exception = Exception("an error")

        forest.e(exception)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.ERROR,
                message = exception.message ?: "",
                tag = expectedName,
                throwable = exception
            )
        )
    }

    @Test
    fun `when a level is set to Forest and log methods are called, then the tree will only receive LogEntry if the level is less than the triggered level`() {
        val tree: Tree = mock()
        var testLevel = Forest.Level.OFF
        val callback = object : PreProcessLogCallback {
            override fun invoke(entry: LogEntry): LogEntry? {
                assertTrue(testLevel.ordinal >= entry.level.ordinal) {
                    "We should not log ${entry.level} when Forest level is set to $testLevel"
                }
                return entry
            }
        }
        Forest.plant(tree)
        Forest.preProcessLog(callback)

        fun test(level: Forest.Level, log: () -> Unit) {
            testLevel = level
            Forest.level = level
            log()
        }

        test(Forest.Level.OFF) { Forest.v(expectedMessage) }
        test(Forest.Level.VERBOSE) { Forest.v(expectedMessage) }
        test(Forest.Level.VERBOSE) { Forest.d(expectedMessage) }
        test(Forest.Level.DEBUG) { Forest.d(expectedMessage) }
        test(Forest.Level.DEBUG) { Forest.v(expectedMessage) }
        test(Forest.Level.DEBUG) { Forest.i(expectedMessage) }
        test(Forest.Level.INFO) { Forest.i(expectedMessage) }
        test(Forest.Level.INFO) { Forest.d(expectedMessage) }
        test(Forest.Level.INFO) { Forest.w(expectedMessage) }
        test(Forest.Level.WARN) { Forest.w(expectedMessage) }
        test(Forest.Level.WARN) { Forest.i(expectedMessage) }
        test(Forest.Level.WARN) { Forest.e(expectedMessage) }
        test(Forest.Level.ERROR) { Forest.e(expectedMessage) }
        test(Forest.Level.ERROR) { Forest.w(expectedMessage) }
        test(Forest.Level.ERROR) { Forest.f(expectedMessage) }
        test(Forest.Level.FATAL) { Forest.f(expectedMessage) }
        test(Forest.Level.FATAL) { Forest.e(expectedMessage) }
    }

    @Test
    fun `when set a new context, then the tree will receive this new context`() {
        val tree: Tree = mock()
        val newContext: ForestContext = mock()
        Forest.changeContext(newContext)
        Forest.plant(tree)
        Forest.d(expectedMessage)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.DEBUG,
                message = expectedMessage,
                context = newContext
            )
        )
    }

    @Test
    fun `when update the context, then the tree will receive the latest update from the context`() {
        val tree: Tree = mock()
        val context = ForestContext.createDataContext()
        Forest.changeContext(context)
        Forest.plant(tree)
        Forest.context["key 1"] = "value 1"
        Forest.context["key 2"] = 123
        Forest.d(expectedMessage)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.DEBUG,
                message = expectedMessage,
                context = context
            )
        )
        assertTrue(context.containsKey("key 1"))
        assertTrue(context.containsKey("key 2"))
        assertEquals("value 1", context["key 1"])
        assertEquals(123, context["key 2"])
    }

    @Test
    fun `when allowGlobalOverride is set to false, then local Forest will not get overrided`() {
        val expectedLevel = Forest.Level.ERROR
        val forest = getForest {
            allowGlobalOverride = false
            level = expectedLevel
        }

        Forest.level = Forest.Level.DEBUG
        Forest.plant(mock())

        assertEquals(expectedLevel, forest.level)
        assertTrue(forest.trees.isEmpty())
    }

    private fun createExpectedLogEntry(
        level: Forest.Level,
        message: String? = expectedMessage,
        throwable: Throwable? = expectedThrowable,
        tag: String? = expectedTag
    ) = LogEntry(
        level = level,
        message = message ?: throwable?.message ?: "",
        throwable = throwable,
        tag = tag,
        attributes = expectedAttributes
    )

    private fun Int.count() = downTo(0)
}

private interface Dummy