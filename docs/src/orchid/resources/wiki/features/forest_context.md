---
---
- [Set Data](#set-data)
- [Get Data](#get-data)
- [Context Listener](#context-listener)

{{ anchor('Forest Context', 'ForestContext') }} allows you to set data that are available to all 
Forests and trees, so you do not need to pass these data for every logging calls. You can only 
update the context in the global Forest, but the data will be available to all trees in all Forests.

For example, you can use {{ anchor('Forest Context', 'ForestContext') }} to set user's
first and last name when application loaded, so the data is available to all the trees during
the whole session.

## Set Data
```kotlin
// Set values
Forest.context["first_name"] = "John"
Forest.context["last_name"] = "Doe"

// Update value
Forest.context["first_name"] = "Joe"

// Remove value
Forest.context.remove("first_name")
```

## Get Data
You can get the data in the context through {{ anchor('LogEntry.context', 'LogEntry') }}:
```kotlin
class MyTree : Tree {
    override fun log(entry: LogEntry) {
        val lastName = entry.context["last_name"]    // Doe
    }
}
```

## Context Listener
You can set a listener to receive callbacks whenever the context data has
been modified. This is useful if you want to also update other objects when
the {{ anchor('Forest Context', 'ForestContext') }} has been modified. 
For example:
```kotlin
Forest.context.setOnModifiedListener { state ->
    when (state) {
        is ModifiedState.New -> {
            if (state.key == "user_id") {
                // Add the user ID value to Firebase Crashlytics
                FirebaseCrashlytics.getInstance().setUserId(state.value)
            } else {
                // Add the new custom key to Firebase Crashlytics
                FirebaseCrashlytics.getInstance().setCustomKey(state.key, state.value)
            }
        }
        is ModifiedState.Updated -> {
            // Update the custom key from Firebase Crashlytics
            FirebaseCrashlytics.getInstance().setCustomKey(state.key, state.newValue)
        }
        is ModifiedState.Removed -> {
            // Remove the custom key from Firebase Crashlytics
            FirebaseCrashlytics.getInstance().setCustomKey(state.key, null)
        }
    }
}
```
