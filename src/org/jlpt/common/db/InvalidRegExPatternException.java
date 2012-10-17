package org.jlpt.common.db;

/**
 * An exception that will be thrown when a user inputs an invalid regular expression pattern.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class InvalidRegExPatternException extends Exception {

  /** Creates a new InvalidRegExPatternException with a default message. */
  public InvalidRegExPatternException() {
    super("The given regular expression pattern is invalid.");
  }

  /**
   * Creates a new InvalidRegExPatternException.
   * 
   * @param msg The message for this exception.
   */
  public InvalidRegExPatternException(String msg) {
    super(msg);
  }

}
