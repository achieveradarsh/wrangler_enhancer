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
 * This interface <code>ExecutorContext</code> provides the context for directive execution.
 */
public interface ExecutorContext {
  /**
   * Gets the environment.
   *
   * @return the environment
   */
  Environment getEnvironment();

  /**
   * This interface <code>Environment</code> provides access to the execution environment.
   */
  interface Environment {
    /**
     * Gets the value of a property.
     *
     * @param name the property name
     * @return the property value
     */
    String get(String name);

    /**
     * Gets the value of a property, or a default value if the property is not defined.
     *
     * @param name the property name
     * @param defaultValue the default value
     * @return the property value, or the default value if the property is not defined
     */
    String get(String name, String defaultValue);
  }
}