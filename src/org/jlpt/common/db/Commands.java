package org.jlpt.common.db;

/**
 * An enum that contains commands that the client sends to the server to process.
 * 
 * @author BJ Peter DeLaCruz
 */
public enum Commands {

  /** Adds an entry to the database. */
  ADD,
  /** Removes an entry from the database. */
  REMOVE,
  /** Updates an entry in the database. */
  UPDATE,
  /** Finds an entry in the database using the given regular expression pattern. */
  FIND,
  /** Retrieves a list of entries from the database. */
  GET,
  /** Saves the entries to the database. */
  SAVE,
  /** Tells the server that a client is going to quit. */
  QUIT;

}
