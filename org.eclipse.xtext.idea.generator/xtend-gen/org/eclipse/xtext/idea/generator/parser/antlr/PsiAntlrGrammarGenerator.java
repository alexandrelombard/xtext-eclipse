/**
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.xtext.idea.generator.parser.antlr;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Action;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.EnumLiteralDeclaration;
import org.eclipse.xtext.EnumRule;
import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ParserRule;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.UnorderedGroup;
import org.eclipse.xtext.generator.parser.antlr.AntlrOptions;
import org.eclipse.xtext.idea.generator.IdeaPluginClassNames;
import org.eclipse.xtext.idea.generator.parser.antlr.AbstractAntlrGrammarWithActionsGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xtext.generator.parser.antlr.AntlrGrammarGenUtil;

@Singleton
@SuppressWarnings("all")
public class PsiAntlrGrammarGenerator extends AbstractAntlrGrammarWithActionsGenerator {
  @Inject
  @Extension
  private IdeaPluginClassNames _ideaPluginClassNames;
  
  @Override
  protected String getGrammarFileName(final Grammar it) {
    return this._namingExtensions.getGrammarFileName(it, "Psi");
  }
  
  @Override
  protected String compileOptions(final Grammar it, final AntlrOptions options) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("options {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("superClass=AbstractPsiAntlrParser;");
    _builder.newLine();
    {
      if (((options.isBacktrack() || options.isMemoize()) || (options.getK() >= 0))) {
        {
          boolean _isBacktrack = options.isBacktrack();
          if (_isBacktrack) {
            _builder.append("\t");
            _builder.append("backtrack=true;");
            _builder.newLine();
          }
        }
        {
          boolean _isMemoize = options.isMemoize();
          if (_isMemoize) {
            _builder.append("\t");
            _builder.append("memoize=true;");
            _builder.newLine();
          }
        }
        {
          int _k = options.getK();
          boolean _greaterEqualsThan = (_k >= 0);
          if (_greaterEqualsThan) {
            _builder.append("\t");
            _builder.append("memoize=");
            int _k_1 = options.getK();
            _builder.append(_k_1, "\t");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  protected String compileParserImports(final Grammar it, final AntlrOptions options) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("import org.eclipse.xtext.idea.parser.AbstractPsiAntlrParser;");
    _builder.newLine();
    _builder.append("import ");
    String _elementTypeProviderName = this._ideaPluginClassNames.getElementTypeProviderName(GrammarUtil.getGrammar(it));
    _builder.append(_elementTypeProviderName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("import org.eclipse.xtext.idea.parser.TokenTypeProvider;");
    _builder.newLine();
    _builder.append("import org.eclipse.xtext.parser.antlr.XtextTokenStream;");
    _builder.newLine();
    _builder.append("import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;");
    _builder.newLine();
    {
      if (((!IterableExtensions.isEmpty(Iterables.<UnorderedGroup>filter(Iterables.<EObject>concat(ListExtensions.<ParserRule, List<EObject>>map(GrammarUtil.allParserRules(it), ((Function1<ParserRule, List<EObject>>) (ParserRule it_1) -> {
        return EcoreUtil2.eAllContentsAsList(it_1);
      }))), UnorderedGroup.class))) && options.isBacktrack())) {
        _builder.append("import org.eclipse.xtext.parser.antlr.IUnorderedGroupHelper.UnorderedGroupState;");
        _builder.newLine();
      }
    }
    _builder.append("import ");
    String _gaFQName = this._grammarAccess.gaFQName(it);
    _builder.append(_gaFQName);
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("import com.intellij.lang.PsiBuilder;");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  protected String compileParserMembers(final Grammar it, final AntlrOptions options) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.newLine();
    _builder.append("@parser::members {");
    _builder.newLine();
    _builder.newLine();
    {
      boolean _isBacktrack = options.isBacktrack();
      if (_isBacktrack) {
        _builder.append("/*");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("This grammar contains a lot of empty actions to work around a bug in ANTLR.");
        _builder.newLine();
        _builder.append("  ");
        _builder.append("Otherwise the ANTLR tool will create synpreds that cannot be compiled in some rare cases.");
        _builder.newLine();
        _builder.append("*/");
        _builder.newLine();
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("protected ");
    String _gaSimpleName = this._grammarAccess.gaSimpleName(it);
    _builder.append(_gaSimpleName, "\t");
    _builder.append(" grammarAccess;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected ");
    String _simpleName = this._ideaPluginClassNames.toSimpleName(this._ideaPluginClassNames.getElementTypeProviderName(GrammarUtil.getGrammar(it)));
    _builder.append(_simpleName, "\t");
    _builder.append(" elementTypeProvider;");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public ");
    String _simpleName_1 = this._ideaPluginClassNames.toSimpleName(this._ideaPluginClassNames.getPsiInternalParserName(GrammarUtil.getGrammar(it)));
    _builder.append(_simpleName_1, "\t");
    _builder.append("(PsiBuilder builder, TokenStream input, ");
    String _simpleName_2 = this._ideaPluginClassNames.toSimpleName(this._ideaPluginClassNames.getElementTypeProviderName(GrammarUtil.getGrammar(it)));
    _builder.append(_simpleName_2, "\t");
    _builder.append(" elementTypeProvider, ");
    String _gaSimpleName_1 = this._grammarAccess.gaSimpleName(it);
    _builder.append(_gaSimpleName_1, "\t");
    _builder.append(" grammarAccess) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("this(input);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("setPsiBuilder(builder);");
    _builder.newLine();
    _builder.append("    \t");
    _builder.append("this.grammarAccess = grammarAccess;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.elementTypeProvider = elementTypeProvider;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("@Override");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected String getFirstRuleName() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return \"");
    String _name = AntlrGrammarGenUtil.<ParserRule>getOriginalElement(IterableExtensions.<ParserRule>head(GrammarUtil.allParserRules(it))).getName();
    _builder.append(_name, "\t\t");
    _builder.append("\";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  @Override
  protected boolean shouldBeSkipped(final TerminalRule it, final Grammar grammar) {
    return false;
  }
  
  @Override
  protected String compileRule(final ParserRule it, final Grammar grammar, final AntlrOptions options) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _isValidEntryRule = AntlrGrammarGenUtil.isValidEntryRule(it);
      if (_isValidEntryRule) {
        _builder.append("//Entry rule ");
        String _entryRuleName = this._grammarAccessExtensions.entryRuleName(it);
        _builder.append(_entryRuleName);
        _builder.newLineIfNotEmpty();
        String _entryRuleName_1 = this._grammarAccessExtensions.entryRuleName(it);
        _builder.append(_entryRuleName_1);
        _builder.append(" returns [");
        String _currentType = this.getCurrentType();
        _builder.append(_currentType);
        _builder.append(" current=false]");
        CharSequence _compileEntryInit = this.compileEntryInit(it, options);
        _builder.append(_compileEntryInit);
        _builder.append(":");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("{ ");
        CharSequence _markComposite = this.markComposite(it);
        _builder.append(_markComposite, "\t");
        _builder.append(" }");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("iv_");
        String _ruleName = this._grammarAccessExtensions.ruleName(AntlrGrammarGenUtil.<ParserRule>getOriginalElement(it));
        _builder.append(_ruleName, "\t");
        _builder.append("=");
        String _ruleName_1 = this._grammarAccessExtensions.ruleName(AntlrGrammarGenUtil.<ParserRule>getOriginalElement(it));
        _builder.append(_ruleName_1, "\t");
        String _defaultArgumentList = AntlrGrammarGenUtil.getDefaultArgumentList(it);
        _builder.append(_defaultArgumentList, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("{ $current=$iv_");
        String _ruleName_2 = this._grammarAccessExtensions.ruleName(AntlrGrammarGenUtil.<ParserRule>getOriginalElement(it));
        _builder.append(_ruleName_2, "\t");
        _builder.append(".current; }");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("EOF;");
        _builder.newLine();
        CharSequence _compileEntryFinally = this.compileEntryFinally(it, options);
        _builder.append(_compileEntryFinally);
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    String _compileEBNF = this.compileEBNF(it, options);
    _builder.append(_compileEBNF);
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  @Override
  protected String compileInit(final AbstractRule it, final AntlrOptions options) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((it instanceof ParserRule)) {
        boolean _isPassCurrentIntoFragment = this.isPassCurrentIntoFragment();
        boolean _not = (!_isPassCurrentIntoFragment);
        String _parameterList = AntlrGrammarGenUtil.getParameterList(((ParserRule)it), Boolean.valueOf(_not), this.getCurrentType());
        _builder.append(_parameterList);
      }
    }
    _builder.append(" returns ");
    CharSequence _compileReturns = this.compileReturns(it, options);
    _builder.append(_compileReturns);
    _builder.newLineIfNotEmpty();
    String _compileInit = super.compileInit(it, options);
    _builder.append(_compileInit);
    return _builder.toString();
  }
  
  protected CharSequence compileReturns(final AbstractRule it, final AntlrOptions options) {
    CharSequence _switchResult = null;
    boolean _matched = false;
    if (it instanceof ParserRule) {
      if (((!GrammarUtil.isDatatypeRule(AntlrGrammarGenUtil.<ParserRule>getOriginalElement(((ParserRule)it)))) && GrammarUtil.isEObjectFragmentRule(AntlrGrammarGenUtil.<ParserRule>getOriginalElement(((ParserRule)it))))) {
        _matched=true;
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("[");
        String _currentType = this.getCurrentType();
        _builder.append(_currentType);
        _builder.append(" current=in_current]");
        _switchResult = _builder;
      }
    }
    if (!_matched) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("[");
      String _currentType = this.getCurrentType();
      _builder.append(_currentType);
      _builder.append(" current=false]");
      _switchResult = _builder;
    }
    return _switchResult;
  }
  
  @Override
  protected String _dataTypeEbnf2(final Keyword it, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _markLeaf = this.markLeaf(it);
      _builder.append(_markLeaf, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _builder.append("kw=");
      String __dataTypeEbnf2 = super._dataTypeEbnf2(it, supportActions);
      _builder.append(__dataTypeEbnf2);
      _builder.newLineIfNotEmpty();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _doneLeaf = this.doneLeaf(it, "kw");
      _builder.append(_doneLeaf, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder.toString();
    } else {
      _xifexpression = super._dataTypeEbnf2(it, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String _dataTypeEbnf2(final RuleCall it, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      String _switchResult = null;
      AbstractRule _rule = it.getRule();
      boolean _matched = false;
      if (_rule instanceof EnumRule) {
        boolean _isAssigned = GrammarUtil.isAssigned(it);
        if (_isAssigned) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (_rule instanceof ParserRule) {
          boolean _isAssigned = GrammarUtil.isAssigned(it);
          if (_isAssigned) {
            _matched=true;
          }
        }
      }
      if (_matched) {
        _switchResult = super._dataTypeEbnf2(it, supportActions);
      }
      if (!_matched) {
        if (_rule instanceof EnumRule) {
          _matched=true;
        }
        if (!_matched) {
          if (_rule instanceof ParserRule) {
            _matched=true;
          }
        }
        if (_matched) {
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          CharSequence _markComposite = this.markComposite(it);
          _builder.append(_markComposite, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          String __dataTypeEbnf2 = super._dataTypeEbnf2(it, supportActions);
          _builder.append(__dataTypeEbnf2);
          _builder.newLineIfNotEmpty();
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          CharSequence _doneComposite = this.doneComposite(it);
          _builder.append(_doneComposite, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          _switchResult = _builder.toString();
        }
      }
      if (!_matched) {
        if (_rule instanceof TerminalRule) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _markLeaf = this.markLeaf(it);
          _builder_1.append(_markLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _localVar = this._grammarAccessExtensions.localVar(it);
          _builder_1.append(_localVar);
          _builder_1.append("=");
          String __dataTypeEbnf2_1 = super._dataTypeEbnf2(it, supportActions);
          _builder_1.append(__dataTypeEbnf2_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _doneLeaf = this.doneLeaf(it, this._grammarAccessExtensions.localVar(it));
          _builder_1.append(_doneLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        _switchResult = super._dataTypeEbnf2(it, supportActions);
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = super._dataTypeEbnf2(it, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String _ebnf2(final Action it, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if ((!supportActions)) {
      return super._ebnf2(it, options, supportActions);
    } else {
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _isBacktrack = options.isBacktrack();
        if (_isBacktrack) {
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("/* */");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _precedeComposite = this.precedeComposite(it);
      _builder.append(_precedeComposite, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      CharSequence _doneComposite = this.doneComposite(it);
      _builder.append(_doneComposite, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      CharSequence _associateWithSemanticElement = this.associateWithSemanticElement();
      _builder.append(_associateWithSemanticElement, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  @Override
  protected String _ebnf2(final Keyword it, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if ((!supportActions)) {
      return super._ebnf2(it, options, supportActions);
    } else {
      String _xifexpression_1 = null;
      boolean _isAssigned = GrammarUtil.isAssigned(it);
      if (_isAssigned) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _markLeaf = this.markLeaf(it);
        _builder.append(_markLeaf, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _localVar = this._grammarAccessExtensions.localVar(GrammarUtil.containingAssignment(it), it);
        _builder.append(_localVar);
        _builder.append("=");
        String __ebnf2 = super._ebnf2(it, options, supportActions);
        _builder.append(__ebnf2);
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _doneLeaf = this.doneLeaf(it, this._grammarAccessExtensions.localVar(GrammarUtil.containingAssignment(it), it));
        _builder.append(_doneLeaf, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _xifexpression_1 = _builder.toString();
      } else {
        StringConcatenation _builder_1 = new StringConcatenation();
        _builder_1.append("{");
        _builder_1.newLine();
        _builder_1.append("\t");
        CharSequence _markLeaf_1 = this.markLeaf(it);
        _builder_1.append(_markLeaf_1, "\t");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("}");
        _builder_1.newLine();
        String _localVar_1 = this._grammarAccessExtensions.localVar(it);
        _builder_1.append(_localVar_1);
        _builder_1.append("=");
        String __ebnf2_1 = super._ebnf2(it, options, supportActions);
        _builder_1.append(__ebnf2_1);
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("{");
        _builder_1.newLine();
        _builder_1.append("\t");
        CharSequence _doneLeaf_1 = this.doneLeaf(it, this._grammarAccessExtensions.localVar(it));
        _builder_1.append(_doneLeaf_1, "\t");
        _builder_1.newLineIfNotEmpty();
        _builder_1.append("}");
        _builder_1.newLine();
        _xifexpression_1 = _builder_1.toString();
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  @Override
  protected String _ebnf2(final EnumLiteralDeclaration it, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if ((!supportActions)) {
      return super._ebnf2(it, options, supportActions);
    } else {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _markLeaf = this.markLeaf(it);
      _builder.append(_markLeaf, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      String _localVar = this._grammarAccessExtensions.localVar(it);
      _builder.append(_localVar);
      _builder.append("=");
      String __ebnf2 = super._ebnf2(it, options, supportActions);
      _builder.append(__ebnf2);
      _builder.newLineIfNotEmpty();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      CharSequence _doneLeaf = this.doneLeaf(it, this._grammarAccessExtensions.localVar(it));
      _builder.append(_doneLeaf, "\t");
      _builder.newLineIfNotEmpty();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder.toString();
    }
    return _xifexpression;
  }
  
  @Override
  protected String _ebnf2(final RuleCall it, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      String _switchResult = null;
      AbstractRule _rule = it.getRule();
      boolean _matched = false;
      if (_rule instanceof EnumRule) {
        boolean _isAssigned = GrammarUtil.isAssigned(it);
        if (_isAssigned) {
          _matched=true;
        }
      }
      if (!_matched) {
        if (_rule instanceof ParserRule) {
          boolean _isAssigned = GrammarUtil.isAssigned(it);
          if (_isAssigned) {
            _matched=true;
          }
        }
      }
      if (_matched) {
        _switchResult = super._ebnf2(it, options, supportActions);
      }
      if (!_matched) {
        if (_rule instanceof EnumRule) {
          _matched=true;
        }
        if (!_matched) {
          if (_rule instanceof ParserRule) {
            boolean _isDatatypeRule = GrammarUtil.isDatatypeRule(AntlrGrammarGenUtil.<AbstractRule>getOriginalElement(it.getRule()));
            if (_isDatatypeRule) {
              _matched=true;
            }
          }
        }
        if (_matched) {
          StringConcatenation _builder = new StringConcatenation();
          {
            boolean _isBacktrack = options.isBacktrack();
            if (_isBacktrack) {
              _builder.append("{");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("/* */");
              _builder.newLine();
              _builder.append("}");
              _builder.newLine();
            }
          }
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          CharSequence _markComposite = this.markComposite(it);
          _builder.append(_markComposite, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          String __ebnf2 = super._ebnf2(it, options, supportActions);
          _builder.append(__ebnf2);
          _builder.newLineIfNotEmpty();
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          CharSequence _doneComposite = this.doneComposite(it);
          _builder.append(_doneComposite, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          _switchResult = _builder.toString();
        }
      }
      if (!_matched) {
        if (_rule instanceof ParserRule) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          {
            boolean _isBacktrack_1 = options.isBacktrack();
            if (_isBacktrack_1) {
              _builder_1.append("{");
              _builder_1.newLine();
              _builder_1.append("\t");
              _builder_1.append("/* */");
              _builder_1.newLine();
              _builder_1.append("}");
              _builder_1.newLine();
            }
          }
          _builder_1.append("{");
          _builder_1.newLine();
          {
            boolean _isEObjectFragmentRuleCall = GrammarUtil.isEObjectFragmentRuleCall(it);
            if (_isEObjectFragmentRuleCall) {
              _builder_1.append("\t");
              _builder_1.append("if (!$current) {");
              _builder_1.newLine();
              _builder_1.append("\t");
              _builder_1.append("\t");
              CharSequence _associateWithSemanticElement = this.associateWithSemanticElement();
              _builder_1.append(_associateWithSemanticElement, "\t\t");
              _builder_1.newLineIfNotEmpty();
              _builder_1.append("\t");
              _builder_1.append("\t");
              _builder_1.append("$current = true;");
              _builder_1.newLine();
              _builder_1.append("\t");
              _builder_1.append("}");
              _builder_1.newLine();
            }
          }
          _builder_1.append("\t");
          CharSequence _markComposite_1 = this.markComposite(it);
          _builder_1.append(_markComposite_1, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _localVar = this._grammarAccessExtensions.localVar(it);
          _builder_1.append(_localVar);
          _builder_1.append("=");
          String __ebnf2_1 = super._ebnf2(it, options, supportActions);
          _builder_1.append(__ebnf2_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append("$current = $");
          String _localVar_1 = this._grammarAccessExtensions.localVar(it);
          _builder_1.append(_localVar_1, "\t");
          _builder_1.append(".current;");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("\t");
          CharSequence _doneComposite_1 = this.doneComposite(it);
          _builder_1.append(_doneComposite_1, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        if (_rule instanceof TerminalRule) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _markLeaf = this.markLeaf(it);
          _builder_1.append(_markLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _localVar = this._grammarAccessExtensions.localVar(it);
          _builder_1.append(_localVar);
          _builder_1.append("=");
          String __ebnf2_1 = super._ebnf2(it, options, supportActions);
          _builder_1.append(__ebnf2_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _doneLeaf = this.doneLeaf(it, this._grammarAccessExtensions.localVar(it));
          _builder_1.append(_doneLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        _switchResult = super._ebnf2(it, options, supportActions);
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = super._ebnf2(it, options, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String crossrefEbnf(final AbstractRule it, final RuleCall call, final CrossReference ref, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      String _switchResult = null;
      boolean _matched = false;
      if (it instanceof EnumRule) {
        _matched=true;
      }
      if (!_matched) {
        if (it instanceof ParserRule) {
          _matched=true;
        }
      }
      if (_matched) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _markComposite = this.markComposite(ref);
        _builder.append(_markComposite, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _crossrefEbnf = super.crossrefEbnf(it, call, ref, supportActions);
        _builder.append(_crossrefEbnf);
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _doneComposite = this.doneComposite(ref);
        _builder.append(_doneComposite, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        _switchResult = _builder.toString();
      }
      if (!_matched) {
        if (it instanceof TerminalRule) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _markLeaf = this.markLeaf(ref);
          _builder_1.append(_markLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _localVar = this._grammarAccessExtensions.localVar(GrammarUtil.containingAssignment(ref));
          _builder_1.append(_localVar);
          _builder_1.append("=");
          String _crossrefEbnf_1 = super.crossrefEbnf(it, call, ref, supportActions);
          _builder_1.append(_crossrefEbnf_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _doneLeaf = this.doneLeaf(ref, this._grammarAccessExtensions.localVar(GrammarUtil.containingAssignment(ref)));
          _builder_1.append(_doneLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        throw new IllegalStateException(("crossrefEbnf is not supported for " + it));
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = super.crossrefEbnf(it, call, ref, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String _assignmentEbnf(final CrossReference it, final Assignment assignment, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      StringConcatenation _builder = new StringConcatenation();
      {
        boolean _isBacktrack = options.isBacktrack();
        if (_isBacktrack) {
          _builder.append("{");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("/* */");
          _builder.newLine();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("if (!$current) {");
      _builder.newLine();
      _builder.append("\t\t");
      CharSequence _associateWithSemanticElement = this.associateWithSemanticElement();
      _builder.append(_associateWithSemanticElement, "\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("$current = true;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      String __assignmentEbnf = super._assignmentEbnf(it, assignment, options, supportActions);
      _builder.append(__assignmentEbnf);
      _xifexpression = _builder.toString();
    } else {
      _xifexpression = super._assignmentEbnf(it, assignment, options, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String _assignmentEbnf(final AbstractElement it, final Assignment assignment, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      StringConcatenation _builder = new StringConcatenation();
      String __assignmentEbnf = super._assignmentEbnf(it, assignment, options, supportActions);
      _builder.append(__assignmentEbnf);
      _builder.newLineIfNotEmpty();
      _builder.append("{");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("if (!$current) {");
      _builder.newLine();
      _builder.append("\t\t");
      CharSequence _associateWithSemanticElement = this.associateWithSemanticElement();
      _builder.append(_associateWithSemanticElement, "\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("$current = true;");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _xifexpression = _builder.toString();
    } else {
      _xifexpression = super._assignmentEbnf(it, assignment, options, supportActions);
    }
    return _xifexpression;
  }
  
  @Override
  protected String _assignmentEbnf(final RuleCall it, final Assignment assignment, final AntlrOptions options, final boolean supportActions) {
    String _xifexpression = null;
    if (supportActions) {
      String _switchResult = null;
      AbstractRule _rule = it.getRule();
      boolean _matched = false;
      if (_rule instanceof EnumRule) {
        _matched=true;
      }
      if (!_matched) {
        if (_rule instanceof ParserRule) {
          _matched=true;
        }
      }
      if (_matched) {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _markComposite = this.markComposite(it);
        _builder.append(_markComposite, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        String _localVar = this._grammarAccessExtensions.localVar(assignment, it);
        _builder.append(_localVar);
        _builder.append("=");
        String __assignmentEbnf = super._assignmentEbnf(it, assignment, options, supportActions);
        _builder.append(__assignmentEbnf);
        _builder.newLineIfNotEmpty();
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _doneComposite = this.doneComposite(it);
        _builder.append(_doneComposite, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("if(!$current) {");
        _builder.newLine();
        _builder.append("\t\t");
        CharSequence _associateWithSemanticElement = this.associateWithSemanticElement();
        _builder.append(_associateWithSemanticElement, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("$current = true;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _switchResult = _builder.toString();
      }
      if (!_matched) {
        if (_rule instanceof TerminalRule) {
          _matched=true;
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _markLeaf = this.markLeaf(it);
          _builder_1.append(_markLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _localVar_1 = this._grammarAccessExtensions.localVar(assignment, it);
          _builder_1.append(_localVar_1);
          _builder_1.append("=");
          String __assignmentEbnf_1 = super._assignmentEbnf(it, assignment, options, supportActions);
          _builder_1.append(__assignmentEbnf_1);
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append("if(!$current) {");
          _builder_1.newLine();
          _builder_1.append("\t\t");
          CharSequence _associateWithSemanticElement_1 = this.associateWithSemanticElement();
          _builder_1.append(_associateWithSemanticElement_1, "\t\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("\t\t");
          _builder_1.append("$current = true;");
          _builder_1.newLine();
          _builder_1.append("\t");
          _builder_1.append("}");
          _builder_1.newLine();
          _builder_1.append("}");
          _builder_1.newLine();
          _builder_1.append("{");
          _builder_1.newLine();
          _builder_1.append("\t");
          CharSequence _doneLeaf = this.doneLeaf(it, this._grammarAccessExtensions.localVar(assignment, it));
          _builder_1.append(_doneLeaf, "\t");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          _switchResult = _builder_1.toString();
        }
      }
      if (!_matched) {
        throw new IllegalStateException(("assignmentEbnf is not supported for " + it));
      }
      _xifexpression = _switchResult;
    } else {
      _xifexpression = super._assignmentEbnf(it, assignment, options, supportActions);
    }
    return _xifexpression;
  }
  
  protected CharSequence markLeaf(final EObject it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("markLeaf(elementTypeProvider.get");
    String _grammarElementIdentifier = this._grammarAccessExtensions.grammarElementIdentifier(AntlrGrammarGenUtil.<EObject>getOriginalElement(it));
    _builder.append(_grammarElementIdentifier);
    _builder.append("ElementType());");
    return _builder;
  }
  
  protected CharSequence doneLeaf(final EObject it, final String token) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("doneLeaf(");
    _builder.append(token);
    _builder.append(");");
    return _builder;
  }
  
  protected CharSequence markComposite(final EObject it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("markComposite(elementTypeProvider.get");
    String _grammarElementIdentifier = this._grammarAccessExtensions.grammarElementIdentifier(AntlrGrammarGenUtil.<EObject>getOriginalElement(it));
    _builder.append(_grammarElementIdentifier);
    _builder.append("ElementType());");
    return _builder;
  }
  
  protected CharSequence doneComposite(final EObject it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("doneComposite();");
    return _builder;
  }
  
  protected CharSequence precedeComposite(final EObject it) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("precedeComposite(elementTypeProvider.get");
    String _grammarElementIdentifier = this._grammarAccessExtensions.grammarElementIdentifier(AntlrGrammarGenUtil.<EObject>getOriginalElement(it));
    _builder.append(_grammarElementIdentifier);
    _builder.append("ElementType());");
    return _builder;
  }
  
  protected CharSequence associateWithSemanticElement() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("associateWithSemanticElement();");
    return _builder;
  }
  
  @Override
  protected boolean isPassCurrentIntoFragment() {
    return true;
  }
  
  @Override
  protected String getCurrentType() {
    return "Boolean";
  }
}
