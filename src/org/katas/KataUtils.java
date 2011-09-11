package org.katas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
