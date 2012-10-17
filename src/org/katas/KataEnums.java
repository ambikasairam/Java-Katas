package org.katas;

/**
 * This enum is used especially by the <code>KataUtils</code> class to simplify the creation of
 * methods that return different types of objects.
 * 
 * @author BJ Peter DeLaCruz
 */
public enum KataEnums {

  /** The different data types that an ADT can hold. */
  CHARACTER("Character"), DOUBLE("Double"), INTEGER("Integer"), STRING("String");

  private final String type;

  /**
   * Creates a new KataEnums object with the given value representing a data type, e.g. Integer.
   * 
   * @param type The data type.
   */
  KataEnums(String type) {
    this.type = type;
  }

  /**
   * Gets the value of this KataEnums object, which represents a data type, e.g. Integer.
   * 
   * @return The data type.
   */
  public String getDisplayName() {
    return this.type;
  }
}
