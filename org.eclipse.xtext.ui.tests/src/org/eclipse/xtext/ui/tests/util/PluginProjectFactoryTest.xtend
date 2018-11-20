/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.util

import org.junit.jupiter.api.Test
import static org.junit.jupiter.api.Assertions.*
import org.eclipse.xtext.ui.util.PluginProjectFactory
import static org.eclipse.xtext.ui.testing.util.LineDelimiters.toPlatform

/**
 * @author dhuebner - Initial contribution and API
 */
class PluginProjectFactoryTest {

	@Test
	def void testAddToBuildProperties() {
		val factory = new TestablePluginProjectFactory
		var content = new StringBuilder
		factory.addToBuildProperties(content, null, "key")
		assertEquals("", content.toString)

		content = new StringBuilder
		factory.addToBuildProperties(content, #[], "key")
		assertEquals("", content.toString)

		content = new StringBuilder
		factory.addToBuildProperties(content, #["entry1"], "key")
		assertEquals('''
		key = entry1'''.toString, content.toString)

		content = new StringBuilder
		factory.addToBuildProperties(content, #["entry1", "entry2", "entry3"], "key")
		assertEquals('''
		key = entry1,\
		      entry2,\
		      entry3'''.toString, toPlatform(content.toString))
	}
}

class TestablePluginProjectFactory extends PluginProjectFactory {

	override addToBuildProperties(StringBuilder content, Iterable<String> entries, String entryName) {
		super.addToBuildProperties(content, entries, entryName)
	}

}