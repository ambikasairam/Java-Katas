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
   * Adds an entry to the database.
   * 
   * @param entry The entry to add.
   */
  public void addEntry(JapaneseEntry entry);

  /**
   * Removes an entry from the database. The entry has to already exist. Otherwise, an
   * <code>EntryDoesNotExistException</code> will be thrown.
   * 
   * @param entry The entry to remove.
   */
  public void removeEntry(JapaneseEntry entry);

  /**
   * Updates an entry in the database. The entry has to already exist. Otherwise, an
   * <code>EntryDoesNotExistException</code> will be thrown.
   * 
   * @param entry The entry to update.
   */
  public void updateEntry(JapaneseEntry entry);

  /**
   * Finds one or more entries in the database using the given regular expression pattern.
   * 
   * @param regexPattern The regular expression pattern to use for the search.
   * @return A list of entries, may be empty if none are found.
   */
  public List<JapaneseEntry> find(String regexPattern);

  /**
   * A thread-safe method that will save the entries in the map to the database file.
   * 
   * @throws IOException If there are problems saving the entries to the database file.
   */
  public void save() throws IOException;

}
