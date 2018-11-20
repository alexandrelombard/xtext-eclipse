package org.eclipse.xtext.ui.tests.core.util;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.resource.IClasspathUriResolver;
import org.eclipse.xtext.ui.tests.internal.TestsActivator;
import org.eclipse.xtext.ui.util.BundleClasspathUriResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BundleClasspathUriResolverTest extends AbstractClasspathUriResolverTest {

	private IClasspathUriResolver _resolver;

	@BeforeEach
	public void setUp() throws Exception {
		_resolver = new BundleClasspathUriResolver();
	}

	@Test public void testClasspathUriForFileInPlugin() {
		URI classpathUri = URI.createURI("classpath:/org/eclipse/xtext/ui/tests/util/simple.ecore");
		URI normalizedUri = _resolver.resolve(TestsActivator.getInstance(), classpathUri);
		assertResourceLoadable(classpathUri, normalizedUri, null);
	}

	@Test public void testClasspathUriForFileInPluginWithFragment() {
		URI classpathUri = URI.createURI("classpath:/org/eclipse/xtext/ui/tests/util/simple.ecore#/");
		URI normalizedUri = _resolver.resolve(TestsActivator.getInstance(), classpathUri);
		assertEquals("/", normalizedUri.fragment());
		assertResourceLoadable(classpathUri, normalizedUri, null);
	}

	@Test public void testPluginClasspathUriForJarredFile() {
		// doesn't work
	}

	@Test public void testClasspathUriForEcore() {
		URI classpathUri = URI.createURI("classpath:/model/Ecore.ecore");
		URI normalizedUri = _resolver.resolve(Platform.getBundle("org.eclipse.emf.ecore"), classpathUri);
		assertResourceLoadable(classpathUri, normalizedUri, null);
	}

	@Test public void testClasspathUriForEcoreWithFragment() {
		URI classpathUri = URI.createURI("classpath:/model/Ecore.ecore#/");
		URI normalizedUri = _resolver.resolve(Platform.getBundle("org.eclipse.emf.ecore"), classpathUri);
		assertEquals("/", normalizedUri.fragment());
		assertResourceLoadable(classpathUri, normalizedUri, null);
	}

}
