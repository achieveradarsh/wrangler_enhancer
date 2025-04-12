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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class <code>Row</code> represents a row of data.
 */
public class Row {
  private final Map<String, Object> fields;

  /**
   * Constructor for a row.
   */
  public Row() {
    this.fields = new LinkedHashMap<>();
  }

  /**
   * Constructor for a row with the given fields.
   *
   * @param fields the fields
   */
  public Row(Map<String, Object> fields) {
    this.fields = new LinkedHashMap<>(fields);
  }

  /**
   * Adds a field to the row.
   *
   * @param name the field name
   * @param value the field value
   */
  public void add(String name, Object value) {
    fields.put(name, value);
  }

  /**
   * Gets the value of a field.
   *
   * @param name the field name
   * @return the field value
   */
  public Object getValue(String name) {
    return fields.get(name);
  }

  /**
   * Checks if the row has a field with the given name.
   *
   * @param name the field name
   * @return true if the row has the field, false otherwise
   */
  public boolean has(String name) {
    return fields.containsKey(name);
  }

  /**
   * Gets the field names.
   *
   * @return the field names
   */
  public Set<String> getFields() {
    return fields.keySet();
  }

  /**
   * Gets the fields.
   *
   * @return the fields
   */
  public Map<String, Object> getValues() {
    return fields;
  }
}