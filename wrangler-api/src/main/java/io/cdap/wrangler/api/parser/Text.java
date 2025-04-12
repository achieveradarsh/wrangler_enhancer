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
 * This class <code>Text</code> represents a text token in the recipe.
 */
public class Text extends Token<String> {
  private final String value;
  private final String original;

  /**
   * Constructor for a text token.
   *
   * @param value the text value
   * @param original the original text string
   */
  public Text(String value, String original) {
    super(original);
    this.value = value;
    this.original = original;
  }

  /**
   * @return the text value
   */
  @Override
  public String value() {
    return value;
  }

  /**
   * @return the original text string
   */
  public String original() {
    return original;
  }

  /**
   * @return token type TEXT
   */
  @Override
  public TokenType type() {
    return TokenType.TEXT;
  }
}