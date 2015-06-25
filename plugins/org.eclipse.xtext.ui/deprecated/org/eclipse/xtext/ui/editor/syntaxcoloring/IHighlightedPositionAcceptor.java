/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.syntaxcoloring;

/**
 * This interface is not intended to be implemented by clients.
 * @deprecated use {@link org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor} instead
 */
@Deprecated
public interface IHighlightedPositionAcceptor {
	
	/**
	 * Associates a text range with a style.
	 * @param offset the offset of the range.
	 * @param length the length of the range.
	 * @param id the ids of the highlighting style, that should be applied.
	 */
	void addPosition(int offset, int length, String... id);
}