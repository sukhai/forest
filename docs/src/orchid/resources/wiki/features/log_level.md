---
---
- [Update Log Leve](#update-log-level)
- [Log Filtering](#log-filtering)

The following log levels and their numerical values are supported:

| Level    | Numerical Value |
| -------- | --------------- |
| OFF      | 0               |
| FATAL    | 1               |
| ERROR    | 2               |
| WARN     | 3               |
| INFO     | 4               |
| DEBUG    | 5               |
| VERBOSE  | 6               |

## Update Log Level
You can change the log level on a particular [Forest](../features/forest#forest) by:
```kotlin
val forest = getForest(MyClass::class.java) { level = Forest.Level.INFO }

// Or
forest.level = Forest.Level.INFO
```

Or change the log level globally on the [Global Forest](../features/forest#global-forest):
```kotlin
Forest.level = Forest.Level.INFO
```

## Log Filtering
Logs will only be forwarded to a tree if the given log level is lower than the one
you set for the Forest, and the level is not `Forest.Level.OFF`. For example:
```kotlin
val forest = getForest(MyClass::class.java)
forest.level = Forest.Level.INFO

myForest.d("I will NOT get logged.")
myForest.i("I will get logged.")
myForest.w("I will get logged.")

// The behavior is same for the Global Forest
Forest.level = Forest.Level.WARN
Forest.i("I will NOT get logged.")
Forest.w("I will get logged.")
Forest.e("I will get logged.")

Forest.level = Forest.Level.OFF
Forest.v("I will NOT get logged.")
Forest.f("I will NOT get logged.")
```