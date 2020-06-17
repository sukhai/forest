---
---
All Forest logging methods accept `attributes` paramter to the log. This allows you to pass 
additional data along with the message you want to log, such as Android Bundle data, tags, and more.

Passing data through the `attributes` parameter in any logging method is a one time thing.
If you are looking for a way to set data for all your logs and always available to all your
logs, consider using {{ anchor(title='Forest Context', itemId='Forest Context', collectionType='wiki') }}
instead.

## Set Attributes
You can set the attributes to any logging methods available in a [Forest](../features/forest#forest)
 or [Global Forest](../features/forest#global-forest).
```kotlin
// MyClass.kt

val forest = getForest(MyClass::class.java)
val attributes = mapOf("name" to "John Doe", "age" to 24)

// Any of the logging methods below will work
forest.v("a message", attributes)
forest.d("a message", attributes)
forest.i("a message", attributes)
forest.w("a message", attributes)
forest.e("a message", attributes)
forest.f("a message", attributes)
```

## Get Attributes
All the attributes that you passed to a log will be available to the {{ anchor('LogEntry', 'LogEntry') }}.
```kotlin
// MyTree.kt

class MyTree : Tree {
    override fun log(entry: LogEntry) {
        val name = entry.attributes["name"]  // "John Doe"
        val age = entry.attributes["age"]    // 24
    
        // ...
    }
}
```