package com.sehmi.engine.hilt

import androidx.test.core.app.ApplicationProvider
import dagger.hilt.EntryPoints

/**
 * Generic helper to retrieve Hilt-managed singletons or state from the application 
 * context within test robots.
 *
 * This utility allows robots to access dependencies (like repositories or database 
 * DAOs) without requiring direct `@Inject` annotations, which can simplify robot 
 * construction and maintenance.
 *
 * @param T The reified type of the Hilt entry point interface to retrieve.
 * @return The implementation of the entry point [T].
 */
inline fun <reified T> getTestEntryPoint(): T {
    return EntryPoints.get(
        ApplicationProvider.getApplicationContext(),
        T::class.java
    )
}
