package org.jlpt.common.db;

/**
 * An enum that contains commands that the client sends to the server to process.
 * 
 * @author BJ Peter DeLaCruz
 */
public enum Commands {

  /** Adds an entry to the database. */
  ADD,
  /** Finds an entry in the database using the given regular expression pattern. */
  FIND,
  /** Retrieves a list of entries from the database. */
  GET,
  /** Initializes the database on the server. */
  INIT,
  /** Tells the server that a client is going to quit. */
  QUIT,
  /** Removes an entry from the database. */
  REMOVE,
  /** Saves the entries to the database. */
  SAVE,
  /** Updates an entry in the database. */
  UPDATE;

}
