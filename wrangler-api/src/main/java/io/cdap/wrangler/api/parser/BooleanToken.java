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
 * This class <code>BooleanToken</code> represents a boolean token in the recipe.
 */
public class BooleanToken extends Token<Boolean> {
  private final boolean value;

  /**
   * Constructor for a boolean token.
   *
   * @param value the boolean value
   */
  public BooleanToken(boolean value) {
    super(Boolean.toString(value));
    this.value = value;
  }

  /**
   * @return the boolean value
   */
  @Override
  public Boolean value() {
    return value;
  }

  /**
   * @return token type BOOLEAN
   */
  @Override
  public TokenType type() {
    return TokenType.BOOLEAN;
  }
}