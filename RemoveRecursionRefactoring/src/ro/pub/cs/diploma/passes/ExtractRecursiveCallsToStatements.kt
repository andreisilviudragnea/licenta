package ro.pub.cs.diploma.passes

import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil
import ro.pub.cs.diploma.*
import java.util.*

class ExtractRecursiveCallsToStatements(private val myMethod: PsiMethod) : Pass<PsiMethod, List<PsiMethodCallExpression>, Nothing?> {

  override fun collect(method: PsiMethod): List<PsiMethodCallExpression> {
    val calls = ArrayList<PsiMethodCallExpression>()
    val returnType = method.returnType ?: return calls
    if (returnType == PsiPrimitiveType.VOID) {
      return calls
    }
    method.accept(object : JavaRecursiveElementVisitor() {
      override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
        super.visitMethodCallExpression(expression)
        if (expression.isRecursiveCallTo(method)) {
          calls.add(expression)
        }
      }

      override fun visitClass(aClass: PsiClass) {}

      override fun visitLambdaExpression(expression: PsiLambdaExpression) {}
    })
    return calls
  }

  override fun transform(expressions: List<PsiMethodCallExpression>): Nothing? {
    val styleManager = myMethod.getStyleManager()
    val factory = myMethod.getFactory()
    val returnType = myMethod.returnType ?: return null
    calls@ for (call in expressions) {
      val parentStatement = PsiTreeUtil.getParentOfType(call, PsiStatement::class.java, true)
      if (parentStatement is PsiDeclarationStatement) {
        for (element in parentStatement.declaredElements) {
          if (element is PsiLocalVariable) {
            if (element.initializer === call) {
              continue@calls
            }
          }
        }
      }
      if (parentStatement is PsiExpressionStatement) {
        val expression = parentStatement.expression
        if (expression is PsiAssignmentExpression && expression.rExpression === call) {
          continue
        }
      }
      val parentBlock = PsiTreeUtil.getParentOfType(call, PsiCodeBlock::class.java, true) ?: continue
      val temp = styleManager.suggestUniqueVariableName(TEMP, myMethod, true)
      parentBlock.addBefore(factory.createVariableDeclarationStatement(temp, returnType, call), parentStatement)
      call.replace(factory.expression(temp))
    }
    return null
  }
}
