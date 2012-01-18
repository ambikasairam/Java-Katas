package org.utils;

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

}
