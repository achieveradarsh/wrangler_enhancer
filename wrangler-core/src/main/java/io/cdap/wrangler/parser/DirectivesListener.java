/*
 * Copyright © 2023 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.parser.BooleanToken;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.DirectiveName;
import io.cdap.wrangler.api.parser.FloatToken;
import io.cdap.wrangler.api.parser.IntegerToken;
import io.cdap.wrangler.api.parser.PropertiesToken;
import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.TextToken;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenGroup;
import io.cdap.wrangler.grammar.DirectivesParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * This class <code>DirectivesListener</code> listens to parse events from ANTLR and builds tokens.
 */
public class DirectivesListener extends org.antlr.v4.runtime.tree.ParseTreeListener {
  private final TokenGroup tokens;

  /**
   * Constructor.
   */
  public DirectivesListener() {
    this.tokens = new TokenGroup();
  }

  /**
   * Visit a parse tree produced by the parser.
   *
   * @param tree the parse tree
   */
  public void visit(ParseTree tree) {
    ParseTreeWalker walker = new ParseTreeWalker();
    walker.walk(this, tree);
  }

  /**
   * Get the tokens extracted from the parse tree.
   *
   * @return the tokens
   */
  public TokenGroup getTokens() {
    return tokens;
  }

  /**
   * Enter a parse tree produced by {@link DirectivesParser#directive}.
   *
   * @param ctx the parse tree
   */
  public void enterDirective(DirectivesParser.DirectiveContext ctx) {
    String directiveName = ctx.IDENTIFIER().getText();
    tokens.add(new DirectiveName(directiveName));
  }

  /**
   * Enter a parse tree produced by the {@code ColumnNameArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterColumnNameArg(DirectivesParser.ColumnNameArgContext ctx) {
    String columnName = ctx.column_name().IDENTIFIER().getText();
    tokens.add(new ColumnName(columnName));
  }

  /**
   * Enter a parse tree produced by the {@code StringArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterStringArg(DirectivesParser.StringArgContext ctx) {
    String text = ctx.string().STRING().getText();
    
    // Remove surrounding quotes
    text = text.substring(1, text.length() - 1);
    
    // Replace escaped quotes
    text = text.replace("\\\"", "\"").replace("\\'", "'");
    
    tokens.add(new TextToken(text));
  }

  /**
   * Enter a parse tree produced by the {@code BooleanArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterBooleanArg(DirectivesParser.BooleanArgContext ctx) {
    String text = ctx.boolean_().BOOLEAN().getText();
    boolean value = Boolean.parseBoolean(text);
    tokens.add(new BooleanToken(value));
  }

  /**
   * Enter a parse tree produced by the {@code PropertyArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterPropertyArg(DirectivesParser.PropertyArgContext ctx) {
    String text = ctx.property().PROPERTY().getText();
    tokens.add(new PropertiesToken(text));
  }

  /**
   * Enter a parse tree produced by the {@code IntegerLiteral} labeled alternative in {@link DirectivesParser#number}.
   *
   * @param ctx the parse tree
   */
  public void enterIntegerLiteral(DirectivesParser.IntegerLiteralContext ctx) {
    String text = ctx.DECIMAL().getText();
    int value = Integer.parseInt(text);
    tokens.add(new IntegerToken(value));
  }

  /**
   * Enter a parse tree produced by the {@code FloatingPointLiteral} labeled alternative in {@link DirectivesParser#number}.
   *
   * @param ctx the parse tree
   */
  public void enterFloatingPointLiteral(DirectivesParser.FloatingPointLiteralContext ctx) {
    String text = ctx.FLOAT().getText();
    double value = Double.parseDouble(text);
    tokens.add(new FloatToken(value));
  }

  /**
   * Enter a parse tree produced by the {@code ByteSizeArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterByteSizeArg(DirectivesParser.ByteSizeArgContext ctx) {
    String text = ctx.byte_size().BYTE_SIZE().getText();
    try {
      tokens.add(new ByteSize(text));
    } catch (SyntaxError e) {
      // This should not happen if the grammar is correct
      throw new RuntimeException("Failed to parse byte size: " + text, e);
    }
  }

  /**
   * Enter a parse tree produced by the {@code TimeDurationArg} labeled alternative in {@link DirectivesParser#argument}.
   *
   * @param ctx the parse tree
   */
  public void enterTimeDurationArg(DirectivesParser.TimeDurationArgContext ctx) {
    String text = ctx.time_duration().TIME_DURATION().getText();
    try {
      tokens.add(new TimeDuration(text));
    } catch (SyntaxError e) {
      // This should not happen if the grammar is correct
      throw new RuntimeException("Failed to parse time duration: " + text, e);
    }
  }

  // ANTLR interface methods that we don't need to implement
  @Override
  public void visitTerminal(org.antlr.v4.runtime.tree.TerminalNode node) {
  }

  @Override
  public void visitErrorNode(org.antlr.v4.runtime.tree.ErrorNode node) {
  }

  @Override
  public void enterEveryRule(org.antlr.v4.runtime.ParserRuleContext ctx) {
  }

  @Override
  public void exitEveryRule(org.antlr.v4.runtime.ParserRuleContext ctx) {
  }
}