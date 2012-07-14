package org.jlpt.common.db;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.FileUtils;
import org.jlpt.common.utils.ParsingUtils;
import org.jlpt.common.utils.Validator;

/**
 * Implements the DbManager interface.
 * 
 * @author BJ Peter DeLaCruz
 */
public class DbManagerImpl implements DbManager {

  private final Map<String, JapaneseEntry> entriesMap = Collections
      .synchronizedMap(new HashMap<String, JapaneseEntry>());
  private final String fileLocation;
  private final String delimiter;
  private final Object fileLock = new Object();

  /**
   * Creates a new DbManagerImpl instance. All of the lines are read in from the given database
   * file. Then the map of JLPT entries is populated using those lines.
   * 
   * @param fileLocation The location of the database file.
   * @param delimiter The delimiter used in the database file to separate parts of an entry.
   * @throws IOException If there are problems reading in from the database file.
   */
  public DbManagerImpl(String fileLocation, String delimiter) throws IOException {
    Validator.checkNotEmptyString(fileLocation);
    Validator.checkNotEmptyString(delimiter);

    this.fileLocation = fileLocation;
    this.delimiter = delimiter;

    List<String> lines = FileUtils.readFile(fileLocation);

    List<String[]> parsedLines = ParsingUtils.parseLines(lines, this.delimiter);

    for (String[] line : parsedLines) {
      if (line.length != 3) {
        // Ignore entries that do not have exactly three fields.
        // TODO: Add logging here.
        continue;
      }
      JapaneseEntry entry = new JapaneseEntry(line[0], line[1], line[2]);
      addEntry(entry);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addEntry(JapaneseEntry entry) {
    Validator.checkNull(entry);

    this.entriesMap.put(entry.getJword(), entry);
    // TODO: Add logger.
  }

  /** {@inheritDoc} */
  @Override
  public void removeEntry(JapaneseEntry entry) {
    Validator.checkNull(entry);

    JapaneseEntry oldEntry = this.entriesMap.get(entry.getJword());
    if (oldEntry == null) {
      // TODO: Add EntryDoesNotExistException.
    }
    this.entriesMap.remove(entry.getJword());
    // TODO: Add logger.
  }

  /** {@inheritDoc} */
  @Override
  public void updateEntry(JapaneseEntry entry) {
    Validator.checkNull(entry);

    JapaneseEntry oldEntry = this.entriesMap.get(entry.getJword());
    if (oldEntry == null) {
      // TODO: Add EntryDoesNotExistException.
    }
    this.entriesMap.put(entry.getJword(), entry);
    // TODO: Add logger.
  }

  /** {@inheritDoc} */
  @Override
  public List<JapaneseEntry> find(String regexPattern) {
    Validator.checkNotEmptyString(regexPattern);

    // TODO Use regular expression to find entry.
    return null;
  }

  /** {@inheritDoc} */
  @Override
  public void save() throws IOException {
    List<String> entries = new ArrayList<>();
    synchronized (this.entriesMap) {
      for (Entry<String, JapaneseEntry> entry : this.entriesMap.entrySet()) {
        entries.add(entry.getValue().getEntryAsString(this.delimiter));
      }
    }
    Collections.sort(entries);
    synchronized (this.fileLock) {
      FileUtils.writeToFile(entries, Paths.get(this.fileLocation));
    }
  }

  /** Prints the contents of the map for debugging purposes only. */
  private void printMap() {
    for (Entry<String, JapaneseEntry> entry : this.entriesMap.entrySet()) {
      System.out.println("Key: " + entry.getKey() + "\tValue: " + entry.getValue());
    }
  }

  /**
   * Tests this class.
   * 
   * @param args The file name and delimiter.
   * @throws IOException If there are problems reading from the file.
   */
  public static void main(String... args) throws IOException {
    if (args.length != 2) {
      System.err.println("Need file name and delimiter.");
      return;
    }
    new DbManagerImpl(args[0], args[1]).printMap();
  }

}
