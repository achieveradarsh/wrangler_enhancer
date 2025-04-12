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
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.ExecutorContext;
import io.cdap.wrangler.api.Optional;
import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.api.parser.ByteSize;
import io.cdap.wrangler.api.parser.ColumnName;
import io.cdap.wrangler.api.parser.SyntaxError;
import io.cdap.wrangler.api.parser.Text;
import io.cdap.wrangler.api.parser.TextToken;
import io.cdap.wrangler.api.parser.TimeDuration;
import io.cdap.wrangler.api.parser.TokenType;
import io.cdap.wrangler.api.parser.UsageDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * A directive for aggregating byte size and time duration values in a dataset.
 * This directive demonstrates the usage of the ByteSize and TimeDuration token types.
 */
public class AggregateStats {
  public static final String NAME = "aggregate-stats";
  
  private final String sizeColumn;
  private final String timeColumn;
  private final String sizeOutColumn;
  private final String timeOutColumn;
  private final String sizeUnit;
  private final String timeUnit;
  private final String operation;

  /**
   * Constructor for the AggregateStats directive.
   *
   * @param definition the parsed directive definition
   */
  public AggregateStats(UsageDefinition definition) {
    this.sizeColumn = ((ColumnName) definition.value("size_column")).value();
    this.timeColumn = ((ColumnName) definition.value("time_column")).value();
    this.sizeOutColumn = ((Text) definition.value("size_out_col")).value();
    this.timeOutColumn = ((Text) definition.value("time_out_col")).value();
    
    if (definition.value("size_unit") != null) {
      this.sizeUnit = ((Text) definition.value("size_unit")).value();
    } else {
      this.sizeUnit = "B"; // Default to bytes
    }
    
    if (definition.value("time_unit") != null) {
      this.timeUnit = ((Text) definition.value("time_unit")).value();
    } else {
      this.timeUnit = "ns"; // Default to nanoseconds
    }
    
    if (definition.value("operation") != null) {
      this.operation = ((Text) definition.value("operation")).value();
    } else {
      this.operation = "total"; // Default to total
    }
  }

  /**
   * Executes the directive on the input rows.
   *
   * @param rows the input rows
   * @param context the execution context
   * @return the transformed rows
   * @throws DirectiveExecutionException if an error occurs during execution
   */
  public List<Row> execute(List<Row> rows, ExecutorContext context)
    throws DirectiveExecutionException {
    
    if (rows.isEmpty()) {
      return rows;
    }
    
    try {
      // Track the sum of all values
      double totalSizeBytes = 0.0;
      double totalTimeNanos = 0.0;
      int rowCount = 0;
      
      // Process each row
      for (Row row : rows) {
        if (row.has(sizeColumn) && row.has(timeColumn)) {
          String sizeValue = row.getValue(sizeColumn).toString();
          String timeValue = row.getValue(timeColumn).toString();
          
          // Parse the size and time values
          ByteSize byteSize = new ByteSize(sizeValue);
          TimeDuration timeDuration = new TimeDuration(timeValue);
          
          // Add to totals
          totalSizeBytes += byteSize.value();
          totalTimeNanos += timeDuration.value();
          rowCount++;
        }
      }
      
      // Create a result row with aggregated values
      Row result = new Row();
      
      // Apply operation (total or average)
      double finalSizeValue;
      double finalTimeValue;
      
      if ("average".equalsIgnoreCase(operation) && rowCount > 0) {
        finalSizeValue = totalSizeBytes / rowCount;
        finalTimeValue = totalTimeNanos / rowCount;
      } else {
        finalSizeValue = totalSizeBytes;
        finalTimeValue = totalTimeNanos;
      }
      
      // Convert to requested units
      ByteSize resultSize = new ByteSize(finalSizeValue + "B");
      TimeDuration resultTime = new TimeDuration(finalTimeValue + "ns");
      
      result.add(sizeOutColumn, resultSize.convertTo(sizeUnit));
      result.add(timeOutColumn, resultTime.convertTo(timeUnit));
      
      // Return a single row with the aggregated results
      List<Row> results = new ArrayList<>();
      results.add(result);
      
      return results;
    } catch (SyntaxError e) {
      throw new DirectiveExecutionException(e.getMessage());
    }
  }

  /**
   * Defines the usage of the directive.
   *
   * @return the usage definition
   */
  public static UsageDefinition getUsage() {
    UsageDefinition.Builder builder = UsageDefinition.builder(NAME);
    builder.define("size_column", TokenType.COLUMN_NAME);
    builder.define("time_column", TokenType.COLUMN_NAME);
    builder.define("size_out_col", TokenType.TEXT);
    builder.define("time_out_col", TokenType.TEXT);
    builder.define("size_unit", TokenType.TEXT, Optional.TRUE);
    builder.define("time_unit", TokenType.TEXT, Optional.TRUE);
    builder.define("operation", TokenType.TEXT, Optional.TRUE);
    return builder.build();
  }
}