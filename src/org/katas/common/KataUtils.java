/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.katas.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.katas.PlayingCard;
import com.bpd.utils.validation.Validator;

/**
 * This class contains a set of methods that will help solving future Java katas much easier.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class KataUtils {

  /**
   * This class cannot be instantiated nor extended.
   */
  private KataUtils() {
    // Empty constructor.
  }

  /**
   * Given a list of N letters, returns a list of all possible combinations of strings with length
   * N.
   * 
   * @param letters Originally a list of N letters.
   * @return List of strings with length N.
   */
  @Deprecated
  public static List<String> makeStringsList(List<Character> letters) {
    Validator.checkNull(letters);
    if (letters.isEmpty()) {
      return new ArrayList<>();
    }

    List<String> strings = new ArrayList<String>();
    int numLetters = letters.size();
    makeStringsList(strings, letters);
    List<String> results = new ArrayList<String>();
    for (String s : strings) {
      if (s.length() == numLetters) {
        results.add(s);
      }
    }
    return results;
  }

  /**
   * A recursive function that will add all possible combinations of strings with lengths 2 to N
   * given a list of N letters to a list.
   * 
   * @param strings Originally an empty list of strings.
   * @param letters Originally a list of N letters.
   */
  @Deprecated
  private static void makeStringsList(List<String> strings, List<Character> letters) {
    Validator.checkNull(strings);
    Validator.checkNull(letters);

    if (letters.size() == 2) {
      strings.add(letters.get(0) + "" + letters.get(1));
      strings.add(letters.get(1) + "" + letters.get(0));
    }
    else {
      Character c = letters.remove(0);
      makeStringsList(strings, letters);
      List<String> tempList = new ArrayList<String>();
      for (String s : strings) {
        StringBuffer buffer = new StringBuffer(s);
        for (int index = 0; index < s.length() + 1; index++) {
          buffer.insert(index, c);
          tempList.add(buffer.toString());
          buffer = new StringBuffer(s);
        }
      }
      strings.addAll(tempList);
    }
  }

  /**
   * Given a set of N characters, generates a list of permutations of strings with length 0 to N.
   * 
   * @param chars The set of N characters.
   * @return The list of permutations of strings with length 0 to N.
   */
  public static List<String> getAllPermutationsOfSubsequences(Set<Character> chars) {
    Validator.checkNull(chars);
    if (chars.isEmpty()) {
      return new ArrayList<>();
    }

    Set<Set<Character>> powerSetOfChars = generatePowerSet(chars);

    List<String> permutations = new ArrayList<String>();

    for (Set<Character> subsequence : powerSetOfChars) {
      permute(new ArrayList<Character>(subsequence), 0, permutations);
    }

    return permutations;
  }

  /**
   * Generates a set of sets of characters (i.e. a power set); each set of characters is used by
   * {@link #allPermutationsOfSubsequences(Set)} to create the list of permutations of strings with
   * length 0 to N.
   * 
   * @param set The set used to create the power set.
   * @return The power set.
   */
  private static Set<Set<Character>> generatePowerSet(Set<Character> set) {
    Validator.checkNull(set);

    Set<Set<Character>> powerSet = new HashSet<Set<Character>>();
    if (set.isEmpty()) {
      powerSet.add(new HashSet<Character>());
      return powerSet;
    }

    Character anElement = set.iterator().next();
    set.remove(anElement);

    for (Set<Character> subset : generatePowerSet(set)) {
      Set<Character> setWithElement = new HashSet<Character>();
      setWithElement.add(anElement);
      setWithElement.addAll(subset);
      powerSet.add(setWithElement);
      powerSet.add(subset);
    }

    set.add(anElement);

    return powerSet;
  }

  /**
   * Permutes a list of characters.
   * 
   * @param characters The characters to permute.
   * @param index The starting point in the list of characters.
   * @param strings The list in which the permutations of strings are stored.
   */
  private static void permute(List<Character> characters, int index, List<String> strings) {
    Validator.checkNull(characters);
    Validator.checkNull(strings);
    if (characters.isEmpty()) {
      return;
    }

    if (index == characters.size()) {
      StringBuffer buffer = new StringBuffer();
      for (int i = 0; i < index; i++) {
        buffer.append(characters.get(i));
      }
      strings.add(buffer.toString());
    }
    else {
      for (int i = index; i < characters.size(); i++) {
        char temp = characters.get(i);
        characters.set(i, characters.get(index));
        characters.set(index, temp);
        permute(characters, index + 1, strings);
        temp = characters.get(i);
        characters.set(i, characters.get(index));
        characters.set(index, temp);
      }
    }
  }

  /**
   * Creates a list of objects of a particular data type from a string (usually, a line read in from
   * a file).
   * 
   * @param line The string containing a specific data type.
   * @param delim The delimiter, e.g. a whitespace character.
   * @param type The data type of the objects in the list.
   * @return A list of objects of a particular data type, or <code>null</code> if problems were
   * encountered parsing the string.
   */
  public static List<?> createList(String line, String delim, KataEnums type) {
    StringTokenizer tokenizer = new StringTokenizer(line, delim);
    try {
      switch (type) {
      case CHARACTER:
        List<Character> characters = new ArrayList<Character>();
        while (tokenizer.hasMoreTokens()) {
          characters.add(tokenizer.nextToken().toCharArray()[0]);
        }
        return characters;
      case DOUBLE:
        List<Double> doubles = new ArrayList<Double>();
        while (tokenizer.hasMoreTokens()) {
          doubles.add(Double.parseDouble(tokenizer.nextToken()));
        }
        return doubles;
      case INTEGER:
        List<Integer> integers = new ArrayList<Integer>();
        while (tokenizer.hasMoreTokens()) {
          integers.add(Integer.parseInt(tokenizer.nextToken()));
        }
        return integers;
      case STRING:
        List<String> strings = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
          strings.add(tokenizer.nextToken());
        }
        return strings;
      default:
        throw new IllegalArgumentException("Unsupported data type: " + type);
      }
    }
    catch (NumberFormatException e) {
      System.err.println("Non-numeric characters found on line: " + line);
    }
    return null;
  }

  /**
   * Given the name of a file, reads in all of the lines from the file.
   * 
   * @param filename Name of a file.
   * @return A list containing all of the lines that were read in.
   */
  public static List<String> readLines(String filename) {
    Validator.checkEmptyString(filename);

    List<String> list = new ArrayList<String>();
    File file = new File(filename);
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(new FileInputStream(file), "ASCII"))) {
      String line = "";
      while ((line = reader.readLine()) != null) {
        list.add(line);
      }
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      return null;
    }
    return list;
  }

  /**
   * Given a list of strings, returns a concatenated string containing all of the strings in the
   * list.
   * 
   * @param strings The list of strings.
   * @return A concatenated string containing all of the strings in the list, separated by spaces.
   */
  public static String stringsListToString(List<String> strings) {
    if (strings == null || strings.isEmpty()) {
      return "";
    }

    StringBuffer buffer = new StringBuffer();
    for (int index = 0; index < strings.size() - 1; index++) {
      String temp = strings.get(index) + " ";
      buffer.append(temp);
    }
    return buffer.toString() + strings.get(strings.size() - 1);
  }

  /**
   * Given a string, a substring to replace, and a replacement substring, returns a list of strings
   * that contains one replacement substring per string. For example,
   * <code>replaceAllOccurences("A dog is a dog", "dog", "cat")</code>, where "dog" is the substring
   * to replace and "cat" is the replacement substring, returns "A cat is a dog" and
   * "A dog is a cat".
   * 
   * @param string The string that contains the substring to replace.
   * @param oldString The substring to replace.
   * @param newString The replacement substring.
   * @return A list of strings that contains one replacement substring per string.
   */
  public static List<String> replace(String string, String oldString, String newString) {
    Validator.checkNull(string);
    Validator.checkNull(oldString);
    Validator.checkNull(newString);

    List<String> tokens = new ArrayList<String>();
    List<String> strings = new ArrayList<String>();
    StringTokenizer tokenizer = new StringTokenizer(string, " ");
    while (tokenizer.hasMoreTokens()) {
      tokens.add(tokenizer.nextToken());
    }
    for (int index = 0; index < tokens.size(); index++) {
      if (tokens.get(index).equals(oldString)) {
        tokens.set(index, newString);
        strings.add(stringsListToString(tokens));
        tokens.set(index, oldString);
      }
    }
    return strings;
  }

  /**
   * Creates a list of playing cards from lines read in from a file.
   * 
   * @param lines Lines containing a list of playing cards (e.g. 2C, KS).
   * @return A list of playing cards.
   */
  public static List<PlayingCard> createPlayingCards(List<String> lines) {
    Validator.checkNull(lines);
    if (lines.isEmpty()) {
      return new ArrayList<>();
    }

    List<PlayingCard> playingCards = new ArrayList<PlayingCard>();
    while (!lines.isEmpty()) {
      if ("#".equals(lines.get(0))) {
        break;
      }

      @SuppressWarnings("unchecked")
      List<String> tokens =
          (List<String>) KataUtils.createList(lines.remove(0), " ", KataEnums.STRING);
      for (String card : tokens) {
        playingCards.add(new PlayingCard(card, true));
      }
    }
    return playingCards;
  }

  /**
   * Given a number that represents currency, e.g. 40000, returns the same number as a string with a
   * period inserted behind the second to last digit, e.g. 400.00.
   * 
   * @param value The number to format, e.g. 40000.
   * @return The number formatted with a period behind the second to last digit, e.g. 400.00.
   */
  public static String getBalanceAsString(int value) {
    String balance = value + "";
    if (balance.length() == 2 && balance.contains("-")) {
      balance = balance.replace("-", "-00");
    }
    else if (balance.length() == 2) {
      balance = "0" + balance;
    }
    else if (balance.length() == 1) {
      balance = "00" + balance;
    }
    balance =
        balance.substring(0, balance.length() - 2) + "." + balance.substring(balance.length() - 2);
    return balance;
  }
}
