package ro.pub.cs.diploma;

import com.intellij.psi.*;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
  private Util() {
  }

  @NotNull
  public static PsiStatement statement(@NotNull final PsiElementFactory factory, @NotNull final String text) {
    return factory.createStatementFromText(text, null);
  }

  @NotNull
  public static PsiElementFactory getFactory(@NotNull final PsiElement element) {
    return JavaPsiFacade.getElementFactory(element.getProject());
  }

  @NotNull
  public static JavaCodeStyleManager getStyleManager(@NotNull final PsiElement element) {
    return JavaCodeStyleManager.getInstance(element.getProject());
  }

  @Nullable
  public static PsiMethod getContainingMethod(@NotNull final PsiElement element) {
    return PsiTreeUtil.getParentOfType(element, PsiMethod.class, true, PsiClass.class, PsiLambdaExpression.class);
  }

  @NotNull
  @Contract(pure = true)
  static String getFrameClassName(@NotNull final String methodName) {
    return Utilities.capitalize(methodName) + Constants.FRAME;
  }

  public static boolean isVoid(@NotNull final PsiType returnType) {
    return returnType instanceof PsiPrimitiveType && PsiPrimitiveType.VOID.equals(returnType);
  }

  @NotNull
  public static <T extends PsiElement> PsiStatement createPushStatement(@NotNull final PsiElementFactory factory,
                                                                        @NotNull final String frameClassName,
                                                                        @NotNull final String stackVarName,
                                                                        @NotNull final T[] arguments,
                                                                        @NotNull final Function<T, String> function) {
    final String argumentsString = Arrays.stream(arguments).map(function).collect(Collectors.joining(","));
    return factory.createStatementFromText(stackVarName + ".push(new " + frameClassName + "(" + argumentsString + "));", null);
  }

  @NotNull
  public static List<PsiForeachStatement> getPsiForEachStatements(PsiMethod method) {
    final List<PsiForeachStatement> statements = new ArrayList<>();
    method.accept(new JavaRecursiveElementVisitor() {
      @Override
      public void visitForeachStatement(PsiForeachStatement statement) {
        super.visitForeachStatement(statement);
        if (RecursionUtil.containsRecursiveCalls(statement, method)) {
          statements.add(statement);
        }
      }
    });
    return statements;
  }
}
