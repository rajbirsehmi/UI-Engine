package com.sehmi.engine.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestLintTask
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue

class DirectComposeTestUsageDetectorTest : LintDetectorTest() {

    override fun getDetector(): Detector = DirectComposeTestUsageDetector()

    override fun getIssues(): List<Issue> = listOf(DirectComposeTestUsageDetector.ISSUE)

    override fun lint(): TestLintTask {
        return super.lint().allowMissingSdk(true)
    }

    fun testDirectPerformClick() {
        lint().files(
            kotlin("""
                package com.example.test
                import androidx.compose.ui.test.onNodeWithTag
                import androidx.compose.ui.test.performClick

                class MyTest {
                    fun test() {
                        onNodeWithTag("tag").performClick()
                    }
                }
            """).indented()
        )
        .run()
        .expect("""
            src/com/example/test/MyTest.kt:7: Error: UI automation actions must use the high-level com.sehmi.engine DSL extensions instead of direct Compose/Espresso testing APIs. [DirectUiTestApiUsage]
                    onNodeWithTag("tag").performClick()
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            1 errors, 0 warnings
        """)
        .expectFixDiffs("""
            Fix for src/com/example/test/MyTest.kt line 7: Replace with clickOnTag(...):
            @@ -7 +7 @@
            -         onNodeWithTag("tag").performClick()
            +         clickOnTag("tag")
        """)
    }

    fun testAllowedInEngine() {
        lint().files(
            kotlin("""
                package com.sehmi.engine.advanced
                import androidx.compose.ui.test.onNodeWithTag
                import androidx.compose.ui.test.performClick

                class InternalEngineCode {
                    fun execute() {
                        onNodeWithTag("tag").performClick()
                    }
                }
            """).indented()
        )
        .run()
        .expectClean()
    }

    fun testDirectAssertion() {
        lint().files(
            kotlin("""
                package com.example.test
                import androidx.compose.ui.test.onNodeWithTag
                import androidx.compose.ui.test.assertIsDisplayed

                class MyTest {
                    fun test() {
                        onNodeWithTag("tag").assertIsDisplayed()
                    }
                }
            """).indented()
        )
        .run()
        .expect("""
            src/com/example/test/MyTest.kt:7: Error: UI automation actions must use the high-level com.sehmi.engine DSL extensions instead of direct Compose/Espresso testing APIs. [DirectUiTestApiUsage]
                    onNodeWithTag("tag").assertIsDisplayed()
                    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            1 errors, 0 warnings
        """)
        .expectFixDiffs("""
            Fix for src/com/example/test/MyTest.kt line 7: Replace with assertTagDisplayed(...):
            @@ -7 +7 @@
            -         onNodeWithTag("tag").assertIsDisplayed()
            +         assertTagDisplayed("tag")
        """)
    }
}
