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

import io.cdap.wrangler.api.Optional;

import java.util.HashMap;
import java.util.Map;

/**
 * This class <code>UsageDefinition</code> represents the usage definition of a directive.
 */
public class UsageDefinition {
  private final String name;
  private final Map<String, Argument> arguments;

  /**
   * Constructor for a usage definition.
   *
   * @param name the directive name
   * @param arguments the arguments
   */
  private UsageDefinition(String name, Map<String, Argument> arguments) {
    this.name = name;
    this.arguments = arguments;
  }

  /**
   * Gets the value of an argument.
   *
   * @param name the argument name
   * @return the value
   */
  public Token value(String name) {
    if (arguments.containsKey(name)) {
      return arguments.get(name).value();
    }
    return null;
  }

  /**
   * Gets the name of the directive.
   *
   * @return the directive name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the arguments.
   *
   * @return the arguments
   */
  public Map<String, Argument> getArguments() {
    return arguments;
  }

  /**
   * Creates a builder for a usage definition.
   *
   * @param name the directive name
   * @return the builder
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /**
   * Builder for a usage definition.
   */
  public static class Builder {
    private final String name;
    private final Map<String, Argument> arguments;

    /**
     * Constructor for a builder.
     *
     * @param name the directive name
     */
    public Builder(String name) {
      this.name = name;
      this.arguments = new HashMap<>();
    }

    /**
     * Defines an argument.
     *
     * @param name the argument name
     * @param type the argument type
     * @return the builder
     */
    public Builder define(String name, TokenType type) {
      arguments.put(name, new Argument(name, type, Optional.FALSE));
      return this;
    }

    /**
     * Defines an argument.
     *
     * @param name the argument name
     * @param type the argument type
     * @param optional whether the argument is optional
     * @return the builder
     */
    public Builder define(String name, TokenType type, Optional optional) {
      arguments.put(name, new Argument(name, type, optional));
      return this;
    }

    /**
     * Builds the usage definition.
     *
     * @return the usage definition
     */
    public UsageDefinition build() {
      return new UsageDefinition(name, arguments);
    }
  }

  /**
   * This class <code>Argument</code> represents an argument in a usage definition.
   */
  public static class Argument {
    private final String name;
    private final TokenType type;
    private final Optional optional;
    private Token value;

    /**
     * Constructor for an argument.
     *
     * @param name the argument name
     * @param type the argument type
     * @param optional whether the argument is optional
     */
    public Argument(String name, TokenType type, Optional optional) {
      this.name = name;
      this.type = type;
      this.optional = optional;
    }

    /**
     * Gets the name of the argument.
     *
     * @return the argument name
     */
    public String getName() {
      return name;
    }

    /**
     * Gets the type of the argument.
     *
     * @return the argument type
     */
    public TokenType getType() {
      return type;
    }

    /**
     * Gets whether the argument is optional.
     *
     * @return whether the argument is optional
     */
    public Optional getOptional() {
      return optional;
    }

    /**
     * Gets the value of the argument.
     *
     * @return the argument value
     */
    public Token value() {
      return value;
    }

    /**
     * Sets the value of the argument.
     *
     * @param value the argument value
     */
    public void setValue(Token value) {
      this.value = value;
    }
  }
}