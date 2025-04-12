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
 * This enum <code>TokenType</code> represents the types of tokens in the recipe.
 */
public enum TokenType {
  /**
   * Directive name token.
   */
  DIRECTIVE_NAME,
  
  /**
   * Column name token.
   */
  COLUMN_NAME,
  
  /**
   * String token.
   */
  STRING,
  
  /**
   * Text token.
   */
  TEXT,
  
  /**
   * Integer token.
   */
  INTEGER,
  
  /**
   * Float token.
   */
  FLOAT,
  
  /**
   * Boolean token.
   */
  BOOLEAN,
  
  /**
   * Properties token.
   */
  PROPERTIES,
  
  /**
   * Byte size token.
   */
  BYTE_SIZE,
  
  /**
   * Time duration token.
   */
  TIME_DURATION
}