package org.katas.minesweeper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * A program that will display hint numbers for each grid in the Minesweeper game.
 * 
 * @see <a href=""></a>
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Minesweeper extends Kata {

  private final Map<Integer, List<MinesweeperSquare<Number, Number>>> minesweeperGrid =
      new HashMap<Integer, List<MinesweeperSquare<Number, Number>>>();
  private int numRows, numColumns;

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    int numGrids = 1;
    while (!this.getLines().isEmpty()) {
      String info = this.getLines().remove(0);
      StringTokenizer tokenizer = new StringTokenizer(info);
      if (tokenizer.countTokens() != 2) {
        String msg = "Expected 2 arguments: number of rows and number of columns.";
        throw new IllegalArgumentException(msg);
      }

      this.numRows = Integer.parseInt(tokenizer.nextToken());
      this.numColumns = Integer.parseInt(tokenizer.nextToken());
      int rowCount = 1;
      for (int index = 0; index < this.numRows && !this.getLines().isEmpty(); index++, rowCount++) {
        String line = this.getLines().remove(0);
        int columnCount = addRow(line, rowCount);
        if (columnCount != this.numColumns) {
          String msg = "Expected " + this.numColumns + " columns. ";
          msg += "Found " + columnCount + ".";
          throw new IllegalArgumentException(msg);
        }
      }
      if (--rowCount != this.numRows) {
        String msg = "Expected " + this.numRows + " rows. ";
        msg += "Found " + rowCount + ".";
        throw new IllegalArgumentException(msg);
      }

      processGrid();
      // printGrid();
      // System.out.println();
      System.out.println("Field #" + numGrids++);
      printGridHints();
      System.out.println();
      this.minesweeperGrid.clear();
    }
  }

  /**
   * Prints the grid as displayed in the input file.
   */
  @SuppressWarnings("unused")
  private void printGrid() {
    for (Entry<Integer, List<MinesweeperSquare<Number, Number>>> entry : this.minesweeperGrid
        .entrySet()) {
      for (MinesweeperSquare<Number, Number> square : entry.getValue()) {
        if (square.isMineLocatedHere()) {
          System.out.print("*");
        }
        else {
          System.out.print(".");
        }
      }
      System.out.println();
    }
  }

  /**
   * Prints the grid with hint numbers.
   */
  private void printGridHints() {
    for (Entry<Integer, List<MinesweeperSquare<Number, Number>>> entry : this.minesweeperGrid
        .entrySet()) {
      for (MinesweeperSquare<Number, Number> square : entry.getValue()) {
        if (square.isMineLocatedHere()) {
          System.out.print("*");
        }
        else {
          System.out.print(square.getNumSurroundingMines());
        }
      }
      System.out.println();
    }
  }

  /**
   * Prints information about the squares in the grid, for debugging purposes only.
   */
  @SuppressWarnings("unused")
  private void printGridInformation() {
    for (Entry<Integer, List<MinesweeperSquare<Number, Number>>> entry : this.minesweeperGrid
        .entrySet()) {
      for (MinesweeperSquare<Number, Number> square : entry.getValue()) {
        System.out.println(square);
      }
      System.out.println();
    }
  }

  /**
   * Adds the given line to the grid.
   * 
   * @param line The line that contains the row to add.
   * @param rowCount The current row number.
   * @return The number of columns.
   */
  protected int addRow(String line, int rowCount) {
    StringTokenizer tokenizer = new StringTokenizer(line);
    int counter = 1;
    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken();
      MinesweeperSquare<Number, Number> square;
      if ("*".equals(token)) {
        square = new MinesweeperSquare<Number, Number>(rowCount, counter, true);
      }
      else if (".".equals(token)) {
        square = new MinesweeperSquare<Number, Number>(rowCount, counter, false);
      }
      else {
        throw new IllegalArgumentException("Invalid character: " + token);
      }
      if (!this.minesweeperGrid.containsKey(rowCount)) {
        this.minesweeperGrid.put(rowCount, new ArrayList<MinesweeperSquare<Number, Number>>());
      }
      this.minesweeperGrid.get(rowCount).add(square);
      counter++;
    }
    return counter - 1;
  }

  /**
   * Iterates through each row and column, and processes squares that contain mines.
   */
  private void processGrid() {
    for (Entry<Integer, List<MinesweeperSquare<Number, Number>>> entry : this.minesweeperGrid
        .entrySet()) {
      for (MinesweeperSquare<Number, Number> square : entry.getValue()) {
        processMine(square);
      }
    }
  }

  /**
   * Given a square that contains a mine, updates the number of mines in the surrounding squares.
   * 
   * @param square The square that contains a mine.
   */
  protected void processMine(MinesweeperSquare<Number, Number> square) {
    if (!square.isMineLocatedHere()) {
      return;
    }

    /** Corners **/
    if (square.getX().intValue() == 1 && square.getY().intValue() == 1) {
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
      // Right-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue());
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
    }
    else if (square.getX().intValue() == 1 && square.getY().intValue() == this.numColumns) {
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 2);
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
    }
    else if (square.getX().intValue() == this.numRows
        && square.getY().intValue() == this.numColumns) {
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 2);
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
    }
    else if (square.getX().intValue() == this.numRows && square.getY().intValue() == 1) {
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
      // Right-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue());
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
    }
    /** Sides **/
    else if (square.getX().intValue() == 1) {
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 2);
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
      // Right-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue());
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
    }
    else if (square.getX().intValue() == this.numRows) {
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 2);
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
      // Right-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue());
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
    }
    else if (square.getY().intValue() == 1) {
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
      // Right-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue());
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
      // Right-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue());
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
    }
    else if (square.getY().intValue() == this.numColumns) {
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
      // Left-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 2);
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 2);
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
    }
    /** Anywhere else inside the grid. */
    else {
      // Left neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue() - 2);
      // Left-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 2);
      // Top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue() - 1);
      // Right-top neighbor
      incrementCount(square.getX().intValue() - 1, square.getY().intValue());
      // Right neighbor
      incrementCount(square.getX().intValue(), square.getY().intValue());
      // Right-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue());
      // Bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 1);
      // Left-bottom neighbor
      incrementCount(square.getX().intValue() + 1, square.getY().intValue() - 2);
    }
  }

  /**
   * Increments the number of mines that the square at (row, column) is next to.
   * 
   * @param row The row number.
   * @param column The column number.
   */
  private void incrementCount(int row, int column) {
    if (this.minesweeperGrid.containsKey(row) && column > -1
        && column < this.minesweeperGrid.get(row).size()
        && !this.minesweeperGrid.get(row).get(column).isMineLocatedHere()) {
      this.minesweeperGrid.get(row).get(column).incrementNumSurroundingMines();
    }
  }

  /**
   * Sets the number of rows, should only be used in the JUnit tests.
   * 
   * @param numRows The number of rows.
   */
  protected void setNumRows(int numRows) {
    this.numRows = numRows;
  }

  /**
   * Sets the number of columns, should only be used in the JUnit tests.
   * 
   * @param numColumns The number of columns.
   */
  protected void setNumColumns(int numColumns) {
    this.numColumns = numColumns;
  }

  /**
   * Returns the square at (row, column) in the grid, should only be used in the JUnit tests.
   * 
   * @param row The row number.
   * @param column The column number.
   * @return The square at (row, column) in the grid.
   */
  protected MinesweeperSquare<Number, Number> getSquareAt(int row, int column) {
    if (this.minesweeperGrid.containsKey(row)) {
      if (column >= 1 && column <= this.minesweeperGrid.get(row).size()) {
        return this.minesweeperGrid.get(row).get(column - 1);
      }
      else {
        return null;
      }
    }
    else {
      return null;
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing Minesweeper grids.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Minesweeper minesweeper = new Minesweeper();
    minesweeper.setLines(KataUtils.readLines(args[0]));

    if (minesweeper.getLines() != null) {
      minesweeper.processLines();
    }
  }

}
