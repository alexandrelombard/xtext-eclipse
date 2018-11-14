/*******************************************************************************
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.shared;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.google.inject.Binder;
import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class PlainInjectorTest {
	
	@Test
	public void testEmptyInjector() {
		Injector injector = Guice.createInjector();
		assertNull(injector.getExistingBinding(Key.get(String.class)));
		String emptyString = injector.getInstance(String.class);
		assertEquals("", emptyString);
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
	}
	
	@Test
	public void testEmptyInjectorNoJIT() {
		Injector injector = Guice.createInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.requireExplicitBindings();
			}
		});
		assertNull(injector.getExistingBinding(Key.get(String.class)));
		assertThrows(ConfigurationException.class, () -> injector.getInstance(String.class));
	}
	
	@Test
	public void testEmptyChildInjector() {
		Injector parentInjector = Guice.createInjector();
		Injector injector = parentInjector.createChildInjector();
		assertNull(injector.getExistingBinding(Key.get(String.class)));
		String emptyString = injector.getInstance(String.class);
		assertEquals("", emptyString);
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
		assertNotNull(parentInjector.getExistingBinding(Key.get(String.class)));
	}
	
	@Test
	public void testChildInjector() {
		Injector parentInjector = Guice.createInjector();
		Injector injector = parentInjector.createChildInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CharSequence.class).to(String.class);
			}
		});
		assertNotNull(injector.getExistingBinding(Key.get(CharSequence.class)));
		// Parent allows JIT bindings and those are always created in the ancestor
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
		CharSequence emptyString = injector.getInstance(CharSequence.class);
		assertEquals("", emptyString);
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
		assertNotNull(parentInjector.getExistingBinding(Key.get(String.class)));
	}
	
	@Test
	public void testEmptyChildInjectorNoJIT() {
		Injector injector = Guice.createInjector().createChildInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.requireExplicitBindings();
			}
		});
		assertNull(injector.getExistingBinding(Key.get(String.class)));
		assertThrows(ConfigurationException.class, () -> injector.getInstance(String.class));
	}
	
	@Test
	public void testChildInjectorNoJIT() {
		Injector parentInjector = Guice.createInjector();
		Injector injector = parentInjector.createChildInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.requireExplicitBindings();
				binder.bind(CharSequence.class).to(String.class);
			}
		});
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
		assertNotNull(parentInjector.getExistingBinding(Key.get(String.class)));
		assertEquals("", injector.getInstance(CharSequence.class));
		assertNotNull(parentInjector.getExistingBinding(Key.get(String.class)));
	}
	
	@Test
	public void testGrandChildInjectorNoJIT() {
		Injector parentInjector = Guice.createInjector();
		Injector injector = parentInjector.createChildInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.requireExplicitBindings();
			}
		}).createChildInjector(new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CharSequence.class).to(String.class);				
			}
		});
		assertNotNull(injector.getExistingBinding(Key.get(String.class)));
		assertNull(parentInjector.getExistingBinding(Key.get(String.class)));
		assertEquals("", injector.getInstance(CharSequence.class));
		assertNull(parentInjector.getExistingBinding(Key.get(String.class)));
	}
}
