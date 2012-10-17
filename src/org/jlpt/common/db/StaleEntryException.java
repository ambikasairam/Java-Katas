package org.jlpt.common.db;

import org.jlpt.common.datamodel.JapaneseEntry;

/**
 * An exception that is thrown when a user attempts to update an entry that has already been updated
 * by another user.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class StaleEntryException extends Exception {

  /** Creates a new StaleEntryException. */
  public StaleEntryException() {
    super("The entry has already been updated by another user.");
  }

  /**
   * Creates a new StaleEntryException.
   * 
   * @param entry The entry that was updated.
   */
  public StaleEntryException(JapaneseEntry entry) {
    super("The following entry has already been updated by another user: " + entry);
  }

}
