package org.katas.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.katas.PlayingCard;

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
   * Given a string, returns a list of characters in the string.
   * 
   * @param string The string from which to extract characters.
   * @return The list of characters in the string.
   */
  public static List<Character> getChars(String string) {
    List<Character> letters = new ArrayList<Character>();
    for (int index = 0; index < string.length(); index++) {
      letters.add(string.charAt(index));
    }
    return letters;
  }

  /**
   * Given a list of characters, returns a string containing all of the characters concatenated
   * together.
   * 
   * @param letters List of characters.
   * @return A string containing all of the characters concatenated together.
   */
  public static String getString(List<Character> letters) {
    StringBuffer buffer = new StringBuffer();
    for (Character c : letters) {
      buffer.append(c);
    }
    return buffer.toString();
  }

  /**
   * Creates a list of integers from 1 to N.
   * 
   * @param n The greatest integer that will be in the list.
   * @return A list of integers from 1 to N.
   */
  public static List<Integer> createIntegersList(int n) {
    List<Integer> integers = new ArrayList<Integer>();
    for (int index = 0; index < n; index++) {
      integers.add(index + 1);
    }
    return integers;
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
   * Prints the contents of a list of objects (strings, integers, etc.).
   * 
   * @param list The list of objects.
   * @return A string displaying the contents of a list.
   */
  public static String printArrayContents(List<?> list) {
    StringBuffer buffer = new StringBuffer();
    String temp = "[";
    buffer.append(temp);
    for (int index = 0; index < list.size() - 1; index++) {
      buffer.append(list.get(index));
      buffer.append(", ");
    }
    buffer.append(list.get(list.size() - 1));
    temp = "]";
    buffer.append(temp);
    return buffer.toString();
  }

  /**
   * Given a list of 2D arrays of objects, returns a string representing the contents of each 2D
   * array in a readable format.
   * 
   * @param list List of 2D arrays of objects.
   * @return A string representing the contents of each 2D array of objects.
   */
  public static String print2dArrayContents(List<?> list) {
    StringBuffer buffer = new StringBuffer();
    int numTables = 0;
    String temp = "[ ";
    buffer.append(temp);
    for (Object o : list) {
      if (o instanceof Object[][]) {
        Object[][] objects = (Object[][]) o;
        for (int index = 0; index < objects.length - 1; index++) {
          buffer.append(printArrayContents(Arrays.asList(objects[index])));
          temp = "\n  ";
          buffer.append(temp);
        }
        buffer.append(printArrayContents(Arrays.asList(objects[objects.length - 1])));
      }
      temp = " ]";
      buffer.append(temp);
      if (++numTables < list.size()) {
        temp = "\n\n[ ";
        buffer.append(temp);
      }
    }
    return buffer.toString();
  }

  /**
   * Given the name of a file, reads in all of the lines from the file.
   * 
   * @param filename Name of a file.
   * @return A list containing all of the lines that were read in.
   */
  public static List<String> readLines(String filename) {
    List<String> list = new ArrayList<String>();
    BufferedReader reader = null;
    try {
      reader =
          new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename)),
              "UTF-16"));
      String line = "";
      while ((line = reader.readLine()) != null) {
        list.add(line);
      }
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
      return null;
    }
    finally {
      try {
        if (reader != null) {
          reader.close();
        }
      }
      catch (IOException e) {
        System.err.println(e.getMessage());
        list = null;
      }
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
    StringBuffer buffer = new StringBuffer();
    for (String s : strings) {
      String temp = s + " ";
      buffer.append(temp);
    }
    return buffer.toString().substring(0, buffer.toString().length() - 1);
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
