package org.katas;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This program reads in a file containing one or more spreadsheets and outputs one or more new
 * spreadsheets after calculating all of the formulas found in them, if any.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Spreadsheet extends Kata {

  /** Number of spreadsheets in a file. */
  private int numSpreadsheets;
  /** List of all spreadsheets in a file. */
  private final List<Object[][]> spreadsheets;

  /**
   * Creates a new Spreadsheet object.
   */
  public Spreadsheet() {
    this.spreadsheets = new ArrayList<Object[][]>();
  }

  /**
   * Sets the number of spreadsheets found in a file.
   * 
   * @param numSpreadsheets The number of spreadsheets found in a file.
   */
  public void setNumSpreadsheets(int numSpreadsheets) {
    this.numSpreadsheets = numSpreadsheets;
  }

  /**
   * Returns the number of spreadsheets found in a file.
   * 
   * @return The number of spreadsheets found in a file.
   */
  public int getNumSpreadsheets() {
    return this.numSpreadsheets;
  }

  /**
   * Processes all spreadsheets found in a file and calculates all of the formulas found in them, if
   * any.
   */
  @Override
  public void processLines() {
    for (int count = 0; count < this.numSpreadsheets; count++) {
      String line = this.getLines().remove(0);
      List<Integer> dimensions = KataUtils.createIntegersList(line, " ");
      if (dimensions.size() != 2) {
        System.err.print("Only two values are expected: ");
        System.err.println("number of columns and number of rows.");
        return;
      }
      if (dimensions.get(0) < 0 && dimensions.get(1) < 0) {
        System.err.print("Negative values are not allowed: ");
        System.err.print(dimensions.get(0) + " ");
        System.err.println(dimensions.get(1));
        return;
      }

      this.spreadsheets.add(new Object[dimensions.get(1)][dimensions.get(0)]);

      populateSpreadsheet(dimensions);

      calculateFormulas();
    }

    System.out.println("Results: ");
    System.out.println(KataUtils.print2dArrayContents(this.spreadsheets));
  }

  /**
   * Populates a spreadsheet after reading in all of the lines from a file.
   * 
   * @param dimensions Dimensions of the spreadsheet (number of rows and columns).
   */
  private void populateSpreadsheet(List<Integer> dimensions) {
    for (int rowIndex = 0; rowIndex < dimensions.get(1); rowIndex++) {
      StringTokenizer tokenizer = new StringTokenizer(this.getLines().remove(0), " ");
      // Get recently added spreadsheet.
      Object[][] tempSpreadsheet = this.spreadsheets.get(this.spreadsheets.size() - 1);
      // Add values to each cell in row.
      int colIndex = 0;
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken();
        try {
          tempSpreadsheet[rowIndex][colIndex] = Integer.parseInt(token);
        }
        catch (NumberFormatException e) {
          if (token.toCharArray()[0] == '=') {
            tempSpreadsheet[rowIndex][colIndex] = token;
          }
          else {
            tempSpreadsheet[rowIndex][colIndex] = null;
          }
        }
        if (tempSpreadsheet[rowIndex][colIndex] == null) {
          throw new IllegalArgumentException("Invalid data found: " + token);
        }
        colIndex++;
      }
    }
  }

  /**
   * Calculates all of the formulas found in a spreadsheet, if any.
   */
  private void calculateFormulas() {
    int rowIndex = 0;
    int colIndex = 0;
    for (Object[] row : this.spreadsheets.get(this.spreadsheets.size() - 1)) {
      for (Object element : row) {
        if (element instanceof String) {
          String formula = (String) element;
          this.spreadsheets.get(this.spreadsheets.size() - 1)[rowIndex][colIndex] =
              this.doCalculation(formula, rowIndex, colIndex);
        }
        colIndex++;
      }
      colIndex = 0;
      rowIndex++;
    }
  }

  /**
   * Returns a value based on the formula found in the cell in the specified row and column.
   * 
   * @param formula Formula to calculate.
   * @param rowIndex Row number; used to detect cyclic dependencies.
   * @param colIndex Column number; used to detect cyclic dependencies.
   * @return Calculated value.
   */
  private int doCalculation(String formula, Integer rowIndex, Integer colIndex) {
    List<List<Integer>> coordinates = new ArrayList<List<Integer>>();
    StringTokenizer tokenizer = new StringTokenizer(formula.substring(1), "+"); // skip '='
    int sum = 0;
    while (tokenizer.hasMoreTokens()) {
      List<Integer> coords = this.getCoordinates(tokenizer.nextToken());
      if (rowIndex == coords.toArray()[0] && colIndex == coords.toArray()[1]) {
        throw new IllegalArgumentException("Cyclic dependency detected in formula: " + formula);
      }
      coordinates.add(coords);
    }
    Object[][] spreadsheet = this.spreadsheets.get(this.spreadsheets.size() - 1);
    int maxRows = spreadsheet.length;
    int maxCols = spreadsheet[0].length;
    for (List<Integer> coord : coordinates) {
      if (coord.get(0) >= maxRows || coord.get(1) >= maxCols) {
        String msg = "Invalid coordinate found: row " + (coord.get(0) + 1) + ", column ";
        msg += (coord.get(1) + 1) + " does not exist.";
        throw new IllegalArgumentException(msg);
      }
      // If a cell also contains a formula, calculate that formula first before calculating the
      // current one.
      if (spreadsheet[coord.get(0)][coord.get(1)] instanceof String) {
        String value = (String) spreadsheet[coord.get(0)][coord.get(1)];
        sum += this.doCalculation(value, coord.get(0), coord.get(1));
        continue;
      }
      sum += (Integer) spreadsheet[coord.get(0)][coord.get(1)];
    }
    return sum;
  }

  /**
   * Gets the numerical coordinates for a two-dimensional array given one or more letters (A-Z)
   * representing column numbers and one or more digits (0-9) representing row numbers.
   * 
   * @param coord The coordinate of the cell for which to get the row and column numbers.
   * @return A list containing the row and column numbers.
   */
  private List<Integer> getCoordinates(String coord) {
    List<Integer> coords = new ArrayList<Integer>();
    int rowNum = 0;
    int rowMultiplier = 0;
    String colNum = "";
    for (char c : coord.toCharArray()) {
      if (c > 64 && c < 91) {
        rowNum += (c - 65) + (rowMultiplier * 26);
        rowMultiplier += 1;
      }
      else if (c > 47 && c < 58) {
        colNum += c;
      }
      else {
        throw new IllegalArgumentException("Illegal character found: " + c);
      }
    }
    coords.add(Integer.parseInt(colNum) - 1);
    coords.add(rowNum);
    return coords;
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing one or more spreadsheets.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Spreadsheet spreadsheet = new Spreadsheet();
    spreadsheet.setLines(KataUtils.readLines(args[0]));
    String line = spreadsheet.getLines().remove(0);
    try {
      spreadsheet.setNumSpreadsheets(Integer.parseInt(line));
    }
    catch (NumberFormatException e) {
      System.err.println("Need number of spreadsheets.");
      return;
    }

    spreadsheet.processLines();
  }

}
