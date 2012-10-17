package org.jlpt.common.db;

import org.jlpt.common.datamodel.JapaneseEntry;

/**
 * An exception that is thrown if a user tries to remove or update a non-existing entry.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class EntryDoesNotExistException extends Exception {

  /** Creates a new EntryDoesNotExistException instance. */
  public EntryDoesNotExistException() {
    super("Entry does not exist in the database.");
  }

  /**
   * Creates a new EntryDoesNotExistException instance.
   * @param entry The entry that does not currently exist in the database.
   */
  public EntryDoesNotExistException(JapaneseEntry entry) {
    super("The following entry does not exist in the database: " + entry);
  }

}
