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
import io.cdap.wrangler.api.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link ByteSize} class.
 */
public class ByteSizeTest {

  @Test
  public void testBasicUnits() throws SyntaxError {
    ByteSize bytes = new ByteSize("1024B");
    Assert.assertEquals(1024.0, bytes.value(), 0.001);
    Assert.assertEquals(TokenType.BYTE_SIZE, bytes.type());
    Assert.assertEquals("B", bytes.getUnit());
    
    ByteSize kilobytes = new ByteSize("1KB");
    Assert.assertEquals(1024.0, kilobytes.value(), 0.001);
    Assert.assertEquals(TokenType.BYTE_SIZE, kilobytes.type());
    Assert.assertEquals("KB", kilobytes.getUnit());
    
    ByteSize megabytes = new ByteSize("1MB");
    Assert.assertEquals(1024.0 * 1024.0, megabytes.value(), 0.001);
    Assert.assertEquals(TokenType.BYTE_SIZE, megabytes.type());
    Assert.assertEquals("MB", megabytes.getUnit());
    
    ByteSize gigabytes = new ByteSize("1GB");
    Assert.assertEquals(1024.0 * 1024.0 * 1024.0, gigabytes.value(), 0.001);
    Assert.assertEquals(TokenType.BYTE_SIZE, gigabytes.type());
    Assert.assertEquals("GB", gigabytes.getUnit());
  }
  
  @Test
  public void testFractionalValues() throws SyntaxError {
    ByteSize halfMegabyte = new ByteSize("0.5MB");
    Assert.assertEquals(0.5 * 1024.0 * 1024.0, halfMegabyte.value(), 0.001);
    
    ByteSize twoPointFiveGB = new ByteSize("2.5GB");
    Assert.assertEquals(2.5 * 1024.0 * 1024.0 * 1024.0, twoPointFiveGB.value(), 0.001);
  }
  
  @Test
  public void testUnitConversion() throws SyntaxError {
    ByteSize twoMegabytes = new ByteSize("2MB");
    
    // Convert to different units
    Assert.assertEquals(2.0, twoMegabytes.convertTo("MB"), 0.001);
    Assert.assertEquals(2048.0, twoMegabytes.convertTo("KB"), 0.001);
    Assert.assertEquals(2097152.0, twoMegabytes.convertTo("B"), 0.001);
    Assert.assertEquals(0.001953125, twoMegabytes.convertTo("GB"), 0.0000001);
  }
  
  @Test
  public void testCaseInsensitivity() throws SyntaxError {
    ByteSize upperCase = new ByteSize("5MB");
    ByteSize lowerCase = new ByteSize("5mb");
    ByteSize mixedCase = new ByteSize("5Mb");
    
    Assert.assertEquals(upperCase.value(), lowerCase.value(), 0.001);
    Assert.assertEquals(upperCase.value(), mixedCase.value(), 0.001);
  }
  
  @Test
  public void testWhitespaceHandling() throws SyntaxError {
    ByteSize noSpace = new ByteSize("10MB");
    ByteSize withSpace = new ByteSize("10 MB");
    
    Assert.assertEquals(noSpace.value(), withSpace.value(), 0.001);
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidFormat() throws SyntaxError {
    new ByteSize("MB10"); // Unit before number is invalid
  }
  
  @Test(expected = SyntaxError.class)
  public void testInvalidUnit() throws SyntaxError {
    new ByteSize("10ZB"); // ZB is not a supported unit
  }
  
  @Test(expected = SyntaxError.class)
  public void testMissingUnit() throws SyntaxError {
    new ByteSize("1024"); // No unit specified
  }
  
  public static void main(String[] args) {
    ByteSizeTest test = new ByteSizeTest();
    
    try {
      System.out.println("Running ByteSizeTest...");
      
      test.testBasicUnits();
      System.out.println("✓ testBasicUnits passed");
      
      test.testFractionalValues();
      System.out.println("✓ testFractionalValues passed");
      
      test.testUnitConversion();
      System.out.println("✓ testUnitConversion passed");
      
      test.testCaseInsensitivity();
      System.out.println("✓ testCaseInsensitivity passed");
      
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
      
      System.out.println("All ByteSize tests passed successfully!");
    } catch (Exception e) {
      System.err.println("Test failed with exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}