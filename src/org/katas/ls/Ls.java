package org.katas.ls;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.katas.common.Kata;
import com.bpd.utils.validation.Validator;

/**
 * A program that will print a list of filenames to a table in sorted alphabetical order to the
 * screen. Each row contains no more than sixty characters.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/400_Unix_Ls.pdf">Unix Ls</a>
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Ls extends Kata {

  private static final int ROW_MAX_WIDTH = 60;

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    printTables(lines);
  }

  /**
   * Prints one or more tables containing filenames to the screen.
   * 
   * @param filenames The list of filenames to process.
   */
  protected static void printTables(List<String> filenames) {
    for (int index = 0; index < filenames.size(); index++) {
      int numberOfLines = Integer.parseInt(filenames.remove(index));
      List<String> files = new ArrayList<>();
      for (int counter = 0; counter < numberOfLines; counter++) {
        files.add(filenames.remove(index));
      }
      System.out.println(getFilesTable(files));
      index--;
    }
  }

  /**
   * Sorts the given list of filenames and then returns a string with the filenames listed in
   * columns in a table. The filenames in the table are sorted in alphabetical order from left to
   * right starting from the first column in the top row.
   * 
   * @param filenames The list of filenames to put in a table.
   * @return A table with all of the filenames listed in it.
   */
  protected static String getFilesTable(List<String> filenames) {
    if (filenames == null || filenames.isEmpty()) {
      return "";
    }
    if (filenames.size() == 1) {
      return filenames.get(0);
    }

    int max = 0;
    for (String filename : filenames) {
      if (max < filename.length()) {
        max = filename.length();
      }
    }
    Collections.sort(filenames, new Comparator<String>() {

      /** {@inheritDoc} */
      @Override
      public int compare(String string1, String string2) {
        return string1.compareToIgnoreCase(string2);
      }

    });
    int numberOfColumns = getNumberOfColumns(max);
    StringBuffer table = new StringBuffer();
    for (int idx = 0; idx < filenames.size(); idx++) {
      if ((idx + 1) % numberOfColumns == 0) {
        String column = getColumn(filenames.get(idx), max) + "\n";
        table.append(column);
      }
      else {
        table.append(getColumn(filenames.get(idx), max + 2));
      }
      if ((idx + 1) % numberOfColumns != 0 && idx + 1 == filenames.size()) {
        table.append("\n");
      }
    }
    return getHyphens(60) + table.toString() + getHyphens(60);
  }

  /**
   * Returns a string containing n hyphens.
   * 
   * @param n The number of hyphens to put in the string.
   * @return A string containing n hyphens.
   */
  private static String getHyphens(int n) {
    Validator.checkNegative(n);

    StringBuffer buffer = new StringBuffer();
    for (int counter = 0; counter < n; counter++) {
      buffer.append("-");
    }
    return buffer.toString() + "\n";
  }

  /**
   * Returns the number of columns that should be printed to the screen.
   * 
   * @param maxFilenameLength The maximum length of a filename in a column.
   * @return The number of columns.
   */
  protected static int getNumberOfColumns(int maxFilenameLength) {
    Validator.checkNegative(maxFilenameLength);

    int numColumns = 0;
    while ((maxFilenameLength + 2) * numColumns + maxFilenameLength < ROW_MAX_WIDTH) {
      numColumns++;
    }
    return numColumns;
  }

  /**
   * Returns a string containing the given filename with zero, one, or more spaces appended to the
   * end of it.
   * 
   * @param filename The filename to be printed.
   * @param maxNumChars The maximum number of characters for this column. If the number of
   * characters in the filename is less than this number, spaces will be appended to end of the
   * filename until the length of the new string is equal to this number. Otherwise, the filename is
   * returned with no spaces appended.
   * @return A string containing the filename and spaces.
   */
  private static String getColumn(String filename, int maxNumChars) {
    Validator.checkNull(filename);
    Validator.checkNegative(maxNumChars);

    StringBuffer buffer = new StringBuffer(filename);
    for (int index = filename.length(); index < maxNumChars; index++) {
      buffer.append(" ");
    }
    return buffer.toString();
  }

}
