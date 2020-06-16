---
---
- [Implement A Tree](#implement-a-tree)
- [Plant A Tree](#plant-a-tree)

A `Tree` is a handler for all the logs that you send through Forest logging
methods. This library comes with {{ anchor('DebugTree', 'DebugTree') }}, which logs the 
log entry to the Android logcat. You can also implement your own `Tree` and plant it to a 
Forest so you can process the logs.

## Implement A Tree
In order to handle or process a log event, you must implement your own 
{{ anchor('Tree', 'big.forest.Tree') }}. Here is an example of implementing a tree that
send all the logs to [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android):
```kotlin
// FirebaseCrashlyticsTree.kt

class FirebaseCrashlyticsTree : Tree {
    override fun log(entry: LogEntry) {
        val tag = if (!entry.tag.isNullOrEmpty()) {
            "$tag - "
        } else {
            ""
        }

        FirebaseCrashlytics.getInstance().log("${tag}${entry.message}")
    }
}
```

## Plant A Tree
You can plant your tree to [Global Forest](../features/forest#global-forest) so the 
tree is available to all the [Forests](../features/forest#forest):
```kotlin
Forest.plant(FirebaseCrashlyticsTree())
```
Or if you prefer to plant to a particular [Forest](../features/forest#forest):
```kotlin
val forest = getForest(MyClass::class.java)
forest.plant(FirebaseCrashlyticsTree())
```

Note, a `Tree` will only receive a `Tree.log` call if 
{{ anchor('Forest.preProcessLog', 'Pre-process Log') }} method return `non-null` value, and 
{{ anchor('Forest.level', 'Log Levels') }} is higher than the logging level.
