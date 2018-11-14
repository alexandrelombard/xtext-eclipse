/*******************************************************************************
 * Copyright (c) 2017 TypeFox GmbH (http://www.typefox.io) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.model.edit;

import org.eclipse.xtext.ui.editor.model.edit.ChangeSerializerTextEditComposer;
import org.eclipse.xtext.ui.editor.model.edit.ITextEditComposer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Moritz Eysholdt - Initial contribution and API
 */
public class ChangeSerializerComposerTest extends AbstractTextEditComposerTest {

	@Override
	protected ITextEditComposer createComposer() {
		return get(ChangeSerializerTextEditComposer.class);
	}

	@Override
	@Test
	@Disabled
	public void testRemoveRootObject() throws Exception {
		super.testRemoveRootObject();
	}

	@Override
	@Test
	@Disabled
	public void testReplaceRootObject() throws Exception {
		super.testReplaceRootObject();
	}

	@Override
	@Disabled
	@Test
	public void testObjectReplacement() throws Exception {
		// doesn't work because of xtext.xtext still uses old formatter.
		super.testObjectReplacement();
	}

}
