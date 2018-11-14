/*******************************************************************************
 * Copyright (c) 2013, 2017 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.core.resource

import java.lang.reflect.InvocationTargetException
import java.util.Map
import org.eclipse.core.resources.IStorage
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.IProgressMonitor
import org.eclipse.emf.common.util.URI
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.JavaCore
import org.eclipse.ui.actions.WorkspaceModifyOperation
import org.eclipse.xtext.ui.resource.JarEntryLocator
import org.eclipse.xtext.ui.resource.Storage2UriMapperJavaImpl
import org.eclipse.xtext.ui.resource.Storage2UriMapperJavaImpl.PackageFragmentRootData
import org.eclipse.xtext.ui.resource.UriValidator
import org.eclipse.xtext.ui.testing.util.JavaProjectSetupUtil.TextFile
import org.eclipse.xtext.ui.util.JavaProjectClasspathChangeAnalyzer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import static org.eclipse.xtext.ui.testing.util.IResourcesSetupUtil.*
import static org.eclipse.xtext.ui.testing.util.JavaProjectSetupUtil.*
import static org.junit.jupiter.api.Assertions.*

/**
 * @author Anton Kosyakov - Initial contribution and API
 */
class Storage2UriMapperJavaImplTest {

	Storage2UriMapperJavaImpl storage2UriMapperJava

	@BeforeEach
	def void setUp() {
		this.storage2UriMapperJava = createFreshStorage2UriMapper
		JavaCore.addElementChangedListener(storage2UriMapperJava)
	}

	@AfterEach
	def void tearDown() {
		JavaCore.removeElementChangedListener(storage2UriMapperJava)
		cleanWorkspace
	}
	
	def protected Storage2UriMapperJavaImpl createFreshStorage2UriMapper() {
		new Storage2UriMapperJavaImpl => [
			uriValidator = new UriValidator() {
				override boolean isPossiblyManaged(IStorage storage) {
					return "indexed".equals(storage.getFullPath().getFileExtension()) 
				}
				override boolean isValid(URI uri, IStorage storage) {
					return "indexed".equals(storage.getFullPath().getFileExtension()) 
				}
			}
			locator = new JarEntryLocator()
			workspace = ResourcesPlugin.workspace
			javaProjectClasspathChangeAnalyzer = new JavaProjectClasspathChangeAnalyzer
		]
	}


	@Test
	def void testOnClasspathChange() {
		assertEquals(0, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)

		val project = createJavaProject("testProject")
		
		val sizeBefore = cachedPackageFragmentRootData.size
		// it should contain all the jars from JDK now
		assertTrue(sizeBefore > 0)
		assertNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		val file = project.createJar
		addJarToClasspath(project, file)
		
		assertEquals(sizeBefore + 1, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNotNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		cachedPackageFragmentRootData.entrySet.forEach [
			assertEquals(1, value.associatedRoots.size, key)
			val head = value.associatedRoots.keySet.head
			assertTrue(head.startsWith('=testProject/'), head)
		]

		val project2 = createJavaProject('testProject2')
		addJarToClasspath(project2, file)
		
		assertEquals(sizeBefore + 1, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNotNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		cachedPackageFragmentRootData.entrySet.forEach [
			assertEquals(2, value.associatedRoots.size, key)
			val head = value.associatedRoots.keySet.head
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head)
			val head2 = value.associatedRoots.keySet.tail.head
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head2)
		]

		removeJarFromClasspath(project, file);
		
		assertEquals(sizeBefore + 1, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNotNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		cachedPackageFragmentRootData.entrySet.forEach [
			if (key.contains("foo.jar")) {
				assertEquals(1, value.associatedRoots.size, key)
				val head = value.associatedRoots.keySet.head
				assertTrue(head.startsWith('=testProject2/'), head)
			} else {
				assertEquals(2, value.associatedRoots.size, key)
				val head = value.associatedRoots.keySet.head
				assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head)
				val head2 = value.associatedRoots.keySet.tail.head
				assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head2)
			}
		]

		removeJarFromClasspath(project2, file);
		
		
		assertEquals(sizeBefore, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		cachedPackageFragmentRootData.entrySet.forEach [
			assertEquals(2, value.associatedRoots.size, key)
			val head = value.associatedRoots.keySet.head
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head)
			val head2 = value.associatedRoots.keySet.tail.head
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head2)
		]
	}

	@Test
	def void testOnCloseOpenRemoveProject() {
		val project = createJavaProject("testProject")
		val project2 = createJavaProject("testProject2")
		
		val sizeBefore = cachedPackageFragmentRootData.size

		val file = project.createJar
		addJarToClasspath(project, file)
		addJarToClasspath(project2, file)
		assertBothProjects(sizeBefore)

		project2.project.close(monitor)
		assertFirstProject(sizeBefore)

		project.project.close(monitor)
		assertNonProjects

		project.project.open(monitor)
		assertFirstProject(sizeBefore)

		project2.project.open(monitor)
		assertBothProjects(sizeBefore)

		project.project.delete(true, monitor)
		project2.project.delete(true, monitor)
		assertNonProjects
	}
	
	@Test
	def void testOnRemoveTwoProjects() {
		val project = createJavaProject("testProject")
		val project2 = createJavaProject("testProject2")
		
		val sizeBefore = cachedPackageFragmentRootData.size

		val file = project.createJar
		addJarToClasspath(project, file)
		addJarToClasspath(project2, file)
		assertBothProjects(sizeBefore)

		val op = new WorkspaceModifyOperation() {
			
			override protected execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
				project.project.delete(true, monitor)
				project2.project.delete(true, monitor)
			}
			
		}
		op.run(monitor);
		assertNonProjects
	}

	def assertNonProjects() {
		assertEquals(0, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
	}

	def assertFirstProject(int sizeBefore) {
		assertEquals(sizeBefore + 1, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNotNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])
		cachedPackageFragmentRootData.entrySet.forEach [
			assertEquals(1, value.associatedRoots.size, key)
			val head = value.associatedRoots.keySet.head
			assertTrue(head.startsWith('=testProject/'), head)
		]
	}

	def assertBothProjects(int sizeBefore) {
		assertEquals(sizeBefore + 1, cachedPackageFragmentRootData.size, "" + cachedPackageFragmentRootData)
		assertNotNull(cachedPackageFragmentRootData.keySet.findFirst[contains("foo.jar")])

		cachedPackageFragmentRootData.entrySet.forEach [
			assertEquals(2, value.associatedRoots.size, key)
			val head = value.associatedRoots.keySet.head
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head)
			val head2 = value.associatedRoots.keySet.last
			assertTrue(head.startsWith('=testProject/') || head.startsWith('=testProject2/'), head2)
		]
	}

	def createJar(IJavaProject project) {
		project.project.getFile("foo.jar") => [
			create(
				jarInputStream(
					new TextFile("foo/bar.indexed", "//empty"),
					new TextFile("META-INF/MANIFEST.MF", "Manifest-Version: 1.0\nBundle-SymbolicName: hubba.bubba\n")
				), true, monitor)
		]
	}

	def getCachedPackageFragmentRootData() {
		(storage2UriMapperJava.class.declaredFields.findFirst[name == "cachedPackageFragmentRootData"] => [
			accessible = true
		]).get(storage2UriMapperJava) as Map<String, PackageFragmentRootData>
	}

}
