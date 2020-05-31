package big.forest

import big.forest.context.Land
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
    private val expectedThreadId = 123L
    private val expectedTimestamp = 234L
    private val expectedMessage = "this is a message"
    private val expectedTag = "a forest"

    @BeforeEach
    fun setup() {
        Forest.level = Forest.Level.VERBOSE
    }

    @AfterEach
    fun tearDown() {
        Forest.deforest()
    }

    @Test
    fun `getForest - when passing a class to parameter, then return Forest with canonical name`() {
        val forest = getForest(ForestTest::class.java)
        assertEquals("big.forest.ForestTest", forest.name)
    }

    @Test
    fun `getForest - when passing an anonymous class to parameter, then return Forest with package name`() {
        val anonymous = object : Dummy {}
        val forest = getForest(anonymous::class.java)
        assertEquals("big.forest", forest.name)
    }

    @Test
    fun `getForest - when passing a name to parameter, then return Forest with the given name`() {
        val name = "a name"
        val forest = getForest(name)
        assertEquals(name, forest.name)
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
    fun `when we plant a tree to global Forest, then the tree is added to all Forests`() {
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
    fun `when we plant a tree to global Forest, then only add tree to other Forests if the Forest does not have the tree`() {
        val tree: Tree = mock()
        val forest = getForest()

        5.count().forEach { _ -> Forest.plant(tree) }

        assertEquals(1, forest.trees.size)
    }

    @Test
    fun `when we cut a tree from a Forest, then the tree will be removed`() {
        val tree: Tree = mock()
        val forest = getForest { plant(tree) }

        assertTrue(forest.trees.contains(tree))

        forest.cut(tree)
        assertFalse(forest.trees.contains(tree))
    }

    @Test
    fun `when we cut a tree from the global Forest, then the tree will be removed from all Forests`() {
        val tree: Tree = mock()
        val forest = getForest { plant(tree) }

        assertTrue(forest.trees.contains(tree))

        Forest.cut(tree)
        assertFalse(forest.trees.contains(tree))
    }

    @Test
    fun `when we log using a Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val forest = getForest(expectedTag) {
            level = Forest.Level.VERBOSE
            preProcessLog = createTestPreProcessLogCallback()
            plant(tree)
        }

        forest.v(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE))

        forest.d(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG))

        forest.i(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO))

        forest.w(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN))

        forest.e(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR))

        forest.f(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL))
    }

    @Test
    fun `when we log using the global Forest, then the tree will receive correct LogEntry`() {
        val tree: Tree = mock()
        val globalForestTag: String? = null

        Forest.plant(tree)
        Forest.level = Forest.Level.VERBOSE
        Forest.preProcessLog(createTestPreProcessLogCallback())

        Forest.v(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.VERBOSE, globalForestTag))

        Forest.d(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.DEBUG, globalForestTag))

        Forest.i(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.INFO, globalForestTag))

        Forest.w(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.WARN, globalForestTag))

        Forest.e(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.ERROR, globalForestTag))

        Forest.f(expectedMessage)
        verify(tree).log(createExpectedLogEntry(Forest.Level.FATAL, globalForestTag))
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
        val expectedThreadId = 123L
        val expectedTimestamp = 234L
        val tree: Tree = mock()
        val forest = getForest {
            level = Forest.Level.VERBOSE
            plant(tree)
            preProcessLog = createTestPreProcessLogCallback()
        }
        val exception = Exception()

        forest.e(exception)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.ERROR,
                tag = ForestTest::class.java.canonicalName,
                throwable = exception,
                timestamp = expectedTimestamp,
                threadId = expectedThreadId
            )
        )
    }

    @Test
    fun `when log an exception from a class with a logger name, then set tag to the logger name`() {
        val expectedName = "a name"
        val expectedThreadId = 123L
        val expectedTimestamp = 234L
        val tree: Tree = mock()
        val forest = getForest(expectedName) {
            level = Forest.Level.VERBOSE
            plant(tree)
            preProcessLog = createTestPreProcessLogCallback()
        }
        val exception = Exception()

        forest.e(exception)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.ERROR,
                tag = expectedName,
                throwable = exception,
                timestamp = expectedTimestamp,
                threadId = expectedThreadId
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
    fun `when move to a new land, then the tree will receive this new land`() {
        val tree: Tree = mock()
        val newLand: Land = mock()
        Forest.moveTo(newLand)
        Forest.plant(tree)
        Forest.preProcessLog(createTestPreProcessLogCallback())
        Forest.d(expectedMessage)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.DEBUG,
                threadId = expectedThreadId,
                timestamp = expectedTimestamp,
                message = expectedMessage,
                land = newLand
            )
        )
    }

    @Test
    fun `when update the land, then the tree will receive the latest update from the land`() {
        val tree: Tree = mock()
        val land = Land.createDataLand()
        Forest.moveTo(land)
        Forest.plant(tree)
        Forest.preProcessLog(createTestPreProcessLogCallback())
        Forest.updateLand {
            "key 1" to "value 1"
            "key 2" to 123
        }
        Forest.d(expectedMessage)

        verify(tree).log(
            LogEntry(
                level = Forest.Level.DEBUG,
                threadId = expectedThreadId,
                timestamp = expectedTimestamp,
                message = expectedMessage,
                land = land
            )
        )
        assertTrue(land.containsKey("key 1"))
        assertTrue(land.containsKey("key 2"))
        assertEquals("value 1", land["key 1"])
        assertEquals(123, land["key 2"])
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

    private fun createTestPreProcessLogCallback(): PreProcessLogCallback = {
        it.copy(
            threadId = expectedThreadId,
            timestamp = expectedTimestamp
        )
    }

    private fun createExpectedLogEntry(level: Forest.Level, tag: String? = expectedTag) = LogEntry(
        level = level,
        threadId = expectedThreadId,
        timestamp = expectedTimestamp,
        message = expectedMessage,
        tag = tag
    )

    private fun Int.count() = downTo(0)
}

private interface Dummy