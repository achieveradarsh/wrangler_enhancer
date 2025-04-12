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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class <code>Text</code> provides utility functions for working with text.
 */
public final class Text {
  private Text() {}

  /**
   * Splits a text into words based on word boundaries.
   *
   * @param text the text to split
   * @return an array of words
   */
  public static String[] getWords(String text) {
    List<String> words = new ArrayList<>();
    BreakIterator boundary = BreakIterator.getWordInstance();
    boundary.setText(text);
    int start = boundary.first();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      String word = text.substring(start, end).trim();
      if (!word.isEmpty()) {
        words.add(word);
      }
    }
    return words.toArray(new String[0]);
  }

  /**
   * Splits a text into sentences based on sentence boundaries.
   *
   * @param text the text to split
   * @return an array of sentences
   */
  public static String[] getSentences(String text) {
    List<String> sentences = new ArrayList<>();
    BreakIterator boundary = BreakIterator.getSentenceInstance();
    boundary.setText(text);
    int start = boundary.first();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      String sentence = text.substring(start, end).trim();
      if (!sentence.isEmpty()) {
        sentences.add(sentence);
      }
    }
    return sentences.toArray(new String[0]);
  }

  /**
   * Extracts all matches of a pattern from a text.
   *
   * @param text the text to search
   * @param regex the regular expression pattern
   * @return an array of matches
   */
  public static String[] getMatches(String text, String regex) {
    List<String> matches = new ArrayList<>();
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(text);
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches.toArray(new String[0]);
  }
}