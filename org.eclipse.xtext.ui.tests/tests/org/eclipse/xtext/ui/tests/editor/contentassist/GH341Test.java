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
import org.eclipse.xtext.ui.testing.ContentAssistProcessorTestBuilder;
import org.eclipse.xtext.ui.tests.editor.contentassist.ui.tests.GH341TestLanguageUiInjectorProvider;
import org.junit.jupiter.api.Test;
import org.eclipse.xtext.testing.extensions.InjectionExtension;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@InjectWith(GH341TestLanguageUiInjectorProvider.class)
@ExtendWith(InjectionExtension.class)
public class GH341Test extends AbstractContentAssistTest {
	
	@Test public void testEmptyDocument() throws Exception {
		newBuilder().assertText(	"works", "broken", "define");
	}
	
	@Test public void test_works_01() throws Exception {
		newBuilder("works ").assertText("Name");
	}
	
	@Test public void test_works_02() throws Exception {
		newBuilder("works myThing ").assertText("Ids", "works", "broken", "define", "myThing");
	}
	
	@Test public void test_works_03() throws Exception {
		newBuilder("works myThing myId ").assertText(".", ";", "X");
	}
	
	@Test public void test_works_04() throws Exception {
		newBuilder("works n x.x ").assertText(".", ";", "X");
	}
	
	@Test public void test_works_05() throws Exception {
		newBuilder("works n x.x; ").assertText("Ids", "works", "broken", "define", "n");
	}
	
	@Test public void test_works_07() throws Exception {
		newBuilder("works n x.x; n. ").assertText("n", "Ids");
	}
	
	@Test public void test_works_08() throws Exception {
		newBuilder("works n x.x; n.<|>n X;").assertProposalAtCursor("n");
	}
	
	@Test public void test_broken_01() throws Exception {
		newBuilder("broken ").assertText("Name");
	}
	
	@Test public void test_broken_02() throws Exception {
		newBuilder("broken myThing ").assertText("Ids", "works", "broken", "define", "myThing");
	}
	
	@Test public void test_broken_03() throws Exception {
		newBuilder("broken myThing myId ").assertText(".", ";", "X");
	}
	
	@Test public void test_broken_04() throws Exception {
		newBuilder("broken n x.x ").assertText(".", ";", "X");
	}
	
	@Test public void test_broken_05() throws Exception {
		newBuilder("broken n x.x; ").assertText("Ids", "works", "broken", "define", "n");
	}
	
	@Test public void test_broken_07() throws Exception {
		newBuilder("broken n x.x; n. ").assertText("n", "Ids");
	}
	
	@Test public void test_broken_08() throws Exception {
		newBuilder("broken n x.x; n.<|>n X;").assertProposalAtCursor("n");
	}
	
	@Test public void test_define_01() throws Exception {
		newBuilder("define ").assertText("stream");
	}
	
	@Test public void test_define_02() throws Exception {
		newBuilder("define stream ").assertText("Name");
	}
	
	private ContentAssistProcessorTestBuilder newBuilder(String model) throws Exception {
		return newBuilder().append(model);
	}
}
