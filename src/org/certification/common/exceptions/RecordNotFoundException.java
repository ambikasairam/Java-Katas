package org.certification.common.exceptions;

/**
 * An exception that is thrown when a record is not found in the database.
 * 
 * @author BJ Peter DeLaCruz
 */
public class RecordNotFoundException extends Exception {

  private static final long serialVersionUID = 6117383577971053393L;

  /** Creates a new RecordNotFoundException. */
  public RecordNotFoundException() {
    super("Record not found.");
  }

  /**
   * Creates a new RecordNotFoundException with the given message.
   * 
   * @param msg The message.
   */
  public RecordNotFoundException(String msg) {
    super(msg);
  }

}
