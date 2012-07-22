package org.jlpt.common.db;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
   * This constructor uses a semicolon as the default delimiter.
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
        // Ignore entries that do not have exactly three fields.
        // TODO: Add logger.
        continue;
      }
      JapaneseEntry entry = new JapaneseEntry(line[0], line[1], line[2]);
      if (line[0].isEmpty() || line[1].isEmpty() || line[2].isEmpty()) {
        // Ignore entries that have empty fields.
        // TODO: Add logger.
        System.err.println(entry);
        continue;
      }
      if (line[0].matches(WHITESPACE_REGEX) || line[1].matches(WHITESPACE_REGEX)
          || line[2].matches(WHITESPACE_REGEX)) {
        // Ignore entries that only have whitespace characters.
        // TODO: Add logger.
        System.err.println(entry);
        continue;
      }
      try {
        addEntry(entry);
      }
      catch (EntryAlreadyExistsException e) {
        // An exception shouldn't be thrown here because the database manager is being created for
        // the first time, so don't do anything here.
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
      // TODO: Add logger.
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
      // TODO: Add logger.
    }
  }

  /** {@inheritDoc} */
  @Override
  public void updateEntry(JapaneseEntry newEntry, JapaneseEntry oldEntry)
      throws EntryDoesNotExistException, StaleEntryException {
    Validator.checkNull(newEntry);
    Validator.checkNull(oldEntry);

    synchronized (this.entriesMap) {
      JapaneseEntry entry = this.entriesMap.get(oldEntry.getJword());
      if (entry == null) {
        throw new EntryDoesNotExistException(oldEntry);
      }
      else if (!entry.equals(oldEntry)) {
        throw new StaleEntryException(oldEntry);
      }
      this.entriesMap.put(entry.getJword(), newEntry);
      // TODO: Add logger.
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

    return results;
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
