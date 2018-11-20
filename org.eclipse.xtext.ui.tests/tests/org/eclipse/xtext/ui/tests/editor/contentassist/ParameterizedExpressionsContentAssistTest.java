/*******************************************************************************
 * Copyright (c) 2017 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import org.eclipse.xtext.testing.InjectWith;
import org.junit.jupiter.api.extension.ExtendWith;
import org.eclipse.xtext.ui.testing.AbstractContentAssistTest;
import org.eclipse.xtext.ui.tests.editor.contentassist.ui.tests.ParameterizedExpressionsTestLanguageUiInjectorProvider;
import org.junit.jupiter.api.Test;
import org.eclipse.xtext.testing.extensions.InjectionExtension;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(ParameterizedExpressionsTestLanguageUiInjectorProvider.class)
public class ParameterizedExpressionsContentAssistTest extends AbstractContentAssistTest {

	@Test public void testEmptyModel() throws Exception {
		newBuilder().assertText("function", "{");
	}
	
	@Test public void test_01() throws Exception {
		newBuilder().append("function* f() {\n").assertText(
				"function", "yield", "{", "}");
	}

	@Test public void test_02() throws Exception {
		newBuilder().append("function* f() { yield;\n").assertText(
				"function", "yield", "{", "}");
	}

	@Test public void test_03() throws Exception {
		newBuilder().append("function* f() { yield y;\n").assertText(
				"function", "yield", "{", "}");
	}
	
	@Test public void test_04() throws Exception {
		newBuilder().append("function f() {\n").assertText(
				"function", "{", "}");
	}

	@Test public void test_05() throws Exception {
		newBuilder().append("function f() { yield;\n").assertText(
				"function", "{", "}");
	}

	@Test public void test_06() throws Exception {
		newBuilder().append("function f() { y;\n").assertText(
				"function", "{", "}");
	}
	
	@Test public void test_07() throws Exception {
		// yield y is a syntax error in that context
		newBuilder().append("function f() { yield y;\n").assertText();
	}
	
	@Test public void test_08() throws Exception {
		newBuilder().append("function f() { yield; y;\n").assertText(
				"function", "{", "}");
	}

}
