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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class <code>TokenGroup</code> represents a group of tokens.
 */
public class TokenGroup implements Iterable<Token> {
  private final List<Token> tokens = new ArrayList<>();

  /**
   * Adds a token to the group.
   *
   * @param token the token to add
   */
  public void add(Token token) {
    tokens.add(token);
  }

  /**
   * Gets a token at the specified index.
   *
   * @param idx the index
   * @return the token at the index
   * @throws IndexOutOfBoundsException if the index is out of range
   */
  public Token get(int idx) {
    return tokens.get(idx);
  }

  /**
   * Gets the number of tokens in the group.
   *
   * @return the number of tokens
   */
  public int size() {
    return tokens.size();
  }

  /**
   * Checks if the group is empty.
   *
   * @return true if the group is empty, false otherwise
   */
  public boolean isEmpty() {
    return tokens.isEmpty();
  }

  /**
   * @return an iterator over the tokens
   */
  @Override
  public Iterator<Token> iterator() {
    return tokens.iterator();
  }
}