package org.katas;

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
   * @param numLetters N, the length of a string.
   * @return List of strings with length N.
   */
  public static List<String> makeList(List<Character> letters, int numLetters) {
    List<String> strings = new ArrayList<String>();
    makeList(strings, letters);
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
  private static void makeList(List<String> strings, List<Character> letters) {
    if (letters.size() == 2) {
      strings.add(letters.get(0) + "" + letters.get(1));
      strings.add(letters.get(1) + "" + letters.get(0));
    }
    else {
      Character c = letters.remove(0);
      makeList(strings, letters);
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
   * Given a line, returns a array list of characters.
   * 
   * @param line The line from which to get the characters.
   * @return An array list of characters.
   */
  public static List<Character> getLine(String line) {
    List<Character> list = new ArrayList<Character>();
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    while (tokenizer.hasMoreTokens()) {
      list.add(tokenizer.nextToken().toCharArray()[0]);
    }
    return list;
  }
}
