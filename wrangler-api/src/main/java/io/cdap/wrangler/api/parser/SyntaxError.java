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
 * This class <code>SyntaxError</code> represents an error that occurs during parsing.
 */
public class SyntaxError extends Exception {
  /**
   * Constructor for a syntax error.
   *
   * @param message the error message
   */
  public SyntaxError(String message) {
    super(message);
  }

  /**
   * Constructor for a syntax error with a cause.
   *
   * @param message the error message
   * @param cause the cause of the error
   */
  public SyntaxError(String message, Throwable cause) {
    super(message, cause);
  }
}