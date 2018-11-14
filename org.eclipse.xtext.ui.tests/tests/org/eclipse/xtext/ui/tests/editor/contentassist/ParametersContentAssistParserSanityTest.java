/*******************************************************************************
 * Copyright (c) 2017 itemis AG (http://www.itemis.de) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.tests.editor.contentassist;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.ui.tests.editor.contentassist.ide.contentassist.antlr.TwoParametersTestLanguageParser;
import org.eclipse.xtext.ui.tests.editor.contentassist.ide.contentassist.antlr.internal.InternalTwoParametersTestLanguageParser;
import org.eclipse.xtext.ui.tests.editor.contentassist.services.TwoParametersTestLanguageGrammarAccess;
import org.eclipse.xtext.ui.tests.editor.contentassist.tests.TwoParametersTestLanguageInjectorProvider;
import org.eclipse.xtext.xbase.lib.util.ReflectExtensions;
import org.eclipse.xtext.xtext.FlattenedGrammarAccess;
import org.eclipse.xtext.xtext.RuleFilter;
import org.eclipse.xtext.xtext.RuleNames;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Injector;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class ParametersContentAssistParserSanityTest {

	private static class DummyParser extends TwoParametersTestLanguageParser {
		@Override
		protected String getRuleName(AbstractElement element) {
			return super.getRuleName(element);
		}
	}

	/**
	 * Returns test data.
	 */
	public static Stream<Object[]> methodNames() {
		try {
			TwoParametersTestLanguageInjectorProvider injectorProvider = new TwoParametersTestLanguageInjectorProvider();
			try {
				injectorProvider.setupRegistry();
				Injector injector = injectorProvider.getInjector();
				DummyParser dummyParser = new DummyParser();
				TwoParametersTestLanguageGrammarAccess grammarAccess = injector.getInstance(TwoParametersTestLanguageGrammarAccess.class);
				RuleNames ruleNames = RuleNames.getRuleNames(grammarAccess.getGrammar(), false);
				RuleFilter ruleFilter = new RuleFilter();
				ruleFilter.setDiscardUnreachableRules(true);
				FlattenedGrammarAccess flattened = new FlattenedGrammarAccess(ruleNames, ruleFilter);
				Grammar flattenedGrammar = flattened.getFlattenedGrammar();
				dummyParser.setGrammarAccess(grammarAccess);
				dummyParser.setNameMappings(new TwoParametersTestLanguageParser.NameMappings(grammarAccess));
				dummyParser.getRuleName(null);
				Map<AbstractElement, String> nameMappings = new ReflectExtensions().get(dummyParser.getNameMappings(), "mappings");
				List<String> methodNames = Lists.newArrayList(nameMappings.values());
				Collections.sort(methodNames);
				Set<String> flattenedRuleNames = Sets.newHashSet(Lists.transform(flattenedGrammar.getRules(), r -> r.getName()));
				return Lists.transform(methodNames, (s) -> new Object[] { s, flattenedRuleNames }).stream();
			} finally {
				injectorProvider.restoreRegistry();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Tests if the method is actually present in the parser.
	 * @param methodName The name of the method that need to be present in the InternalN4JSParser class.
	 * @param flattenedRuleNames All rules that are supposed to be present in the generated parser class
	 */
	@ParameterizedTest
	@MethodSource("methodNames")
	public void testMethodExists(String methodName, Set<String> flattenedRuleNames) throws Exception {
		Class<?> type = InternalTwoParametersTestLanguageParser.class;
		try {
			Method method = type.getDeclaredMethod(methodName);
			// will throw if missing but just to make it more obvious
			assertNotNull(method, "method is not null");
		} catch (NoSuchMethodException e) {
			String withoutPrefix = methodName.substring("rule__".length());
			String withoutSuffix = withoutPrefix.substring(0, withoutPrefix.indexOf('_'));
			String flattenedRuleName = "rule" + withoutSuffix;
			switch (flattenedRuleName) {
				case "ruleIdOrKeyword":
					assertTrue(flattenedRuleNames.containsAll(Arrays.asList("norm1_IdOrKeyword")),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm2_IdOrKeyword"),
							"methodName " + methodName + " points to a never called rule");
					break;
				case "ruleIdOrKeyword2":
					assertTrue(flattenedRuleNames.containsAll(Arrays.asList("norm2_IdOrKeyword2", "norm7_IdOrKeyword2")),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm1_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm3_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm4_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm5_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm6_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm8_IdOrKeyword2"),
							"methodName " + methodName + " points to a never called rule");
					break;
				case "ruleScenario6":
					assertTrue(flattenedRuleNames.containsAll(Arrays.asList("norm3_Scenario6")),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm1_Scenario6"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm2_Scenario6"),
							"methodName " + methodName + " points to a never called rule");
					assertFalse(flattenedRuleNames.contains("norm4_Scenario6"),
							"methodName " + methodName + " points to a never called rule");
					break;
				default:
					assertFalse(flattenedRuleNames.contains(flattenedRuleName),
							"methodName " + methodName + " points to a never called rule");
				}
		}
	}
}
