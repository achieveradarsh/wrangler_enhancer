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

package io.cdap.wrangler.api;

/**
 * This class <code>DirectiveExecutionException</code> represents an error that occurs during directive execution.
 */
public class DirectiveExecutionException extends Exception {
  /**
   * Constructor for a directive execution exception.
   *
   * @param message the error message
   */
  public DirectiveExecutionException(String message) {
    super(message);
  }

  /**
   * Constructor for a directive execution exception with a cause.
   *
   * @param message the error message
   * @param cause the cause of the error
   */
  public DirectiveExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
}