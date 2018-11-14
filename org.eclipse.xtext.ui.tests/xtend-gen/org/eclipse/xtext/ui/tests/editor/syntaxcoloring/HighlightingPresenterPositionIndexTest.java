/**
 * Copyright (c) 2013 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.ui.tests.editor.syntaxcoloring;

import java.util.Collections;
import java.util.List;
import org.eclipse.xtext.ui.editor.syntaxcoloring.AttributedPosition;
import org.eclipse.xtext.ui.editor.syntaxcoloring.HighlightingPresenter;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
@SuppressWarnings("all")
public class HighlightingPresenterPositionIndexTest extends HighlightingPresenter {
  private List<AttributedPosition> positions;
  
  @BeforeEach
  public void setUp() {
    AttributedPosition _attributedPosition = new AttributedPosition(0, 2, null, null);
    AttributedPosition _attributedPosition_1 = new AttributedPosition(2, 2, null, null);
    this.positions = Collections.<AttributedPosition>unmodifiableList(CollectionLiterals.<AttributedPosition>newArrayList(_attributedPosition, _attributedPosition_1));
  }
  
  @Test
  public void testIndexAtOffset() {
    Assertions.assertEquals(0, this.computeIndexAtOffset(this.positions, (-1)));
    Assertions.assertEquals(0, this.computeIndexAtOffset(this.positions, 0));
    Assertions.assertEquals(1, this.computeIndexAtOffset(this.positions, 1));
    Assertions.assertEquals(1, this.computeIndexAtOffset(this.positions, 2));
    Assertions.assertEquals(2, this.computeIndexAtOffset(this.positions, 3));
    Assertions.assertEquals(2, this.computeIndexAtOffset(this.positions, 4));
  }
  
  @Test
  public void testIndexAfterOffset() {
    Assertions.assertEquals(0, this.computeIndexAfterOffset(this.positions, (-1)));
    Assertions.assertEquals(1, this.computeIndexAfterOffset(this.positions, 0));
    Assertions.assertEquals(1, this.computeIndexAfterOffset(this.positions, 1));
    Assertions.assertEquals(2, this.computeIndexAfterOffset(this.positions, 2));
    Assertions.assertEquals(2, this.computeIndexAfterOffset(this.positions, 3));
    Assertions.assertEquals(2, this.computeIndexAfterOffset(this.positions, 4));
  }
  
  @Test
  public void testIndexEndingAtOrEnclosingOffset() {
    Assertions.assertEquals(0, this.computeIndexEndingAtOrEnclosingOffset(this.positions, (-1)));
    Assertions.assertEquals(0, this.computeIndexEndingAtOrEnclosingOffset(this.positions, 0));
    Assertions.assertEquals(0, this.computeIndexEndingAtOrEnclosingOffset(this.positions, 1));
    Assertions.assertEquals(1, this.computeIndexEndingAtOrEnclosingOffset(this.positions, 2));
    Assertions.assertEquals(1, this.computeIndexEndingAtOrEnclosingOffset(this.positions, 3));
    Assertions.assertEquals(2, this.computeIndexEndingAtOrEnclosingOffset(this.positions, 4));
  }
}
