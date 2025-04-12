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
 * This class <code>FloatToken</code> represents a floating-point token in the recipe.
 */
public class FloatToken extends Token<Double> {
  private final double value;

  /**
   * Constructor for a floating-point token.
   *
   * @param value the floating-point value
   */
  public FloatToken(double value) {
    super(Double.toString(value));
    this.value = value;
  }

  /**
   * @return the floating-point value
   */
  @Override
  public Double value() {
    return value;
  }

  /**
   * @return token type FLOAT
   */
  @Override
  public TokenType type() {
    return TokenType.FLOAT;
  }
}