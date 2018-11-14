/**
 * Copyright (c) 2015, 2017 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ui.tests.workspace;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.xtext.ui.testing.util.IResourcesSetupUtil;
import org.eclipse.xtext.ui.testing.util.JavaProjectSetupUtil;
import org.eclipse.xtext.ui.tests.internal.TestsActivator;
import org.eclipse.xtext.ui.workspace.EclipseProjectConfig;
import org.eclipse.xtext.ui.workspace.EclipseProjectConfigProvider;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class EclipseWorkspaceConfigurationTest {
  @Inject
  private EclipseProjectConfigProvider projectConfigProvider;
  
  @BeforeEach
  public void setup() {
    final Injector injector = TestsActivator.getInstance().getInjector(
      TestsActivator.ORG_ECLIPSE_XTEXT_UI_TESTS_EDITOR_CONTENTASSIST_DOMAINMODELTESTLANGUAGE);
    injector.injectMembers(this);
  }
  
  @AfterEach
  public void tearDown() {
    try {
      IResourcesSetupUtil.cleanWorkspace();
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void testConfig() {
    try {
      final IWorkspaceRoot wsroot = ResourcesPlugin.getWorkspace().getRoot();
      IProject[] _projects = wsroot.getProjects();
      for (final IProject p : _projects) {
        p.delete(true, true, null);
      }
      Assertions.assertEquals(0, wsroot.getProjects().length);
      final IJavaProject project = JavaProjectSetupUtil.createJavaProject("projectA");
      JavaProjectSetupUtil.createJavaProject("projectB");
      final EclipseProjectConfig projectConfig = this.projectConfigProvider.createProjectConfig(project.getProject());
      Assertions.assertNotNull(projectConfig);
      Assertions.assertNotNull(projectConfig.getWorkspaceConfig().findProjectByName("projectB"));
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
