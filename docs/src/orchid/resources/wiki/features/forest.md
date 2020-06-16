---
---
- [Forest](#forest)
  - [Creating a Forest](#creating-a-forest)
  - [Forest Properties](#forest-properties)
  - [Forest Config](#forest-config)
  - [Plant A Tree](#plant-a-tree)
  - [Cut A Tree](#cut-a-tree)
  - [Logging An Event](#logging-an-event)
- [Global Forest](#global-forest)
  - [allowGlobalOverride](#allowglobaloverride)

There are 2 types of {{ anchor('Forests', 'big.forest.Forest') }}, regular
[Forest](#forest) and [Global Forest](#global-forest). 

## Forest
A {{ anchor('Forest', 'big.forest.Forest') }} is a logger with a name and use for logging
a message. 

This type of Forest is recommended as it allows you to set a name to the Forest, and
the name of the Forest will be available as the {{ anchor('LogEntry.tag', 'LogEntry') }}. You
can then use this {{ anchor('LogEntry.tag', 'LogEntry') }} to find out where the logs get
sent from.

### Creating a Forest
Creating a Forest with a name is optional, but it's recommended. When you create a Forest, 
it will get added to the [Global Forest](#global-forest). Next time when you request to create
a Forest with the same name, [Global Forest](#global-forest) will return the same instance
of Forest instead.

```kotlin
// Name of the Forest would be ""
val forestWithoutName = getForest()

val forestWithName = getForest("forest_name")

val forestWithClassName = getForest(MyClass::class.java)
```

### Forest Properties
By default, all the properties you set in the [Global Forest](#global-forest) will be passed
down to a Forest whenever the [Global Forest](#global-forest) is updated, and these properties 
include:</br>
**Note: The override will happen even after Forest has been created.**
- The collection of {{ anchor(title='Trees', itemId='Tree', collectionType='wiki') }}.
- All the properties in {{ anchor('ForestConfig', 'big.forest.ForestConfig') }}.

You can override this behavior and prevent [Global Forest](#global-forest) from overriding
your particular Forest by configuring the `allowGlobalOverride` flag:
```kotlin
val forest = getForest(MyClass::class.java) { allowGlobalOverride = false }
```
Or if you prefer to override this behavior for all Forests, you can set it in the 
[Global Forest](#global-forest):
```kotlin
Forest.allowGlobalOverride = false
```

### Forest Config
You can configure a Forest when it's created, and some of the properties can also be 
configured after it's created. All the values are defaulted to the values set in the 
[Global Forest](#global-forest).
```kotlin
val forest = getForest(MyClass::class.java) {
    allowGlobalOverride = false    // Default value from Global Forest, otherwise default is true
    level = Forest.Level.ERROR     // Default value from Global Forest, otherwise default is Forest.Level.VERBOSE
    preProcessLog = { it }         // Default value from Global Forest, otherwise default is null
    
    // All the trees are defaulted to the trees we set from Global Forest,
    // otherwise default to no tree.

    // You can also plant extra trees
    plant(DebugTree())
    plant(OtherTree())
    
    // You can also cut down trees
    cut(tree1)
    cut(tree2)
}

// You can also change these properties after the Forest has been created
forest.level = Forest.Level.DEBUG
forest.preProcessLog { it.copy(message = "My Log - ${it.message}") }
forest.plant(NewTree())
forest.cut(oldTree)
```

### Plant A Tree
You can plant a tree to handle logs that sent by a Forest. Planting a tree
means adding a log handler to the Forest so Forest knows where to send 
a log to.</br>
**Note: The same tree can only be planted once. If you try to plant the same
tree more than once, it will not be added to the Forest**.
```kotlin
// MyTree.kt
class MyTree : Tree {
    override fun log(entry: LogEntry) {
        // ...
        println(entry)
    }
}

// onCreate() of your application class or application entry point
// Plant the tree through Global Forest so this tree is available to all Forests
Forest.plant(MyTree())

// Or, plan the tree through specific Forest so the tree is only available to this Forest
val forest = getForest(MyClass::class.java) { plant(MyTree()) }
```

### Cut A Tree
You can cut a tree from a Forest. Cutting a tree means removing the log handler
from the Forest so any new log will no longer be forwarded to this tree.
```kotlin
val myTree = // ...

// Cut the tree through Global Forest so this tree is removed from all Forests
Forest.cut(myTree)

// Or, just cut the tree from a specific Forest when the Forest is newly created
val forest = getForest(MyClass::class.java) { cut(myTree) }
```

You can also cut all the trees by calling `Forest.deforest()` method. 
- Calling this method through the [Global Forest](#global-forest) will remove all the trees from
all Forests that has `allowGlobalOverride` set to `true`.
- Calling this method through a regular Forest will only remove all the trees from the
calling Forest.

### Logging An Event
You can log an event by using any of the following methods, and the event will be
forwarded to the {{ anchor(title='Trees', itemId='Tree', collectionType='wiki') }} that
are available to the calling Forest:
```kotlin
val forest = getForest(MyClass::class.java)

val attributes = mapOf("key1" to "value1", "key2" to 2)

forest.v("a message")
forest.v("a message", attributes)
forest.v("a message", Throwable("a throwable"))
forest.v("a message", Throwable("a throwable"), attributes)
forest.v(Throwable("a throwable"))
forest.v(Throwable("a throwable"), attributes)

// The same list of methods also available for other Forest.Level, for example
forest.d(...)
forest.i(...)
forest.w(...)
forest.e(...)
forest.f(...)
forest.log(...)
```

## Global Forest
A Global Forest is just a [Forest](#forest) that is singleton and can be used to
log an event with its static methods:
```kotlin
Forest.d(...)
Forest.i(...)
Forest.w(...)
Forest.e(...)
Forest.f(...)
Forest.log(...)
```

All the methods that are available to a [Forest](#forest) are also available to
the Global Forest.

### allowGlobalOverride
By default, calling the methods in Global Forest like `Forest.level`, `Forest.plant()`, 
`Forest.cut()`, `Forest.preProcessLog()`, and many more, will forward the task to
all the [Forests](#forest) that were already created, meaning the properties in those
[Forests](#forest) will be overrided. You can avoid this by calling
`Forest.allowGlobalOverride = false`.

One use case is you want to set the logging level and plant the trees once to
all the [Forests](#forest), then prevent further changes to the [Forests](#forest).
You can achieve this behavior like the following:
```kotlin
// MyApplication.kt
class MyApplication : Application() {
    private val forest = getForest(MyApplication::class.java) { 
        // You want to allow override for application because this forest
        // gets initialized before onCreate() is called, so you want the
        // trees that we planted through Forest.plant() will also be planted
        // to this forest
        allowGlobalOverride = true
    }

    override fun onCreate() {
        Forest.plant(Tree1())
        Forest.plant(Tree2())
        Forest.level = Forest.Level.INFO
        Forest.allowGlobalOverride = false

        // ...
    }
}
```