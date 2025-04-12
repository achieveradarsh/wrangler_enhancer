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
 * This class <code>IntegerToken</code> represents an integer token in the recipe.
 */
public class IntegerToken extends Token<Integer> {
  private final int value;

  /**
   * Constructor for an integer token.
   *
   * @param value the integer value
   */
  public IntegerToken(int value) {
    super(Integer.toString(value));
    this.value = value;
  }

  /**
   * @return the integer value
   */
  @Override
  public Integer value() {
    return value;
  }

  /**
   * @return token type INTEGER
   */
  @Override
  public TokenType type() {
    return TokenType.INTEGER;
  }
}