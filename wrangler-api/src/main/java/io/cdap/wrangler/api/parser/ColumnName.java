/*
 * Copyright © 2017-2019 Cask Data, Inc.
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

package io.cdap.wrangler.api.parser;

/**
 * This class <code>ColumnName</code> represents a column name token in the recipe.
 */
public class ColumnName extends Token<String> {
  private final String name;

  /**
   * Constructor for a column name token.
   *
   * @param name the column name
   */
  public ColumnName(String name) {
    super(":" + name);
    this.name = name;
  }

  /**
   * @return the column name
   */
  @Override
  public String value() {
    return name;
  }

  /**
   * @return token type COLUMN_NAME
   */
  @Override
  public TokenType type() {
    return TokenType.COLUMN_NAME;
  }
}