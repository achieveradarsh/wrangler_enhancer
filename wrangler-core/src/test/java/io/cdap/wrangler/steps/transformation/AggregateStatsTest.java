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

package io.cdap.wrangler.steps.transformation;

import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.TextToken;
import io.cdap.wrangler.api.parser.UsageDefinition;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link AggregateStats} class.
 */
public class AggregateStatsTest {
  
  private static class MockExecutorContext implements ExecutorContext {
    @Override
    public Environment getEnvironment() {
      return new Environment() {
        @Override
        public String get(String name) {
          return null;
        }
        
        @Override
        public String get(String name, String defaultValue) {
          return defaultValue;
        }
      };
    }
  }
  
  @Test
  public void testBasicAggregation() throws DirectiveExecutionException {
    // Create usage definition
    UsageDefinition definition = UsageDefinition.builder("aggregate-stats")
      .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .build();
    
    // Set argument values
    definition.getArguments().get("size_column").setValue(new ColumnName("size"));
    definition.getArguments().get("time_column").setValue(new ColumnName("time"));
    definition.getArguments().get("size_out_col").setValue(new Text("total_size", "total_size"));
    definition.getArguments().get("time_out_col").setValue(new Text("total_time", "total_time"));
    
    // Create directive
    AggregateStats directive = new AggregateStats(definition);
    
    // Create input rows
    List<Row> rows = new ArrayList<>();
    
    Row row1 = new Row();
    row1.add("size", "10MB");
    row1.add("time", "500ms");
    rows.add(row1);
    
    Row row2 = new Row();
    row2.add("size", "5MB");
    row2.add("time", "250ms");
    rows.add(row2);
    
    // Execute directive
    List<Row> results = directive.execute(rows, new MockExecutorContext());
    
    // Check results
    Assert.assertEquals(1, results.size());
    Row result = results.get(0);
    
    // The total size should be 15MB in bytes
    Double totalSizeBytes = (Double) result.getValue("total_size");
    Assert.assertEquals(15 * 1024 * 1024.0, totalSizeBytes, 0.001);
    
    // The total time should be 750ms in nanoseconds
    Double totalTimeNanos = (Double) result.getValue("total_time");
    Assert.assertEquals(750 * 1000000.0, totalTimeNanos, 0.001);
  }
  
