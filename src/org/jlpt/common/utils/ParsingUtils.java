package org.jlpt.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for parsing lines read in from a file.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class ParsingUtils {

  /** Do not instantiate this class. */
  private ParsingUtils() {
    // Emptry constructor.
  }

  /**
   * Parses a list of strings using the given delimiter and returns a list of token arrays.
   * @param lines The lines of strings to parse.
   * @param delimiter The delimiter to use for the parsing.
   * @return A list of token arrays.
   */
  public static List<String[]> parseLines(List<String> lines, String delimiter) {
    List<String[]> parsedLines = new ArrayList<>();
    for (String line : lines) {
      String[] words = line.split(delimiter);
      parsedLines.add(words);
    }
    return parsedLines;
  }

}
