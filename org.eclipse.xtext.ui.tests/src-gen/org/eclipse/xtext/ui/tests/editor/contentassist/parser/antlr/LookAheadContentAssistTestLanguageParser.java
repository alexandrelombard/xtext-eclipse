/*
 * generated by Xtext
 */
package org.eclipse.xtext.ui.tests.editor.contentassist.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.ui.tests.editor.contentassist.parser.antlr.internal.InternalLookAheadContentAssistTestLanguageParser;
import org.eclipse.xtext.ui.tests.editor.contentassist.services.LookAheadContentAssistTestLanguageGrammarAccess;

public class LookAheadContentAssistTestLanguageParser extends AbstractAntlrParser {

	@Inject
	private LookAheadContentAssistTestLanguageGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalLookAheadContentAssistTestLanguageParser createParser(XtextTokenStream stream) {
		return new InternalLookAheadContentAssistTestLanguageParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}

	public LookAheadContentAssistTestLanguageGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(LookAheadContentAssistTestLanguageGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
