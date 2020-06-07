package android.util

class Log {
    companion object Test {
        var actualTag: String? = null
        var actualMessage: String? = null
        var actualThrowable: Throwable? = null
        var actualLevel: Int = 0

        fun assert(assertion: Test.() -> Unit) {
            assertion(this)
            actualTag = null
            actualMessage = null
            actualThrowable = null
            actualLevel = 0
        }

        @JvmStatic
        fun v(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 2
            return 0
        }

        @JvmStatic
        fun d(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 3
            return 0
        }

        @JvmStatic
        fun i(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 4
            return 0
        }

        @JvmStatic
        fun w(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 5
            return 0
        }

        @JvmStatic
        fun e(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 6
            return 0
        }

        @JvmStatic
        fun wtf(tag: String, msg: String?, tr: Throwable?): Int {
            actualTag = tag
            actualMessage = msg
            actualThrowable = tr
            actualLevel = 7
            return 0
        }
    }
}