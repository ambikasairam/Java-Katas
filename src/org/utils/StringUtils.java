package org.utils;

/**
 * This class contains utility methods for string manipulation.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public final class StringUtils {

  /** Do not instantiate this class. */
  private StringUtils() {
    // Empty constructor.
  }

  /**
   * Replaces the last occurrence of a substring in a string with a new substring. For example:
   * <p>
   * <code>replaceLast("hellohellohello", "ello", "i") -> "hellohellohi"</code>
   * 
   * @param string The string that contains the substring to replace.
   * @param oldSubstring The substring to replace.
   * @param newSubstring The new substring to insert in place of the old substring.
   * @return A new string with the last occurrence of the old substring replaced.
   */
  public static String replaceLast(String string, String oldSubstring, String newSubstring) {
    Validator.checkNull(string);
    Validator.checkNull(oldSubstring);
    Validator.checkNull(newSubstring);

    String reversedString = new StringBuffer(string).reverse().toString();
    String reversedOldSubstring = new StringBuffer(oldSubstring).reverse().toString();
    String reversedNewSubstring = new StringBuffer(newSubstring).reverse().toString();
    reversedString =
        org.apache.commons.lang3.StringUtils.replaceOnce(reversedString, reversedOldSubstring,
            reversedNewSubstring);
    return new StringBuffer(reversedString).reverse().toString();
  }

}
