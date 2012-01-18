package org.utils;

import java.util.List;

/**
 * A utility class that validates objects.
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
  public static void checkIfNotEmpty(List<?> list) {
    if (list == null) {
      throw new IllegalArgumentException("list is null.");
    }
    if (list.isEmpty()) {
      throw new IllegalArgumentException("list is empty.");
    }
  }

}
