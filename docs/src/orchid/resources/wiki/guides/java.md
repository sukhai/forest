---
---
- [Java Installation](#java-installation)
- [Java Usage](#java-usage)

## Java Installation
You can follow the {{ anchor(title='installation guide', itemId='Installation') }}
to install this library. 

However, if you are **not** running on an Android application, you will need to 
selectively install the module and not install the `forest-android` module.
For example, in gradle, you should just install the `forest` module:
```groovy
dependencies {
    implementation 'com.github.sukhai.forest:forest:{{ site.version }}'
}
```

## Java Usage
All of the code snippets shown in this documentation use Kotlin. Unless specify, 
all of the classes and methods are supported in Java as well with a few workarounds:

**1. You will need to use {{ anchor('Forest.Global', 'big.forest.Forest.Global') }} to 
access the global Forest methods. For example,** 
```kotlin
Forest.plant(DebugTree())
Forest.d("a message")
```
Equivalent Java code:
```java
Forest.Global.plant(new DebugTree());
Forest.Global.d("a message");
```
**2. You can create a {{ anchor(title='Forest', itemId='Forest', collectionType='wiki') }} 
through the {{ anchor('Forest.Global', 'big.forest.Forest.Global') }}:**
```kotlin
val forest = getForest(MyClass::class.java)

// With configuration
val forest = getForest(MyClass::class.java) { config ->
    config.level = Forest.Level.INFO
    config.preProcessLog { ... }
}
```
Equivalent Java code:
```java
Forest forest = Forest.Global.getForest(MyClass.class);

// With configuration
Forest forest = Forest.Global.getForest(MyClass.class, config -> {
    config.setLevel(Forest.Level.INFO);
    config.setPreProcessLog(logEntry -> { ... });
});
```
**3. You can use the same methods to log a message:**
```kotlin
forest.w("a message")
forest.w("a message", Throwable("an exception"))
forest.w("a message", Throwable("an exception"), mapOf("key1" to "value 1", "key2" to 2))
```
Equivalent Java code:
```java
forest.w("a message");
forest.w("a message", new Throwable("an exception"));

Map<String, Object> attributes = new HashMap<>();
attributes.put("key1", "value 1");
attributes.put("key2", 2);
forest.w("a message", new Throwable("an exception"), attributes);
```
