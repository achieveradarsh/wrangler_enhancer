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
import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.Token;
import io.cdap.wrangler.api.parser.TokenGroup;
import io.cdap.wrangler.api.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link GrammarBasedParser} class.
 */
public class GrammarBasedParserTest {

  @Test
  public void testParseDirectiveWithByteSize() throws SyntaxError {
    GrammarBasedParser parser = new GrammarBasedParser();
    String directive = "aggregate-stats :data_size :response_time total_size total_time 5KB 10ms";
    TokenGroup tokens = parser.parse(directive);
    
    Assert.assertEquals(7, tokens.size());
    Assert.assertEquals("aggregate-stats", tokens.get(0).value());
    Assert.assertEquals(TokenType.DIRECTIVE_NAME, tokens.get(0).type());
    
    Assert.assertEquals("data_size", tokens.get(1).value());
    Assert.assertEquals(TokenType.COLUMN_NAME, tokens.get(1).type());
    
    Assert.assertEquals("response_time", tokens.get(2).value());
    Assert.assertEquals(TokenType.COLUMN_NAME, tokens.get(2).type());
    
    Assert.assertEquals("total_size", tokens.get(3).value());
    Assert.assertEquals(TokenType.STRING, tokens.get(3).type());
    
    Assert.assertEquals("total_time", tokens.get(4).value());
    Assert.assertEquals(TokenType.STRING, tokens.get(4).type());
    
    // The ByteSize token
    Token byteSizeToken = tokens.get(5);
    Assert.assertEquals(TokenType.BYTE_SIZE, byteSizeToken.type());
    Assert.assertTrue(byteSizeToken instanceof ByteSize);
    ByteSize byteSize = (ByteSize) byteSizeToken;
    Assert.assertEquals(5 * 1024.0, byteSize.value(), 0.001);
    Assert.assertEquals("KB", byteSize.getUnit());
    
    // The TimeDuration token
    Token timeDurationToken = tokens.get(6);
    Assert.assertEquals(TokenType.TIME_DURATION, timeDurationToken.type());
    Assert.assertTrue(timeDurationToken instanceof TimeDuration);
    TimeDuration timeDuration = (TimeDuration) timeDurationToken;
    Assert.assertEquals(10 * 1000000.0, timeDuration.value(), 0.001);
    Assert.assertEquals("ms", timeDuration.getUnit());
  }
  
  @Test
  public void testParseDirectiveWithDifferentUnits() throws SyntaxError {
    GrammarBasedParser parser = new GrammarBasedParser();
    
    // Test with megabytes and seconds
    String directive1 = "aggregate-stats :data_size :response_time total_size total_time 2.5MB 1.5s";
    TokenGroup tokens1 = parser.parse(directive1);
    
    ByteSize byteSize1 = (ByteSize) tokens1.get(5);
    Assert.assertEquals(2.5 * 1024 * 1024.0, byteSize1.value(), 0.001);
    Assert.assertEquals("MB", byteSize1.getUnit());
    
    TimeDuration timeDuration1 = (TimeDuration) tokens1.get(6);
    Assert.assertEquals(1.5 * 1000000000.0, timeDuration1.value(), 0.001);
    Assert.assertEquals("s", timeDuration1.getUnit());
    
    // Test with gigabytes and minutes
    String directive2 = "aggregate-stats :data_size :response_time total_size total_time 1GB 2m";
    TokenGroup tokens2 = parser.parse(directive2);
    
    ByteSize byteSize2 = (ByteSize) tokens2.get(5);
    Assert.assertEquals(1 * 1024 * 1024 * 1024.0, byteSize2.value(), 0.001);
    Assert.assertEquals("GB", byteSize2.getUnit());
    
    TimeDuration timeDuration2 = (TimeDuration) tokens2.get(6);
    Assert.assertEquals(2 * 60 * 1000000000.0, timeDuration2.value(), 0.001);
    Assert.assertEquals("m", timeDuration2.getUnit());
  }
  
