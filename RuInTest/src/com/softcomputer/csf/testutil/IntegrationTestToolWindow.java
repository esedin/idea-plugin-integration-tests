package com.softcomputer.csf.testutil;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;

public class IntegrationTestToolWindow implements ToolWindowFactory
{
   private ToolWindow toolWindow;

   @Override
   public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
   {
      this.toolWindow = toolWindow;
   }
}
