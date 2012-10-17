package org.utils;

import java.util.List;

/**
 * Contains list-related utility methods.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class ListUtils {

  /** Do not instantiate this class. */
  private ListUtils() {
    // Empty constructor.
  }

  /**
   * Converts a list into a string representation separated by commas. If the list contains a
   * sequence of numbers (<code>n_1, n_2, n_3, ..., n_x</code>) and the sequence is sorted in
   * increasing order (<code>n_1 < n_2 < n_3 < ... < n_x</code>) and each number in the sequence is
   * one whole number greater than the previous number (
   * <code>n_1, n_1 + 1, n_1 + 2, ..., n_1 + x</code>), the first and last numbers in the sequence
   * will appear in the string, separated by a hyphen. For example:<br>
   * <br>
   * 
   * <table>
   * <tr>
   * <td style="text-align: right"><u>Input</u></td>
   * <td>1 3 5 6 7 9 11 13 14 15 17 19</td>
   * </tr>
   * <tr>
   * <td><u>Output</u></td>
   * <td>1, 3, 5-7, 9, 11, 13-15, 17, 19</td>
   * </tr>
   * </table>
   * 
   * @param list The list to print to a string.
   * @return The string representation of the list.
   */
  public static String listToString(List<Integer> list) {
    if (list == null || list.isEmpty()) {
      return "";
    }

    StringBuffer buffer = new StringBuffer();
    for (int start = 0, end = 1; end <= list.size(); end++) {
      int count = 0;
      while (end < list.size() && (list.get(end) - list.get(start)) == (end - start)) {
        end++;
        count++;
      }

      if (count == 0) {
        String temp = list.get(start) + ", ";
        buffer.append(temp);
      }
      else {
        String temp = list.get(start) + "-" + list.get(end - 1) + ", ";
        buffer.append(temp);
      }
      start = end;
    }

    return buffer.toString().substring(0, buffer.toString().lastIndexOf(','));
  }

}