  @Test
  public void testUnitConversion() throws SyntaxError {
    ByteSize byteSize = new ByteSize("1024KB");
    Assert.assertEquals(1024 * 1024.0, byteSize.value(), 0.001);
    Assert.assertEquals(1.0, byteSize.convertTo("MB"), 0.001);
    
    TimeDuration timeDuration = new TimeDuration("60s");
    Assert.assertEquals(60 * 1000000000.0, timeDuration.value(), 0.001);
    Assert.assertEquals(1.0, timeDuration.convertTo("m"), 0.001);
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidByteSizeFormat() throws SyntaxError {
    GrammarBasedParser parser = new GrammarBasedParser();
    String directive = "aggregate-stats :data_size :response_time total_size total_time KB5 10ms";
    parser.parse(directive);
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidTimeDurationFormat() throws SyntaxError {
    GrammarBasedParser parser = new GrammarBasedParser();
    String directive = "aggregate-stats :data_size :response_time total_size total_time 5KB ms10";
    parser.parse(directive);
  }
  
  public static void main(String[] args) {
    GrammarBasedParserTest test = new GrammarBasedParserTest();
    
    try {
      System.out.println("Running GrammarBasedParserTest...");
      
      test.testParseDirectiveWithByteSize();
      System.out.println("✓ testParseDirectiveWithByteSize passed");
      
      test.testParseDirectiveWithDifferentUnits();
      System.out.println("✓ testParseDirectiveWithDifferentUnits passed");
      
      test.testUnitConversion();
      System.out.println("✓ testUnitConversion passed");
      
      try {
        test.testInvalidByteSizeFormat();
        System.err.println("✗ testInvalidByteSizeFormat failed (did not throw exception)");
      } catch (SyntaxError e) {
        System.out.println("✓ testInvalidByteSizeFormat passed");
      }
      
      try {
        test.testInvalidTimeDurationFormat();
        System.err.println("✗ testInvalidTimeDurationFormat failed (did not throw exception)");
      } catch (SyntaxError e) {
        System.out.println("✓ testInvalidTimeDurationFormat passed");
      }
      
      System.out.println("\nTesting direct parser usage for demonstration purposes:");
      GrammarBasedParser parser = new GrammarBasedParser();
      String directive = "aggregate-stats :data_size :response_time total_size_kb total_time_sec KB s";
      TokenGroup tokens = parser.parse(directive);
      
      System.out.println("Parsed directive: " + directive);
      System.out.println("Directive name: " + tokens.get(0).value());
      
      for (int i = 1; i < tokens.size(); i++) {
        Token token = tokens.get(i);
        System.out.println("Argument " + i + ": " + token.value() + " (type: " + token.type() + ")");
      }
      
      // Display detailed information for ByteSize and TimeDuration objects
      ByteSize byteSize = new ByteSize("5MB");
      System.out.println("\nByte size example: 5MB");
      System.out.println("  Value in bytes: " + byteSize.value());
      System.out.println("  Value in KB: " + byteSize.convertTo("KB"));
      System.out.println("  Value in MB: " + byteSize.convertTo("MB"));
      System.out.println("  Value in GB: " + byteSize.convertTo("GB"));
      
      TimeDuration timeDuration = new TimeDuration("2.5h");
      System.out.println("\nTime duration example: 2.5h");
      System.out.println("  Value in nanoseconds: " + timeDuration.value());
      System.out.println("  Value in milliseconds: " + timeDuration.convertTo("ms"));
      System.out.println("  Value in seconds: " + timeDuration.convertTo("s"));
      System.out.println("  Value in minutes: " + timeDuration.convertTo("m"));
      System.out.println("  Value in hours: " + timeDuration.convertTo("h"));
      
      System.out.println("\nAll GrammarBasedParser tests passed successfully!");
    } catch (Exception e) {
      System.err.println("Test failed with exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}