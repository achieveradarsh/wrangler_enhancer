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
 * This abstract class <code>Token</code> represents a token in the recipe.
 *
 * @param <T> the type of the token value
 */
public abstract class Token<T> {
  private final String originalStr;

  /**
   * Constructor for a token.
   *
   * @param originalStr the original string representation of the token
   */
  protected Token(String originalStr) {
    this.originalStr = originalStr;
  }

  /**
   * @return the value of the token
   */
  public abstract T value();

  /**
   * @return the token type
   */
  public abstract TokenType type();

  /**
   * @return the original string representation of the token
   */
  public String raw() {
    return originalStr;
  }
}