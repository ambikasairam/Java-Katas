package org.katas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

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
   * Turns a string (usually, a line from a file) into a list of strings.
   * 
   * @param string The string that needs to be broken up into a list of strings.
   * @return The list of strings.
   */
  public static List<String> stringToList(String string) {
    List<String> list = new ArrayList<String>();
    StringTokenizer tokenizer = new StringTokenizer(string, " ");
    while (tokenizer.hasMoreTokens()) {
      list.add(tokenizer.nextToken());
    }
    return list;
  }

  /**
   * Given a list of N letters, returns a list of all possible combinations of strings with length
   * N.
   * 
   * @param letters Originally a list of N letters.
   * @return List of strings with length N.
   */
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
   * Given a line with characters delimited by spaces on it, returns an array list of characters.
   * 
   * @param line The line from which to get the characters.
   * @return An array list of characters.
   */
  public static List<Character> extractChars(String line) {
    List<Character> list = new ArrayList<Character>();
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    while (tokenizer.hasMoreTokens()) {
      list.add(tokenizer.nextToken().toCharArray()[0]);
    }
    return list;
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
   * Creates a list of integers from a string (usually, a line read in from a file).
   * 
   * @param line The string containing integers.
   * @param delim The delimiter, e.g. a whitespace character.
   * @return A list of integers, or <code>null</code> if problems were encountered parsing the
   * string.
   */
  public static List<Integer> createIntegersList(String line, String delim) {
    List<Integer> integers = new ArrayList<Integer>();
    StringTokenizer tokenizer = new StringTokenizer(line, delim);
    try {
      while (tokenizer.hasMoreTokens()) {
        integers.add(Integer.parseInt(tokenizer.nextToken()));
      }
    }
    catch (NumberFormatException e) {
      System.err.println("Non-numeric characters found on line: " + line);
      return null;
    }
    return integers;
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
      reader = new BufferedReader(new FileReader(filename));
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
        reader.close();
      }
      catch (IOException e) {
        System.err.println(e.getMessage());
        list = null;
      }
    }
    return list;
  }
}
