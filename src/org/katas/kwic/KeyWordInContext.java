package org.katas.kwic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;

/**
 * This program sorts a list of titles using KWIC (Keywords In Context).
 * 
 * @author BJ Peter DeLaCruz
 */
public class KeyWordInContext extends Kata {

  private final List<String> ignoreWordsList;
  private final List<String> keywordsList;
  private static final String SEPARATOR = "::";

  /**
   * Creates a new Kwic object with two empty lists: one to store words that are to be ignored
   * during the sorting process, and one to store keywords that are used to sort a list of titles.
   */
  public KeyWordInContext() {
    ignoreWordsList = new ArrayList<String>();
    keywordsList = new ArrayList<String>();
  }

  /**
   * Gets the list of words that are to be ignored.
   * 
   * @return The list of words that are to be ignored.
   */
  public List<String> getIgnoreWordsList() {
    return this.ignoreWordsList;
  }

  /**
   * Gets the list of keywords that are to be used for sorting the titles.
   * 
   * @return The list of keywords that are to be used for sorting the titles.
   */
  public List<String> getKeywordsList() {
    return this.keywordsList;
  }

  /**
   * Given a list of words to ignore and a list of keywords, which are words that are found in a
   * title and are not in the former list, sorts a list of titles using the latter and prints the
   * results; a title may be printed more than once.
   */
  @Override
  public void processLines() {
    processIgnoreWords();

    if (this.getLines().isEmpty()) {
      System.err.println("At least one title must be specified.");
      return;
    }

    processKeywords();

    Collections.sort(this.keywordsList);
    Set<String> titles = new LinkedHashSet<String>(); // No repeats, keeps insertion order.

    for (String word : this.keywordsList) {
      for (String title : this.getLines()) {
        if (title.toLowerCase(Locale.US).contains(word.toLowerCase(Locale.US))) {
          titles.addAll(KataUtils.replace(title.toLowerCase(Locale.US),
              word.toLowerCase(Locale.US), word.toUpperCase(Locale.US)));
        }
      }
    }

    for (String title : titles) {
      System.out.println(title);
    }
  }

  /**
   * Processes each line until a separator is reached; each line before the separator represents a
   * word that is to be ignored when sorting the list of titles.
   */
  private void processIgnoreWords() {
    if (!this.getLines().isEmpty()) {
      String line = this.getLines().remove(0).toLowerCase();
      while (!line.equals(SEPARATOR)) {
        this.ignoreWordsList.add(line);
        line = this.getLines().remove(0).toLowerCase();
      }
    }
  }

  /**
   * Processes each line and puts all of the words from each that are not in the ignore list in the
   * list of keywords, which is used for sorting the titles.
   */
  private void processKeywords() {
    for (String line : this.getLines()) {
      @SuppressWarnings("unchecked")
      List<String> keywords = (List<String>) KataUtils.createList(line, " ", KataEnums.STRING);
      for (int index = 0; index < keywords.size(); index++) {
        keywords.set(index, keywords.get(index).toLowerCase());
      }
      keywords.removeAll(this.ignoreWordsList);
      this.keywordsList.addAll(keywords);
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing a list of words to ignore and a list of titles.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    KeyWordInContext kwic = new KeyWordInContext();
    kwic.setLines(KataUtils.readLines(args[0]));

    if (kwic.getLines() != null) {
      kwic.processLines();
    }
  }

}
