---
---
- [Gradle](#gradle)
- [Maven](#maven)

## Gradle
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
    implementation 'com.github.sukhai:forest:{{ site.version }}'
}
```

You can also selectively install the module you want from this library:
```groovy
dependencies {
    implementation 'com.github.sukhai.forest:forest:{{ site.version }}'
    
    // Note: You will not have DebugTree if you do not import this module
    implementation 'com.github.sukhai.forest:forest-android:{{ site.version }}'
}
```

## Maven
**Step 1: Add the JitPack repository to your build file**</br>
```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

**Step 2: Add the dependency**</br>
The following installation will add all the modules from this library:
```xml
<dependency>
    <groupId>com.github.sukhai</groupId>
    <artifactId>forest</artifactId>
    <version>{{ site.version }}</version>
</dependency>
```

You can also selectively install the module you want from this library:
```xml
<dependency>
    <groupId>com.github.sukhai.forest</groupId>
    <artifactId>forest</artifactId>
    <version>{{ site.version }}</version>
</dependency>

<!-- Note: You will not have DebugTree if you do not import this module -->
<dependency>
    <groupId>com.github.sukhai.forest</groupId>
    <artifactId>forest-android</artifactId>
    <version>{{ site.version }}</version>
</dependency>
```

## Next Step
- Learn about how to use Forest by visiting the {{ anchor('Getting Started', 'Getting Started') }}
page.
- Using Forest for Java application? Visit {{ anchor('Java Usage', 'Java') }} to learn more
about how you can use this library for your Java application.