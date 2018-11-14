/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.xtext.ui.editor.contentassist.FQNPrefixMatcher;
import org.eclipse.xtext.ui.editor.contentassist.FQNPrefixMatcher.DefaultLastSegmentFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class DefaultLastSegmentFinderTest {

	private DefaultLastSegmentFinder finder;

	@BeforeEach
	public void setUp() throws Exception {
		finder = new FQNPrefixMatcher.DefaultLastSegmentFinder();
	}
	
	@Test public void testGetLastSegment_01() {
		assertEquals(null, finder.getLastSegment(null, '.'));
		assertEquals(null, finder.getLastSegment("", '.'));
	}
	
	@Test public void testGetLastSegment_02() {
		assertEquals(null, finder.getLastSegment("a", '.'));
		assertEquals(finder.getLastSegment("A", '.'), "A");
	}
	
	@Test public void testGetLastSegment_03() {
		assertEquals(null, finder.getLastSegment("aA", '.'));
		assertEquals(finder.getLastSegment("AA", '.'), "AA");
		assertEquals(finder.getLastSegment("Aa", '.'), "Aa");
	}
	
	@Test public void testGetLastSegment_04() {
		assertEquals(finder.getLastSegment("prefix.aA", '.'), "aA");
		assertEquals(finder.getLastSegment("prefix.aA.aa", '.'), "aa");
		assertEquals(finder.getLastSegment("preFix.A", '.'), "A");
		assertEquals(finder.getLastSegment("prefix.AA", '.'), "AA");
		assertEquals(finder.getLastSegment("prefix.Aa", '.'), "Aa");
	}
	
	@Test public void testGetLastSegment_05() {
		assertEquals(finder.getLastSegment("prefix.A.a", '.'), "A.a");
		assertEquals(finder.getLastSegment("preFix.A.A", '.'), "A.A");
		assertEquals(finder.getLastSegment("prefix.AA.A", '.'), "AA.A");
		assertEquals(finder.getLastSegment("prefix.Aa.A", '.'), "Aa.A");
	}
}
