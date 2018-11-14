package org.eclipse.xtext.ui.tests.core.util;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.jupiter.api.AfterEach;

public abstract class AbstractClasspathUriResolverTest {

	protected IProject _project;
	protected static final String MODEL_FILE = "simple.ecore";
	protected static final String JAR_FILE = "simple.jar";
	protected static final String JAR_FILE2 = "simpleroot.jar";
	protected static final String TEST_PROJECT_NAME = "test";

	@AfterEach
	public void tearDown() throws Exception {
		if (_project != null && _project.exists()) {
			_project.delete(true, null);
		}
	}

	protected void assertResourceLoadable(URI classpathUri, URI normalizedUri,
			String expectedUri) {
		if(expectedUri != null) {
			assertEquals(expectedUri, normalizedUri.toString());
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.getResource(normalizedUri.trimFragment(), true);
		assertNotNull(resource, "Classpath URI ot registered");
		assertTrue(resource.isLoaded(), "Resource not loaded");
	}

}
