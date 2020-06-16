---
---
You can apply a callback to process all the logs that are about to be forwarded to 
all the trees. This is useful if you want to modify some values in the log entry 
(i.e masking sensitive data) or filter out some type of log entries, so you don't have
to apply these logic in every single tree you implemented.

**Note, this callback is triggered after the `LogEntry` has gone through `Forest.level` filter.
See {{ anchor('Log Levels', 'Log Levels') }} for more details.**

Apply to a single Forest:
```kotlin
val forest = getForest(MyClass::class.java)
forest.preProcessLog { logEntry ->
    when {
        logEntry.message.contains("secret") -> {
            // Forward a LogEntry that has the word "secret" and replace it with "******"
            // before forwarding to all the trees
            logEntry.copy(message = logEntry.message.replace("secret", "******"))
        }
        logEntry.attributes.containsKey("dont_log") -> {
            // Return null will not forward this logEntry to any tree
            null
        }
        else -> {
            // Forward the given logEntry to all the trees as is
            logEntry
        }
    }
}
```

Apply to all Forests:
```kotlin
Forest.preProcessLog { logEntry ->
    when {
        logEntry.message.contains("secret") -> {
            // Forward a LogEntry that has the word "secret" and replace with "******"
            // before forwarding to all the trees
            logEntry.copy(message = logEntry.message.replace("secret", "******"))
        }
        logEntry.attributes.containsKey("dont_log") -> {
            // Return null will not forward this logEntry to any tree
            null
        }
        else -> {
            // Forward the given logEntry to all the trees as is
            logEntry
        }
    }
}
```