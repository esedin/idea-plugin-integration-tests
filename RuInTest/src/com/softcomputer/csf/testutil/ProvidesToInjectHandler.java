package com.softcomputer.csf.testutil;

import java.awt.event.MouseEvent;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.psi.PsiElement;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class ProvidesToInjectHandler implements GutterIconNavigationHandler<PsiElement>
{
   private final static String WIN_CMD = "C:\\Windows\\System32\\cmd.exe";

   private String testName;

   ProvidesToInjectHandler(String testName)
   {
      this.testName = testName;
   }

   @Override
   public void navigate(MouseEvent mouseEvent, PsiElement psiElement)
   {
      final Project project = psiElement.getProject();
      String projectPath = project.getBasePath();

      GeneralCommandLine cmd = new GeneralCommandLine();
      cmd.setWorkDirectory(projectPath + "\\tests\\services-frameworks");
      cmd.setExePath(WIN_CMD);
      cmd.addParameter("/K");
      cmd.addParameter(projectPath + "\\tests\\services-frameworks\\test.bat");
      cmd.addParameter("test");
      cmd.addParameter(testName);

      try
      {
         ProcessHandler handler = new OSProcessHandler(cmd.createProcess(), cmd.getCommandLineString());

         final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("CSF Integration Test");

         ConsoleView consoleView = TextConsoleBuilderFactory.getInstance().createBuilder(project).getConsole();
         Content content = ContentFactory.SERVICE.getInstance().createContent(consoleView.getComponent(), testName, true);
         consoleView.attachToProcess(handler);

         toolWindow.getContentManager().addContent(content);
         toolWindow.getContentManager().setSelectedContent(content);
         toolWindow.activate(
            new Runnable()
            {
               @Override
               public void run()
               {
                  IdeFocusManager.getInstance(project).requestFocus(toolWindow.getComponent(), true);
               }
            }
         );

         handler.startNotify();
      }
      catch (ExecutionException e)
      {
         e.printStackTrace();
      }
   }
}


