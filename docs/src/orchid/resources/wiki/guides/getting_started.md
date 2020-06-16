---
---
- [Basic Usage](#basic-usage)
  - [Global Forest](#global-forest)
- [Core Concepts](#core-concepts)
- [Next Step](#next-step)

**Before you get started, make sure you have install this library by following this
{{ anchor(title='installation guide', itemId='Installation', collectionType='wiki') }}.**

## Basic Usage
You will have to plant at least a {{ anchor(title='Tree', itemId='Tree', collectionType='wiki') }} 
to the `Forest` first so you can receive a log event from the `Forest`. By default, this library 
comes with {{ anchor(title='DebugTree', itemId='DebugTree') }}, which print the logs to the logcat, 
and we will be using this as an example on this page.

#### Step 1: Plant a Tree
```kotlin
// onCreate() of your application class or application entry point
Forest.plant(DebugTree())
```

#### Step 2: Create a Forest in any of your class
```kotlin
val forest = getForest(MyClass::class.java)
```

#### Step 3: Start logging!
```kotlin
forest.v("a message")
forest.d("a message")
forest.i("a message")
forest.w("a message")
forest.e("a message")
forest.f("a message")
```

### Global Forest
You can optionally use the [Global Forest](../features/forest#global-forest)
to log event. The benefit of using the {{ anchor(title='Global Forest', itemId='Global Forest', collectionType='wiki') }}
is it's easy to use and you do not have to create a Forest on any of your class. 
You can just use the static methods to log event:
```kotlin
// onCreate() of your application class or application entry point
Forest.plant(DebugTree())

// Start logging!
Forest.v("a message")
Forest.d("a message")
Forest.i("a message")
Forest.w("a message")
Forest.e("a message")
Forest.f("a message")
```

## Core Concepts
Let's first understand 2 important classes in this library and their purposes:
- {{ anchor(title='Forest', itemId='Forest', collectionType='wiki') }} - Serves as a logger, 
in which you will use it to log an event.
- {{ anchor(title='Tree', itemId='Tree', collectionType='wiki') }} - Serves as a log handler, 
in which you will use it to handle the log event sent by `Forest`.

There are 2 types of `Forest`, [Forest](../features/forest#forest) and 
[Global Forest](../features/forest#global-forest). For simplicity, unless specify, the code
examples use on this wiki will be using [Forest](../features/forest#forest).

## Next Step
{{ anchor('Forest', 'Home') }} has many features to help you log an event much easily. 
Checkout the following features by visiting the link below:
- Create your own log handler. See {{ anchor(title='Tree', itemId='Tree', collectionType='wiki') }}.
- In-depth understanding of the Forest object. See {{ anchor(title='Forest', itemId='Forest', collectionType='wiki') }}.
- Different types of log levels. See {{ anchor('Log Levels', 'Log Levels') }}.
- Adding attributes to your log event. See {{ anchor('Adding Attributes', 'Adding Attributes') }}.
- Sharing data across all loggers. See {{ anchor('Forest Context', 'Forest Context') }}.
- Pre-process a log event to modify or filter the log event. See {{ anchor('Pre-process Log', 'Pre-process Log') }}.
