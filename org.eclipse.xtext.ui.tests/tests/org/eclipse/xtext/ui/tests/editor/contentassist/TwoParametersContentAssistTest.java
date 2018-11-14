/*******************************************************************************
 * Copyright (c) 2010, 2017 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import org.eclipse.xtext.testing.InjectWith;
import org.junit.jupiter.api.extension.ExtendWith;
import org.eclipse.xtext.ui.tests.editor.contentassist.ui.tests.TwoParametersTestLanguageUiInjectorProvider;
import org.eclipse.xtext.testing.extensions.InjectionExtension;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@ExtendWith(InjectionExtension.class)
@InjectWith(TwoParametersTestLanguageUiInjectorProvider.class)
public class TwoParametersContentAssistTest extends AbstractParametersContentAssistTest {

}
