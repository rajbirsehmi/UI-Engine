package com.sehmi.engine.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.getContainingUFile

class DirectComposeTestUsageDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? = null

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val file = node.getContainingUFile() ?: return
        val packageName = file.packageName
        
        // Allowed in com.sehmi.engine.* package (the engine itself)
        if (packageName.startsWith("com.sehmi.engine")) {
            return
        }

        // Detect forbidden usage in src/androidTest/
        if (!context.isTestSource) return

        val methodName = method.name
        val containingClass = method.containingClass?.qualifiedName ?: ""

        val isComposeTestApi = containingClass.startsWith("androidx.compose.ui.test") && 
            (methodName.startsWith("onNode") || 
             methodName.startsWith("perform") || 
             methodName.startsWith("assert"))

        val isEspressoApi = containingClass.startsWith("androidx.test.espresso")

        if (isComposeTestApi || isEspressoApi) {
            val fix = createFix(methodName)
            
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "UI automation actions must use the high-level com.sehmi.engine DSL extensions instead of direct Compose/Espresso testing APIs.",
                fix,
            )
        }
    }

    private fun createFix(methodName: String): LintFix? {
        return when (methodName) {
            "performClick" -> {
                // Try to find the tag in the receiver chain (e.g. onNodeWithTag("tag").performClick())
                // This is a simplified heuristic for the QuickFix.
                LintFix.create()
                    .name("Replace with clickOnTag(...)")
                    .replace()
                    .pattern("onNodeWithTag\\((.*)\\)\\.performClick\\(\\)")
                    .with("clickOnTag(\\1)")
                    .build()
            }
            "performTextInput" -> {
                LintFix.create()
                    .name("Replace with enterText(...)")
                    .replace()
                    .pattern("onNodeWithTag\\((.*)\\)\\.performTextInput\\((.*)\\)")
                    .with("enterText(\\1, \\2)")
                    .build()
            }
            "assertIsDisplayed" -> {
                LintFix.create()
                    .name("Replace with assertTagDisplayed(...)")
                    .replace()
                    .pattern("onNodeWithTag\\((.*)\\)\\.assertIsDisplayed\\(\\)")
                    .with("assertTagDisplayed(\\1)")
                    .build()
            }
            else -> null
        }
    }

    companion object {
        @JvmField
        val ISSUE = Issue.create(
            id = "DirectUiTestApiUsage",
            briefDescription = "Forbidden direct usage of Compose or Espresso testing APIs",
            explanation = "UI automation actions must use the high-level com.sehmi.engine DSL extensions instead of direct Compose/Espresso testing APIs.",
            category = Category.CORRECTNESS,
            priority = 8,
            severity = Severity.ERROR,
            implementation = Implementation(
                DirectComposeTestUsageDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
