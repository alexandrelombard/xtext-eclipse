/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.xtext.ui.editor.model.IXtextDocument;
import org.eclipse.xtext.ui.shared.contribution.SharedStateContribution;
import org.eclipse.xtext.ui.shared.internal.SharedStateContributionRegistryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class SharedStateContributionRegistryImplTest {

	private Injector injector;

	@BeforeEach
	public void createParentInjector() {
		injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CharSequence.class).to(String.class);
				binder.bind(String.class).toInstance("fromParentInjector");
			}
		});
	}
	
	@Test
	public void testCreateContributingInjector() {
		SharedStateContributionRegistryImpl registry = new SharedStateContributionRegistryImpl(injector);
		Injector myInjector = registry.createInjectorWithoutJITBindings(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CharSequenceClient.class).to(CharSequenceClientImpl.class);
			}
		});
		assertEquals("fromParentInjector", myInjector.getInstance(CharSequenceClient.class).getSeq());
		
		assertNotNull(myInjector.getExistingBinding(Key.get(CharSequenceClientImpl.class)));
		assertNotNull(myInjector.getExistingBinding(Key.get(CharSequenceClient.class)));
		assertNotNull(myInjector.getExistingBinding(Key.get(CharSequence.class)));
		assertNotNull(myInjector.getExistingBinding(Key.get(String.class)));
		
		assertNull(injector.getExistingBinding(Key.get(CharSequenceClientImpl.class)));
		assertNull(injector.getExistingBinding(Key.get(CharSequenceClient.class)));
		assertNotNull(injector.getExistingBinding(Key.get(CharSequence.class)));
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
	}
	
	@Test
	public void testCreateContribution() {
		SharedStateContributionRegistryImpl registry = new SharedStateContributionRegistryImpl(injector);
		SharedStateContribution contribution = registry.createContribution(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CharSequenceClient.class).to(CharSequenceClientImpl.class);
			}
		});
		assertTrue(contribution.isContributionSupported(CharSequenceClient.class));
		assertFalse(contribution.isContributionSupported(IXtextDocument.class));
		assertEquals("fromParentInjector", contribution.getContribution(CharSequenceClient.class).getSeq());
	}
	
	interface CharSequenceClient {
		CharSequence getSeq();
	}
	static class CharSequenceClientImpl implements CharSequenceClient {
		@Inject
		CharSequence seq;

		@Override
		public CharSequence getSeq() {
			return seq;
		}
		
	}
	
}
