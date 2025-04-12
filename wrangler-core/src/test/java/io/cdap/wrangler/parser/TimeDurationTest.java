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

import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link TimeDuration} class.
 */
public class TimeDurationTest {

  @Test
  public void testBasicUnits() throws SyntaxError {
    TimeDuration nanos = new TimeDuration("1000ns");
    Assert.assertEquals(1000.0, nanos.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, nanos.type());
    Assert.assertEquals("ns", nanos.getUnit());
    
    TimeDuration micros = new TimeDuration("1μs");
    Assert.assertEquals(1000.0, micros.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, micros.type());
    Assert.assertEquals("μs", micros.getUnit());
    
    TimeDuration millis = new TimeDuration("1ms");
    Assert.assertEquals(1000000.0, millis.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, millis.type());
    Assert.assertEquals("ms", millis.getUnit());
    
    TimeDuration seconds = new TimeDuration("1s");
    Assert.assertEquals(1000000000.0, seconds.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, seconds.type());
    Assert.assertEquals("s", seconds.getUnit());
    
    TimeDuration minutes = new TimeDuration("1m");
    Assert.assertEquals(60 * 1000000000.0, minutes.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, minutes.type());
    Assert.assertEquals("m", minutes.getUnit());
    
    TimeDuration hours = new TimeDuration("1h");
    Assert.assertEquals(60 * 60 * 1000000000.0, hours.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, hours.type());
    Assert.assertEquals("h", hours.getUnit());
    
    TimeDuration days = new TimeDuration("1d");
    Assert.assertEquals(24 * 60 * 60 * 1000000000.0, days.value(), 0.001);
    Assert.assertEquals(TokenType.TIME_DURATION, days.type());
    Assert.assertEquals("d", days.getUnit());
  }
  
  @Test
  public void testFractionalValues() throws SyntaxError {
    TimeDuration halfSecond = new TimeDuration("0.5s");
    Assert.assertEquals(0.5 * 1000000000.0, halfSecond.value(), 0.001);
    
    TimeDuration twoPointFiveMinutes = new TimeDuration("2.5m");
    Assert.assertEquals(2.5 * 60 * 1000000000.0, twoPointFiveMinutes.value(), 0.001);
  }
  
  @Test
  public void testUnitConversion() throws SyntaxError {
    TimeDuration twoSeconds = new TimeDuration("2s");
    
    // Convert to different units
    Assert.assertEquals(2.0, twoSeconds.convertTo("s"), 0.001);
    Assert.assertEquals(2000.0, twoSeconds.convertTo("ms"), 0.001);
    Assert.assertEquals(2000000.0, twoSeconds.convertTo("μs"), 0.001);
    Assert.assertEquals(2000000000.0, twoSeconds.convertTo("ns"), 0.001);
    Assert.assertEquals(0.0333333, twoSeconds.convertTo("m"), 0.0001);
    Assert.assertEquals(0.000555555, twoSeconds.convertTo("h"), 0.0000001);
  }
  
  @Test
  public void testWhitespaceHandling() throws SyntaxError {
    TimeDuration noSpace = new TimeDuration("10ms");
    TimeDuration withSpace = new TimeDuration("10 ms");
    
    Assert.assertEquals(noSpace.value(), withSpace.value(), 0.001);
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidFormat() throws SyntaxError {
    new TimeDuration("ms10"); // Unit before number is invalid
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidUnit() throws SyntaxError {
    new TimeDuration("10ys"); // ys is not a supported unit
  }
  
  @Test(expected = SyntaxError.class)
  public void testMissingUnit() throws SyntaxError {
    new TimeDuration("1000"); // No unit specified
  }
  
  public static void main(String[] args) {
    TimeDurationTest test = new TimeDurationTest();
    
    try {
      System.out.println("Running TimeDurationTest...");
      
      test.testBasicUnits();
      System.out.println("✓ testBasicUnits passed");
      
      test.testFractionalValues();
      System.out.println("✓ testFractionalValues passed");
      
      test.testUnitConversion();
      System.out.println("✓ testUnitConversion passed");
      
      test.testWhitespaceHandling();
      System.out.println("✓ testWhitespaceHandling passed");
      
      try {
        test.testInvalidFormat();
        System.err.println("✗ testInvalidFormat failed (did not throw exception)");
      } catch (SyntaxError e) {
        System.out.println("✓ testInvalidFormat passed");
      }
      
      try {
        test.testInvalidUnit();
        System.err.println("✗ testInvalidUnit failed (did not throw exception)");
      } catch (SyntaxError e) {
        System.out.println("✓ testInvalidUnit passed");
      }
      
      try {
        test.testMissingUnit();
        System.err.println("✗ testMissingUnit failed (did not throw exception)");
      } catch (SyntaxError e) {
        System.out.println("✓ testMissingUnit passed");
      }
      
      System.out.println("All TimeDuration tests passed successfully!");
    } catch (Exception e) {
      System.err.println("Test failed with exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}