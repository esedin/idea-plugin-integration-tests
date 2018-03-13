package com.softcomputer.csf.testutil;

import static com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.LEFT;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;

public class MyLineMarkerProvider implements LineMarkerProvider
{
   private static final String TEST_ANNOTATION = "org.testng.annotations.Test";

   private static final Icon TEST_ICON = IconLoader.getIcon("/icon/right-arrow.png");
   private static final Icon TEST_CLASS_ICON = IconLoader.getIcon("/icon/right-arrows.png");

   public LineMarkerInfo getLineMarkerInfo(@NotNull final PsiElement element)
   {
      if (element instanceof PsiClass)
      {
         PsiClass clazz = (PsiClass) element;

         for (PsiMethod method : clazz.getMethods())
         {
            if (AnnotationUtil.isAnnotated(method, Collections.singletonList(TEST_ANNOTATION)))
            {
               return new LineMarkerInfo<>(element, element.getTextRange(), TEST_CLASS_ICON,
                  Pass.UPDATE_ALL, null, new ProvidesToInjectHandler(clazz.getName()), LEFT);
            }
         }
      }

      if (!(element instanceof PsiMethod)) return null;

      PsiMethod method = (PsiMethod) element;

      if (!isTestNgTestMethod(method)) return null;

      String methodName = method.getName();
      String testClass = method.getContainingClass().getName();
      String testName = testClass + "#" + methodName;

      return new LineMarkerInfo<>(element, element.getTextRange(), TEST_ICON, Pass.UPDATE_ALL, null,
         new ProvidesToInjectHandler(testName), LEFT);
   }

   private boolean isTestNgTestMethod(PsiMethod method)
   {
      return AnnotationUtil.isAnnotated(method, TEST_ANNOTATION, false);
   }

   @Override
   public void collectSlowLineMarkers(@NotNull List<PsiElement> list, @NotNull Collection<LineMarkerInfo> collection)
   {

   }
}