  @Test
  public void testAggregationWithCustomUnits() throws DirectiveExecutionException, SyntaxError {
    // Create usage definition with custom units
    UsageDefinition definition = UsageDefinition.builder("aggregate-stats")
      .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("size_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
      .define("time_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
      .build();
    
    // Set argument values
    definition.getArguments().get("size_column").setValue(new ColumnName("size"));
    definition.getArguments().get("time_column").setValue(new ColumnName("time"));
    definition.getArguments().get("size_out_col").setValue(new Text("total_size", "total_size"));
    definition.getArguments().get("time_out_col").setValue(new Text("total_time", "total_time"));
    definition.getArguments().get("size_unit").setValue(new Text("MB", "MB"));
    definition.getArguments().get("time_unit").setValue(new Text("s", "s"));
    
    // Create directive
    AggregateStats directive = new AggregateStats(definition);
    
    // Create input rows
    List<Row> rows = new ArrayList<>();
    
    Row row1 = new Row();
    row1.add("size", "1024KB");
    row1.add("time", "500ms");
    rows.add(row1);
    
    Row row2 = new Row();
    row2.add("size", "1MB");
    row2.add("time", "1.5s");
    rows.add(row2);
    
    // Execute directive
    List<Row> results = directive.execute(rows, new MockExecutorContext());
    
    // Check results
    Assert.assertEquals(1, results.size());
    Row result = results.get(0);
    
    // The total size should be 2MB in megabytes
    Double totalSizeMB = (Double) result.getValue("total_size");
    Assert.assertEquals(2.0, totalSizeMB, 0.001);
    
    // The total time should be 2s in seconds
    Double totalTimeSec = (Double) result.getValue("total_time");
    Assert.assertEquals(2.0, totalTimeSec, 0.001);
  }
  
  @Test
  public void testAverageOperation() throws DirectiveExecutionException, SyntaxError {
    // Create usage definition with average operation
    UsageDefinition definition = UsageDefinition.builder("aggregate-stats")
      .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("size_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
      .define("time_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
      .define("operation", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
      .build();
    
    // Set argument values
    definition.getArguments().get("size_column").setValue(new ColumnName("size"));
    definition.getArguments().get("time_column").setValue(new ColumnName("time"));
    definition.getArguments().get("size_out_col").setValue(new Text("avg_size", "avg_size"));
    definition.getArguments().get("time_out_col").setValue(new Text("avg_time", "avg_time"));
    definition.getArguments().get("size_unit").setValue(new Text("MB", "MB"));
    definition.getArguments().get("time_unit").setValue(new Text("ms", "ms"));
    definition.getArguments().get("operation").setValue(new Text("average", "average"));
    
    // Create directive
    AggregateStats directive = new AggregateStats(definition);
    
    // Create input rows
    List<Row> rows = new ArrayList<>();
    
    Row row1 = new Row();
    row1.add("size", "10MB");
    row1.add("time", "500ms");
    rows.add(row1);
    
    Row row2 = new Row();
    row2.add("size", "5MB");
    row2.add("time", "250ms");
    rows.add(row2);
    
    Row row3 = new Row();
    row3.add("size", "15MB");
    row3.add("time", "750ms");
    rows.add(row3);
    
    // Execute directive
    List<Row> results = directive.execute(rows, new MockExecutorContext());
    
    // Check results
    Assert.assertEquals(1, results.size());
    Row result = results.get(0);
    
    // The average size should be 10MB in megabytes
    Double avgSizeMB = (Double) result.getValue("avg_size");
    Assert.assertEquals(10.0, avgSizeMB, 0.001);
    
    // The average time should be 500ms in milliseconds
    Double avgTimeMs = (Double) result.getValue("avg_time");
    Assert.assertEquals(500.0, avgTimeMs, 0.001);
  }
  
  @Test
  public void testEmptyInput() throws DirectiveExecutionException {
    // Create usage definition
    UsageDefinition definition = UsageDefinition.builder("aggregate-stats")
      .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
      .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
      .build();
    
    // Set argument values
    definition.getArguments().get("size_column").setValue(new ColumnName("size"));
    definition.getArguments().get("time_column").setValue(new ColumnName("time"));
    definition.getArguments().get("size_out_col").setValue(new Text("total_size", "total_size"));
    definition.getArguments().get("time_out_col").setValue(new Text("total_time", "total_time"));
    
    // Create directive
    AggregateStats directive = new AggregateStats(definition);
    
    // Create empty input rows
    List<Row> rows = new ArrayList<>();
    
    // Execute directive
    List<Row> results = directive.execute(rows, new MockExecutorContext());
    
    // Check results - should return the empty input
    Assert.assertEquals(0, results.size());
  }
  
  public static void main(String[] args) {
    AggregateStatsTest test = new AggregateStatsTest();
    
    try {
      System.out.println("Running AggregateStatsTest...");
      
      test.testBasicAggregation();
      System.out.println("✓ testBasicAggregation passed");
      
      test.testAggregationWithCustomUnits();
      System.out.println("✓ testAggregationWithCustomUnits passed");
      
      test.testAverageOperation();
      System.out.println("✓ testAverageOperation passed");
      
      test.testEmptyInput();
      System.out.println("✓ testEmptyInput passed");
      
      System.out.println("\nTesting AggregateStats directive with example data:");
      
      // Create a sample set of rows with byte sizes and time durations
      List<Row> sampleRows = new ArrayList<>();
      
      Row r1 = new Row();
      r1.add("request_id", "REQ-001");
      r1.add("file_size", "2.5MB");
      r1.add("response_time", "150ms");
      sampleRows.add(r1);
      
      Row r2 = new Row();
      r2.add("request_id", "REQ-002");
      r2.add("file_size", "10MB");
      r2.add("response_time", "300ms");
      sampleRows.add(r2);
      
      Row r3 = new Row();
      r3.add("request_id", "REQ-003");
      r3.add("file_size", "1.2GB");
      r3.add("response_time", "1.5s");
      sampleRows.add(r3);
      
      System.out.println("Sample input data:");
      for (Row row : sampleRows) {
        System.out.println("  " + row.getValue("request_id") + 
                         " - Size: " + row.getValue("file_size") + 
                         ", Response time: " + row.getValue("response_time"));
      }
      
      // Create total aggregation directive
      UsageDefinition totalDef = UsageDefinition.builder("aggregate-stats")
        .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
        .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
        .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
        .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
        .define("size_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
        .define("time_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
        .build();
      
      totalDef.getArguments().get("size_column").setValue(new ColumnName("file_size"));
      totalDef.getArguments().get("time_column").setValue(new ColumnName("response_time"));
      totalDef.getArguments().get("size_out_col").setValue(new Text("total_size", "total_size"));
      totalDef.getArguments().get("time_out_col").setValue(new Text("total_time", "total_time"));
      totalDef.getArguments().get("size_unit").setValue(new Text("MB", "MB"));
      totalDef.getArguments().get("time_unit").setValue(new Text("ms", "ms"));
      
      AggregateStats totalDirective = new AggregateStats(totalDef);
      List<Row> totalResults = totalDirective.execute(sampleRows, new MockExecutorContext());
      
      System.out.println("\nTotal aggregation results:");
      Row totalResult = totalResults.get(0);
      System.out.println("  Total size (MB): " + totalResult.getValue("total_size"));
      System.out.println("  Total time (ms): " + totalResult.getValue("total_time"));
      
      // Create average aggregation directive
      UsageDefinition avgDef = UsageDefinition.builder("aggregate-stats")
        .define("size_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
        .define("time_column", io.cdap.wrangler.api.parser.TokenType.COLUMN_NAME)
        .define("size_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
        .define("time_out_col", io.cdap.wrangler.api.parser.TokenType.TEXT)
        .define("size_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
        .define("time_unit", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
        .define("operation", io.cdap.wrangler.api.parser.TokenType.TEXT, io.cdap.wrangler.api.Optional.TRUE)
        .build();
      
      avgDef.getArguments().get("size_column").setValue(new ColumnName("file_size"));
      avgDef.getArguments().get("time_column").setValue(new ColumnName("response_time"));
      avgDef.getArguments().get("size_out_col").setValue(new Text("avg_size", "avg_size"));
      avgDef.getArguments().get("time_out_col").setValue(new Text("avg_time", "avg_time"));
      avgDef.getArguments().get("size_unit").setValue(new Text("MB", "MB"));
      avgDef.getArguments().get("time_unit").setValue(new Text("ms", "ms"));
      avgDef.getArguments().get("operation").setValue(new Text("average", "average"));
      
      AggregateStats avgDirective = new AggregateStats(avgDef);
      List<Row> avgResults = avgDirective.execute(sampleRows, new MockExecutorContext());
      
      System.out.println("\nAverage aggregation results:");
      Row avgResult = avgResults.get(0);
      System.out.println("  Average size (MB): " + avgResult.getValue("avg_size"));
      System.out.println("  Average time (ms): " + avgResult.getValue("avg_time"));
      
      System.out.println("\nAll AggregateStats tests passed successfully!");
    } catch (Exception e) {
      System.err.println("Test failed with exception: " + e.getMessage());
      e.printStackTrace();
    }
  }
}