package org.katas;

import java.io.Serializable;

/**
 * The test class used by A to test whether Java is pass-by-value or pass-by-reference.
 * 
 * @author BJ Peter DeLaCruz
 */
public class B implements Serializable {

  /** Used for object serialization. */
  private static final long serialVersionUID = 1L;
  /** Some string. */
  private String string;

  /** Empty constructor. */
  public B() {
    // Empty constructor
  }

  /**
   * Constructor with a String parameter.
   * 
   * @param string Some string.
   */
  public B(String string) {
    this.string = string;
  }

  /**
   * Sets the string variable.
   * 
   * @param string Some string.
   */
  public void setString(String string) {
    this.string = string;
  }

  /**
   * Gets the value of string.
   * 
   * @return The value of string.
   */
  public String getString() {
    return this.string;
  }
}
