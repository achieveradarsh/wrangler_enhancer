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

import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.DirectiveName;
import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenGroup;
import io.cdap.wrangler.grammar.DirectivesLexer;
import io.cdap.wrangler.grammar.DirectivesParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

/**
 * This class <code>GrammarBasedParser</code> parses directives using ANTLR-generated grammar.
 */
public class GrammarBasedParser {
  /**
   * Parses a directive string into tokens.
   *
   * @param directive the directive string
   * @return a group of tokens representing the directive and its arguments
   * @throws SyntaxError if the directive cannot be parsed
   */
  public TokenGroup parse(String directive) throws SyntaxError {
    try {
      // Create a lexer for the input
      ANTLRInputStream input = new ANTLRInputStream(directive);
      DirectivesLexer lexer = new DirectivesLexer(input);
      
      // Create a parser for the tokens
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      DirectivesParser parser = new DirectivesParser(tokens);
      
      // Parse the directive
      ParseTree tree = parser.parse();
      
      // Visit the parse tree to extract tokens
      DirectivesListener listener = new DirectivesListener();
      listener.visit(tree);
      
      return listener.getTokens();
    } catch (Exception e) {
      throw new SyntaxError("Failed to parse directive: " + directive, e);
    }
  }

  /**
   * Main method for testing.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    GrammarBasedParser parser = new GrammarBasedParser();
    
    try {
      // Test parsing a directive with byte size and time duration arguments
      String directive = "aggregate-stats :data_size :response_time total_size_kb total_time_sec KB s";
      TokenGroup tokens = parser.parse(directive);
      
      // Print the tokens
      System.out.println("Directive: " + directive);
      System.out.println("Parsed tokens:");
      
      Token directiveToken = tokens.get(0);
      System.out.println("  Directive name: " + directiveToken.value());
      
      for (int i = 1; i < tokens.size(); i++) {
        Token token = tokens.get(i);
        System.out.println("  Argument " + i + ": " + token.value() + " (type: " + token.type() + ")");
      }
      
      // Test parsing a byte size token
      String byteSizeStr = "5KB";
      ByteSize byteSize = new ByteSize(byteSizeStr);
      System.out.println("\nByte size: " + byteSizeStr);
      System.out.println("  Value in bytes: " + byteSize.value());
      System.out.println("  Value in KB: " + byteSize.convertTo("KB"));
      System.out.println("  Value in MB: " + byteSize.convertTo("MB"));
      
      // Test parsing a time duration token
      String timeDurationStr = "1.5s";
      TimeDuration timeDuration = new TimeDuration(timeDurationStr);
      System.out.println("\nTime duration: " + timeDurationStr);
      System.out.println("  Value in nanoseconds: " + timeDuration.value());
      System.out.println("  Value in milliseconds: " + timeDuration.convertTo("ms"));
      System.out.println("  Value in seconds: " + timeDuration.convertTo("s"));
      
      System.out.println("\nParser test completed successfully!");
    } catch (Exception e) {
      System.err.println("Parser test failed: " + e.getMessage());
      e.printStackTrace();
    }
  }
}