package big.forest.trees

import android.util.Log
import big.forest.Forest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DebugTreeTest {
    private lateinit var tree: DebugTree
    private val expectedMessage = "a message"
    private val expectedThrowable = Throwable("a throwable")
    private val expectedAttributes = mapOf(
        "key1" to "value1",
        "key2" to 123
    )
    private val expectedMessageWithAttributes = "$expectedMessage\nAttributes:\n$expectedAttributes"
    private val expectedMessageWithEverything =
        "$expectedMessage\nContext:\n{key3=value3}\nAttributes:\n$expectedAttributes"
    private val levelVerbose = 2
    private val levelDebug = 3
    private val levelInfo = 4
    private val levelWarn = 5
    private val levelError = 6
    private val levelFatal = 7

    @BeforeEach
    fun setup() {
        Forest.context.clear()
        Forest.deforest()

        tree = DebugTree()
        Forest.plant(tree)
    }

    @AfterEach
    fun tearDown() {
        Forest.deforest()
        Forest.context.clear()
    }

    @Test
    fun `calling Forest_d will trigger debug logging`() {
        Log.Test.assert {
            Forest.d(expectedMessage)
            assertEquals(expectedMessage, actualMessage)

            Forest.d(expectedMessage, expectedThrowable)
            assertEquals(expectedMessage, actualMessage)
            assertEquals(expectedThrowable, actualThrowable)

            Forest.d(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithAttributes, actualMessage)

            Forest.context["key3"] = "value3"
            Forest.d(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithEverything, actualMessage)
            assertEquals(levelDebug, actualLevel)
        }
    }

    @Test
    fun `calling Forest_i will trigger info logging`() {
        Log.Test.assert {
            Forest.i(expectedMessage)
            assertEquals(expectedMessage, actualMessage)

            Forest.i(expectedMessage, expectedThrowable)
            assertEquals(expectedMessage, actualMessage)
            assertEquals(expectedThrowable, actualThrowable)

            Forest.i(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithAttributes, actualMessage)

            Forest.context["key3"] = "value3"
            Forest.i(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithEverything, actualMessage)
            assertEquals(levelInfo, actualLevel)
        }
    }

    @Test
    fun `calling Forest_w will trigger warn logging`() {
        Log.Test.assert {
            Forest.w(expectedMessage)
            assertEquals(expectedMessage, actualMessage)

            Forest.w(expectedMessage, expectedThrowable)
            assertEquals(expectedMessage, actualMessage)
            assertEquals(expectedThrowable, actualThrowable)

            Forest.w(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithAttributes, actualMessage)

            Forest.context["key3"] = "value3"
            Forest.w(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithEverything, actualMessage)
            assertEquals(levelWarn, actualLevel)
        }
    }

    @Test
    fun `calling Forest_e will trigger error logging`() {
        Log.Test.assert {
            Forest.e(expectedMessage)
            assertEquals(expectedMessage, actualMessage)

            Forest.e(expectedMessage, expectedThrowable)
            assertEquals(expectedMessage, actualMessage)
            assertEquals(expectedThrowable, actualThrowable)

            Forest.e(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithAttributes, actualMessage)

            Forest.context["key3"] = "value3"
            Forest.e(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithEverything, actualMessage)
            assertEquals(levelError, actualLevel)
        }
    }

    @Test
    fun `calling Forest_f will trigger wtf logging`() {
        Log.Test.assert {
            Forest.f(expectedMessage)
            assertEquals(expectedMessage, actualMessage)

            Forest.f(expectedMessage, expectedThrowable)
            assertEquals(expectedMessage, actualMessage)
            assertEquals(expectedThrowable, actualThrowable)

            Forest.f(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithAttributes, actualMessage)

            Forest.context["key3"] = "value3"
            Forest.f(expectedMessage, expectedAttributes)
            assertEquals(expectedMessageWithEverything, actualMessage)
            assertEquals(levelFatal, actualLevel)
        }
    }
}