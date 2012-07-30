package org.jlpt.common.db;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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

  private static final Logger LOGGER = Logger.getGlobal();

  private final Map<String, JapaneseEntry> entriesMap = Collections
      .synchronizedMap(new HashMap<String, JapaneseEntry>());
  private final String fileLocation;
  private final String delimiter;
  private final Object fileLock = new Object();
  private static final String WHITESPACE_REGEX = "^\\s*$";

  /**
   * Creates a new DbManagerImpl instance. All of the lines are read in from the given database
   * file. Then the map of JLPT entries is populated using those lines.
   * 
   * It is assumed that the database file at the given location uses a semicolon as the delimiter.
   * 
   * @param fileLocation The location of the database file.
   * @throws IOException If there are problems reading in from the database file.
   */
  public DbManagerImpl(String fileLocation) throws IOException {
    this(fileLocation, ";");
  }

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
        String msg = "Ignoring entries that do not have exactly three fields: ";
        msg += Arrays.toString(line);
        LOGGER.log(Level.INFO, msg);
        continue;
      }
      JapaneseEntry entry = new JapaneseEntry(line[0], line[1], line[2]);
      if (line[0].isEmpty() || line[1].isEmpty() || line[2].isEmpty()) {
        LOGGER.log(Level.INFO, "Ignoring entries that have empty fields: " + entry);
        continue;
      }
      if (line[0].matches(WHITESPACE_REGEX) || line[1].matches(WHITESPACE_REGEX)
          || line[2].matches(WHITESPACE_REGEX)) {
        LOGGER.log(Level.INFO, "Ignoring entries that only have whitespace characters: " + entry);
        continue;
      }
      try {
        addEntry(entry);
      }
      catch (EntryAlreadyExistsException e) {
        LOGGER.log(Level.INFO, e.getMessage());
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addEntry(JapaneseEntry entry) throws EntryAlreadyExistsException {
    Validator.checkNull(entry);

    synchronized (this.entriesMap) {
      JapaneseEntry oldEntry = this.entriesMap.get(entry.getJword());
      if (oldEntry != null && oldEntry.equals(entry)) {
        throw new EntryAlreadyExistsException(entry);
      }

      this.entriesMap.put(entry.getJword(), entry);

      // LOGGER.log(Level.INFO, "Added entry to database: " + entry + "."); 
    }
  }

  /** {@inheritDoc} */
  @Override
  public void removeEntry(JapaneseEntry entry) throws EntryDoesNotExistException {
    Validator.checkNull(entry);

    synchronized (this.entriesMap) {
      JapaneseEntry oldEntry = this.entriesMap.get(entry.getJword());
      if (oldEntry == null) {
        throw new EntryDoesNotExistException(entry);
      }
      this.entriesMap.remove(entry.getJword());

      LOGGER.log(Level.INFO, "Removed entry from database: " + entry + ".");
    }
  }

  /** {@inheritDoc} */
  @Override
  public void updateEntry(JapaneseEntry newEntry, JapaneseEntry oldEntry)
      throws EntryDoesNotExistException, StaleEntryException {
    Validator.checkNull(newEntry);
    Validator.checkNull(oldEntry);

    synchronized (this.entriesMap) {
      JapaneseEntry entry = this.entriesMap.remove(oldEntry.getJword());
      if (entry == null) {
        throw new EntryDoesNotExistException(oldEntry);
      }
      else if (!entry.equals(oldEntry)) {
        throw new StaleEntryException(oldEntry);
      }
      this.entriesMap.put(entry.getJword(), newEntry);

      String msg = "Updated entry in database. Old entry: ";
      msg += entry + ". New entry: " + newEntry + ".";
      LOGGER.log(Level.INFO, msg);
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<JapaneseEntry> find(String regexPattern) throws InvalidRegExPatternException {
    Validator.checkNull(regexPattern);

    if (regexPattern.isEmpty()) {
      throw new InvalidRegExPatternException("Empty regular expression pattern.");
    }

    Pattern pattern = null;
    try {
      pattern = Pattern.compile(regexPattern);
    }
    catch (PatternSyntaxException exception) {
      throw new InvalidRegExPatternException(exception.getMessage());
    }

    List<JapaneseEntry> results = new ArrayList<>();
    synchronized (this.entriesMap) {
      for (Entry<String, JapaneseEntry> entries : this.entriesMap.entrySet()) {
        JapaneseEntry entry = entries.getValue();
        Matcher matcher = pattern.matcher(entry.getJword());
        if (matcher.find()) {
          results.add(entry);
          continue;
        }
        matcher = pattern.matcher(entry.getReading());
        if (matcher.find()) {
          results.add(entry);
          continue;
        }
        matcher = pattern.matcher(entry.getEnglishMeaning());
        if (matcher.find()) {
          results.add(entry);
        }
      }
    }

    sortEntries(results);

    String msg = "Processed search using regular expression: " + regexPattern;
    msg += ". Found ";
    if (results.size() == 1) {
      msg += "1 entry.";
    }
    else {
      msg += results.size() + " entries.";
    }
    LOGGER.log(Level.INFO, msg);

    return results;
  }

  /** {@inheritDoc} */
  @Override
  public boolean save() {
    List<String> entries = new ArrayList<>();
    synchronized (this.entriesMap) {
      for (Entry<String, JapaneseEntry> entry : this.entriesMap.entrySet()) {
        entries.add(entry.getValue().getEntryAsString(this.delimiter));
      }
    }
    Collections.sort(entries);
    synchronized (this.fileLock) {
      try {
        FileUtils.writeToFile(entries, Paths.get(this.fileLocation));
        LOGGER.log(Level.INFO, "Successfully saved entries to database.");
        return true;
      }
      catch (IOException e) {
        LOGGER.log(Level.SEVERE, "Failed to save entries to database. Reason: " + e.getMessage());
        return false;
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public List<JapaneseEntry> getEntries() {
    List<JapaneseEntry> entries = null;
    synchronized (this.entriesMap) {
      entries = new ArrayList<>(this.entriesMap.values());
    }
    sortEntries(entries);
    return entries;
  }

  /**
   * Sorts the list of entries in ascending order by Japanese word.
   * 
   * @param entries The list of entries to sort.
   */
  private void sortEntries(List<JapaneseEntry> entries) {
    Validator.checkNull(entries);

    Collections.sort(entries, new Comparator<JapaneseEntry>() {

      @Override
      public int compare(JapaneseEntry entry1, JapaneseEntry entry2) {
        return entry1.getJword().compareTo(entry2.getJword());
      }

    });
  }

}
