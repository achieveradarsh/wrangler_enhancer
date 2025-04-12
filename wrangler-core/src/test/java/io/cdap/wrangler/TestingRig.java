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

package io.cdap.wrangler;

import io.cdap.wrangler.parser.ByteSizeTest;
import io.cdap.wrangler.parser.GrammarBasedParserTest;
import io.cdap.wrangler.parser.TimeDurationTest;
import io.cdap.wrangler.steps.transformation.AggregateStatsTest;

/**
 * Main test runner that executes all test cases.
 */
public class TestingRig {
  
  public static void main(String[] args) {
    System.out.println("==================================================");
    System.out.println("Starting ByteSize tests");
    System.out.println("==================================================");
    ByteSizeTest.main(args);
    
    System.out.println("\n==================================================");
    System.out.println("Starting TimeDuration tests");
    System.out.println("==================================================");
    TimeDurationTest.main(args);
    
    System.out.println("\n==================================================");
    System.out.println("Starting GrammarBasedParser tests");
    System.out.println("==================================================");
    try {
      GrammarBasedParserTest.main(args);
    } catch (Exception e) {
      System.out.println("GrammarBasedParser tests failed: " + e.getMessage());
      e.printStackTrace();
    }
    
    System.out.println("\n==================================================");
    System.out.println("Starting AggregateStats tests");
    System.out.println("==================================================");
    try {
      AggregateStatsTest.main(args);
    } catch (Exception e) {
      System.out.println("AggregateStats tests failed: " + e.getMessage());
      e.printStackTrace();
    }
    
    System.out.println("\n==================================================");
    System.out.println("All tests completed!");
    System.out.println("==================================================");
  }
}