package org.jlpt.common.db;

import org.jlpt.common.datamodel.JapaneseEntry;

/**
 * An exception that is thrown when a user tries to add an already existing entry to the database.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class EntryAlreadyExistsException extends Exception {

  /** Creates a new EntryAlreadyExistsException. */
  public EntryAlreadyExistsException() {
    super("Entry already exists in the database.");
  }

  /**
   * Creates a new EntryAlreadyExistsException.
   * @param entry The entry that already exists in the database.
   */
  public EntryAlreadyExistsException(JapaneseEntry entry) {
    super("The following entry already exists in the database: " + entry);
  }

}
