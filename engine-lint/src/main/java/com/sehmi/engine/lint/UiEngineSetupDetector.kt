package com.sehmi.engine.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.*
import org.jetbrains.uast.visitor.AbstractUastVisitor

/**
 * Detects missing setup for [UiEngine] when [UiEngine.withRobot] is used.
 */
class UiEngineSetupDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> = listOf("withRobot")

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        val containingClass = method.containingClass?.qualifiedName ?: ""
        if (containingClass != "com.sehmi.engine.core.UiEngine") return

        val uClass = node.getParentOfType<UClass>() ?: return
        
        var hasSetComposeRule = false
        var hasUiEngineRule = false

        uClass.accept(object : AbstractUastVisitor() {
            override fun visitCallExpression(node: UCallExpression): Boolean {
                val resolvedMethod = node.resolve()
                if (resolvedMethod?.name == "setComposeRule" && 
                    resolvedMethod.containingClass?.qualifiedName == "com.sehmi.engine.core.UiEngine") {
                    hasSetComposeRule = true
                }
                return super.visitCallExpression(node)
            }

            override fun visitVariable(node: UVariable): Boolean {
                val type = node.type.canonicalText
                if (type == "com.sehmi.engine.core.UiEngineRule") {
                    hasUiEngineRule = true
                }
                return super.visitVariable(node)
            }
        })

        if (!hasSetComposeRule && !hasUiEngineRule) {
            context.report(
                ISSUE,
                node,
                context.getLocation(node),
                "Using `UiEngine.withRobot` requires setting the ComposeRule. " +
                "Add `@get:Rule val engineRule = UiEngineRule(composeRule)` to your test class."
            )
        }
    }

    companion object {
        @JvmField
        val ISSUE = Issue.create(
            id = "MissingUiEngineSetup",
            briefDescription = "UiEngine.withRobot used without setup",
            explanation = """
                When using `UiEngine.withRobot`, you must ensure that the `ComposeTestRule` 
                is registered with the engine. 
                
                You can do this by adding a `UiEngineRule` to your test class:
                `@get:Rule val engineRule = UiEngineRule(composeRule)`
                
                Or by manually calling `UiEngine.setComposeRule(composeRule)` in your `@Before` method.
            """.trimIndent(),
            category = Category.CORRECTNESS,
            priority = 9,
            severity = Severity.ERROR,
            implementation = Implementation(
                UiEngineSetupDetector::class.java,
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
