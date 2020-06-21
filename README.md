![](https://github.com/sukhai/forest/workflows/CI/badge.svg) [![](https://jitpack.io/v/sukhai/forest.svg)](https://jitpack.io/#sukhai/forest)

# Forest
Forest is A lightweight Android-Kotlin first logging library. It is inspired by the 
[Timber](https://github.com/JakeWharton/timber) project. Although Timber is good and easy to use, 
but it's missing some features that can work better on an Android project.

For more information please visit the [website](https://sukhai.github.io/forest/).

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

## Installation
**Step 1: Add the JitPack repository to your build file**</br>
Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2: Add the dependency**</br>
The following installation will add all the modules from this library:
```groovy
dependencies {
    implementation 'com.github.sukhai:forest:x.y.z'
}
```
Please replace `x.y.z` with the latest version: [![](https://jitpack.io/v/sukhai/forest.svg)](https://jitpack.io/#sukhai/forest)
For more information please visit the [installation guide](https://sukhai.github.io/forest/wiki/guides/installation/)

## Features
Features offer by this library includes:
- Log messages with a named logger. See {{ anchor(title='Forest', itemId='Forest', collectionType='wiki') }}.
- Log message with a global logger. See [Global Forest](wiki/features/forest#global-forest).
- Multiple log handlers. See {{ anchor(title='Tree', itemId='Tree', collectionType='wiki') }}.
- Adding additional attributes to a log. See {{ anchor(title='adding attributes', itemId='Adding Attributes', collectionType='wiki') }}.
- Share data globally with all the log handlers. See {{ anchor(title='Forest Context', itemId='Forest Context', collectionType='wiki') }}.
- Pre-process a log. See {{ anchor(title='pre-processing callback', itemId='Pre-process Log', collectionType='wiki') }}.

## Full Documentation
- [Website](https://sukhai.github.io/forest/)

## License
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