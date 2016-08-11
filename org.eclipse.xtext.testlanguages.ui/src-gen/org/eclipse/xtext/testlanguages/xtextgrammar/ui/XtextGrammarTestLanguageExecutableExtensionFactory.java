/*
 * generated by Xtext
 */
package org.eclipse.xtext.testlanguages.xtextgrammar.ui;

import com.google.inject.Injector;
import org.eclipse.xtext.testlanguages.ui.internal.TestlanguagesActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class XtextGrammarTestLanguageExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return TestlanguagesActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return TestlanguagesActivator.getInstance().getInjector(TestlanguagesActivator.ORG_ECLIPSE_XTEXT_TESTLANGUAGES_XTEXTGRAMMAR_XTEXTGRAMMARTESTLANGUAGE);
	}
	
}
