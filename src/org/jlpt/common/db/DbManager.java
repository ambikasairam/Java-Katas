package org.jlpt.common.db;

import java.io.IOException;
import java.util.List;
import org.jlpt.common.datamodel.JapaneseEntry;

/**
 * Contains methods for operations against the database file.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface DbManager {

  /**
   * Adds an entry to the database. The entry <code>must</u> not already exist.
   * 
   * @param entry The entry to add.
   * @throws EntryAlreadyExistsException If the entry already exists in the database.
   * @throws IOException If there are problems sending the request from the client to the server.
   */
  public void addEntry(JapaneseEntry entry) throws EntryAlreadyExistsException, IOException;

  /**
   * Removes an entry from the database. The entry <u>must</u> already exist.
   * 
   * @param entry The entry to remove.
   * @throws EntryDoesNotExistException If the entry currently does not exist in the database.
   * @throws IOException If there are problems sending the request from the client to the server.
   */
  public void removeEntry(JapaneseEntry entry) throws EntryDoesNotExistException, IOException;

  /**
   * Updates an entry in the database. The entry <u>must</u> already exist.
   * 
   * @param newEntry The new entry to put in the database.
   * @param oldEntry The old entry that is currently in the database.
   * @throws EntryDoesNotExistException If the entry currently does not exist in the database.
   * @throws StaleEntryException If the entry to be updated has already been updated by another
   * user.
   * @throws IOException If there are problems sending the request from the client to the server.
   */
  public void updateEntry(JapaneseEntry newEntry, JapaneseEntry oldEntry)
      throws EntryDoesNotExistException, StaleEntryException, IOException;

  /**
   * Finds one or more entries in the database using the given regular expression pattern.
   * 
   * @param regexPattern The regular expression pattern to use for the search.
   * @return A list of entries, may be empty if none are found.
   * @throws Exception If there are problems sending the request from the client to the server, or
   * reading the results from the server; or if the regular expression pattern is invalid.
   */
  public List<JapaneseEntry> find(String regexPattern) throws Exception;

  /**
   * @return A list of all entries in the database.
   * @throws Exception If there are problems sending the request from the client to the server, or
   * reading the results from the server.
   */
  public List<JapaneseEntry> getEntries() throws Exception;

  /**
   * A thread-safe method that will save the entries in the map to the database file.
   * 
   * @return True if the database file has been saved, false otherwise.
   * @throws Exception If there are problems saving the entries to the database file.
   */
  public boolean save() throws Exception;

}
