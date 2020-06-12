![](https://github.com/sukhai/forest/workflows/CI/badge.svg) [![](https://jitpack.io/v/sukhai/forest.svg)](https://jitpack.io/#sukhai/forest)

# Overview
Forest is a Android-Kotlin first logging library. It is inspired by the 
[Timber](https://github.com/JakeWharton/timber) library. 
Although Timber is good and easy to use, but it's missing some features that I wish it had when 
I was using it for some of my side projects.

Instead of extending Timber project, I decided to write it from scratch with some features from 
Timber plus some of the features I wish it had. These are some of the features of this library:
- Logging with a global logger. See [Global Forest](#global-forest).
- Logging with a named logger. See [Local Forest](#local-forest).
- Multiple log handlers. See [Tree](#tree).
- Sharing data globally with every loggers. See [Context](#context).
- Pre-processing a log. See [Pre-processing Callback](#pre-processing-callback).

## Table of Contents
- [Overview](#overview)
  - [Table of Contents](#table-of-contents)
- [Getting Started](#getting-started)
  - [Setting Up the Dependency](#setting-up-the-dependency)
    - [Step 1. Add the JitPack repository to your build file](#step-1-add-the-jitpack-repository-to-your-build-file)
    - [Step 2. Add the dependency](#step-2-add-the-dependency)
  - [Usage](#usage)
- [More Usage Details](#more-usage-details)
  - [Tree](#tree)
  - [Forest](#forest)
    - [Global Forest](#global-forest)
    - [Local Forest](#local-forest)
  - [Context](#context)
    - [Context Listener](#context-listener)
  - [Filter](#filter)
    - [Log Level](#log-level)
    - [Pre-processing Callback](#pre-processing-callback)
- [License](#license)

# Getting Started
## Setting Up the Dependency
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.sukhai:forest:x.y.z'
}
```
(Please replace the `x.y.z` with the latest version numbers: [![](https://jitpack.io/v/sukhai/forest.svg)](https://jitpack.io/#sukhai/forest))

## Usage
Similar to Timber, you will have to plant [Tree](#tree) to the Forest first. By default,
Forest does not have any tree planted. However, this library comes with `DebugTree`, 
which will print the logs to the logcat.
You can plant a tree like
```kotlin
Forest.plant(DebugTree())    // Java: Forest.Global.plant(new DebugTree());
```
then start logging
```kotlin
// Using global Forest
Forest.v("a message")    // Java: Forest.Global.v("a message");
Forest.d("a message")    // Java: Forest.Global.d("a message");
Forest.i("a message")    // Java: Forest.Global.i("a message");
Forest.w("a message")    // Java: Forest.Global.w("a message");
Forest.e("a message")    // Java: Forest.Global.e("a message");
Forest.f("a message")    // Java: Forest.Global.f("a message");

// Or

// Using local Forest
val forest = getForest(MyClass::class.java)    // Java: Forest forest = Forest.Global.getForest(MyClass.class);
forest.v("a message")    // forest.v("a message");
forest.d("a message")    // forest.d("a message");
forest.i("a message")    // forest.i("a message");
forest.w("a message")    // forest.w("a message");
forest.e("a message")    // forest.e("a message");
forest.f("a message")    // forest.f("a message");
```

# More Usage Details
**Note** 

All of the examples in this section use Kotlin. The main difference between Kotlin
and Java usage in this library is the global Forest, where in Kotlin, you can
use it like `Forest.d`, while Java you will have to access the `Global` object, 
such as `Forest.Global.d`.

## Tree
A `Tree` is a handler for all the logs that you send through Forest logging
methods. This library comes with `DebugTree`, which logs the log entry to
the logcat. You can implement your own `Tree` and plant it to a Forest.

An example of the implementation:
```kotlin
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

You can then plant your tree to either [Global Forest](#global-forest) or [Local Forest](#local-forest).
```kotlin
// Global Forest
val debugTree = DebugTree()

Forest.plant(debugTree)

// Local Forest
val forest = getForest(MyClass::class.java) {
    // Add the tree to this local forest
    plant(FirebaseCrashlyticsTree())

    // Remove DebugTree from this local forest
    cut(debugTree)
}
```

Note, a `Tree` will only receive a `Tree.log` call if `Forest.preProcessLog` method
return non-null value, and `Forest.level` is higher than the logging level.
See [Filter](#filter) section below for more details.

## Forest
Forest contains a collection of [Tree](#tree)s, and where you create a log entry to the trees. 
There are 2 types of Forest, [global Forest](#global-forest) and [local Forest](#local-forest). 
Both types of Forests share the same available methods and functionalities except the following:

- [Context](#context) can only be modified in [global Forest](#global-forest), but the data in the context will be available for all the trees planted in all the Forests.
- Planting or cutting [Tree](#tree)s on global Forest will automatically plant or cut the same tree on all local Forest, unless `Forest.allowGlobalOverride` is set to `false`.

### Global Forest
A globally available Forest, where you can plant a [Tree](#tree) then use its static 
methods to log a message, such as:
```kotlin
Forest.plant(DebugTree())

Forest.v("a message")
Forest.d("a message")
Forest.i("a message")
Forest.w("a message")
Forest.e("a message")
Forest.f("a message")
```
A few things to note:
- Unlike [local Forest](#local-forest), global Forest does not have a name, meaning `LogEntry.tag` does not have a default value, and will try to read the first stacktrace element class name as the tag value if `LogEntry.throwable` is non `null`, otherwise `LogEntry.tag` will be `null`.
- `Forest.plant(Tree)`, `Forest.cut(Tree)`, `Forest.level` will perform the same operation to all the local Forest, even after a local Forest has been created or initialized, unless `Forest.allowGlobalOverride` or local Forest `allowGlobalOverride` is set to `false`.
- By default, `Forest.allowGlobalOverride` is set to `true`.
- Most of the examples here use the methods from the global Forest, such as `Forest.d`, but these methods are available in a local Forest as well.

### Local Forest
A local Forest is a Forest that you created through the Forest static methods, and you can set a name to a local Forest when you create it.
A local Forest is created and handled by the [global Forest](#global-forest).

When you use the static method to create a local Forest with a name, the global Forest will check if a Forest with the same name has already been created before or not, if it has, then this same local Forest will be returned, otherwise a new local Forest with the given name will be created and returned.

You can create a local Forest in the following ways
```kotlin
val forestWithCustomName = getForest("a forest name")

val forestWithClassName = getForest(MyClass::class.java)

val forestWithCustomConfig = getForest(MyClass::class.java) {
    // This will override the level set through Forest.level
    level = Forest.Level.Error

    // This will override the callback set through Forest.preProcessLog
    preProcessLog = { logEntry -> logEntry.copy(message = "${logEntry.tag} - ${logEntry.message}") }

    // Default to true, setting false will disable global Forest from
    // making changes to this local forest after it's created
    allowGlobalOverride = false

    // Plant this tree only to this forest, this tree will not be available to
    // all other local Forests and global Forest
    plant(DebugTree())
}

// You can also plant a tree through global Forest and all local Forests
// will get the same tree added, as long as allowGlobalOverride is set to true
Forest.plant(DebugTree())

// DebugTree is available to this forest
val forest = getForest(MyClass::class.java)
```
By default, the following properties from global Forest will be passed down to local Forest when the local Forest is newly created or `allowGlobalOverride` is set to `true` (default value):
- `Forest.level`
- `Forest.preProcessLog`
- `Forest.allowGlobalOverride`

But you can override these values on the local Forest by using the same methods on the local Forest instance. For example:
```kotlin
// You can override the value through initialization
val forest = getForest(MyClass::class.java) { level = Forest.Level.ERROR }

// Or you can override it after you have retrieved the instance
forest.level = Forest.Level.WARN
forest.preProcessLog { it }
forest.plant(DebugTree())
```
You can use the following methods to log a message to the local Forest:
```kotlin
val forest = // ...

forest.v("a message")
forest.d("a message")
forest.i("a message")
forest.w("a message")
forest.e("a message")
forest.f("a message")
```

## Context
There's a concept of context in the Forest, which allows you to set data
that are available to all Forests and trees, so you do not need to pass
these data for every logging calls. You can only update the context in the global Forest, but the data 
will be available to all trees in all Forests.

For example, you can use the context to set user's first and last name,
which will then be available to all the trees when a log entry is generated.
```kotlin
// Set value
Forest.context["first_name"] = "John"
Forest.context["last_name"] = "Doe"

// Remove value
Forest.context.remove("first_name")

// You can get the context value in a LogEntry
val myTree = object : Tree {
    override fun log(entry: LogEntry) {
        val lastName = entry.context["last_name"]
    }
}
```

### Context Listener
You can set a listener to receive callbacks whenever the context data has
been modified. This is useful if you want to also update other 3rd party 
logging services. For example
```kotlin
Forest.context.setOnModifiedListener { state ->
    when (state) {
        is ModifiedState.New -> {
            if (state.key == "user_id") {
                FirebaseCrashlytics.getInstance().setUserId(state.value)
            } else {
                FirebaseCrashlytics.getInstance().setCustomKey(state.key, state.value)
            }
        }
        is ModifiedState.Updated -> {
            FirebaseCrashlytics.getInstance().setCustomKey(state.key, state.newValue)
        }
        is ModifiedState.Removed -> {
            FirebaseCrashlytics.getInstance().setCustomKey(state.key, null)
        }
    }
}
```

## Filter
There are two ways of filtering logs, and can be done through [Forest.level](#log-level) and/or 
[Forest.preProcessLog](#pre-processing-callback).

### Log Level
There are a set of log levels available and you can choose to enable only a subset of log levels to 
be sent to all your trees. You can use this to filter out some logs that you don't want to be sent 
to the trees.

The levels of the logs are, from the lowest to the highest level:
```kotlin
Forest.Level.OFF
Forest.Level.FATAL
Forest.Level.ERROR
Forest.Level.WARN
Forest.Level.INFO
Forest.Level.DEBUG
Forest.Level.VERBOSE
```
You can set a logging level on a Forest and the Forest will use this to determine if it should forward 
a log to the trees. For example
```kotlin
// Set level globally to all Forests
Forest.level = Forest.Level.WARN

// Set level to a particular Forest
val forest = getForest(MyClass::class.java) { level = Forest.Level.ERROR }
```
After setting a log level, a log will only be forwarded to a tree in a Forest if the given log level is 
lower than the one you set for the Forest, and the level is not `Forest.Level.OFF`. For example:
```kotlin
val myForest = getForest(MyClass::class.java) { 
    level = Forest.Level.INFO
    allowGlobalOverride = false
}
myForest.d("I will not get sent to all the trees in myForest.")
myForest.w("I will get sent to all the trees in myForest.")
myForest.e("I will get sent to all the trees in myForest.")

Forest.level = Forest.Level.WARN
Forest.i("I will not get sent to the trees in global Forest.")
Forest.w("I will get sent to the trees in global Forest.")
Forest.e("I will get sent to the trees in global Forest.")

Forest.level = Forest.Level.OFF
Forest.v("None of the logs will get sent to the trees in the global Forest.")
Forest.f("None of the logs will get sent to the trees in the global Forest.")
```

### Pre-processing Callback
You can apply a pre-processing callback to all the logs that are about to be forwarded to 
all the trees. This is useful if you want to modify some values in the log entry 
(i.e masking sensitive data) or skipping some type of log entries, so you don't have
to apply these logic in every single tree you implemented.

Note, this callback is triggered after the `LogEntry` has gone through [log level](#log-level) filter.

```kotlin
Forest.preProcessLog { logEntry ->
    when {
        logEntry.message.contains("secret") -> {
            // Forward a LogEntry that has the word "secret" replaced with "******"
            // to all the trees
            logEntry.copy(message = logEntry.message.replace("secret", "******"))
        }
        logEntry.attributes.containsKey("dont_log") -> {
            // Return null will not forward this logEntry to any tree
            null
        }
        else -> {
            // Forward the given logEntry to all the trees
            logEntry
        }
    }
}
```

# License
```
Copyright 2020 Su Khai Koh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```