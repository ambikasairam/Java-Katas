package org.utils;

import java.util.List;

/**
 * A utility class that contains methods for validating parameters.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public final class Validator {

  /** Don't instantiate this class. */
  private Validator() {
    // Empty constructor.
  }

  /**
   * Verifies that an object is not null; throws an <code>IllegalArgumentException</code> if the
   * object is null.
   * 
   * @param object The object that cannot be null.
   */
  public static void checkNull(Object object) {
    if (object == null) {
      throw new IllegalArgumentException("Null arguments are not allowed.");
    }
  }

  /**
   * Verifies that a list is not empty; throws an <code>IllegalArgumentException</code> if the list
   * is null or empty.
   * 
   * @param list The list that cannot be null nor empty.
   */
  public static void checkEmptyList(List<?> list) {
    if (list == null) {
      throw new IllegalArgumentException("list is null.");
    }
    if (list.isEmpty()) {
      throw new IllegalArgumentException("list is empty.");
    }
  }

  /**
   * Verifies that a string is not empty; throws an <code>IllegalArgumentException</code> if the
   * string is null or empty.
   * 
   * @param string The string that cannot be null not empty.
   */
  public static void checkEmptyString(String string) {
    if (string == null) {
      throw new IllegalArgumentException("string is null.");
    }
    if (string.isEmpty()) {
      throw new IllegalArgumentException("string is empty.");
    }
  }

  /**
   * Verifies that a number is not negative; throws an <code>IllegalArgumentException</code> if the
   * number is negative.
   * 
   * @param number The number that cannot be negative.
   */
  public static void checkNegative(long number) {
    if (number < 0) {
      throw new IllegalArgumentException("number is negative.");
    }
  }

  /**
   * Verifies that a number is not negative; throws an <code>IllegalArgumentException</code> if the
   * number is negative.
   * 
   * @param number The number that cannot be negative.
   */
  public static void checkNegative(double number) {
    if (number < 0) {
      throw new IllegalArgumentException("number is negative.");
    }
  }

  /**
   * Verifies that a number is not negative; throws an <code>IllegalArgumentException</code> if the
   * number is negative.
   * 
   * @param number The number that cannot be negative.
   */
  public static void checkNegative(byte number) {
    if (number < 0) {
      throw new IllegalArgumentException("number is negative.");
    }
  }

}
