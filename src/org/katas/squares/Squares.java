package org.katas.squares;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import org.katas.common.Kata;
import org.katas.common.KataUtils;
import org.katas.squares.Line.Direction;
import com.bpd.utils.math.Point;

/**
 * This program reads in lines representing start or end points for horizontal or vertical lines and
 * counts the number of squares that the lines form.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/201_Squares.pdf">Squares</a>
 */
public class Squares extends Kata {

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    try {
      int problemNum = 1;
      while (!this.lines.isEmpty()) {

        int dimension = Integer.parseInt(this.lines.remove(0)) + 1;
        int numLines = Integer.parseInt(this.lines.remove(0));

        Set<Line> linesSet = new HashSet<Line>();
        while (!this.lines.isEmpty() && this.lines.get(0).length() > 1) {
          processLine(linesSet, dimension);
        }

        if (linesSet.size() != numLines) {
          String msg = "Expected " + numLines + " lines. Found " + linesSet.size() + ".";
          throw new IllegalArgumentException(msg);
        }

        List<Line> lines = new ArrayList<Line>(linesSet);
        Collections.sort(lines);

        System.out.println("Problem #" + problemNum++ + "\n");
        Map<Integer, ArrayList<Line>> rowsColumns = new HashMap<Integer, ArrayList<Line>>();
        organizeLines(lines, rowsColumns);
        findSquares(rowsColumns);
      }
    }
    catch (NumberFormatException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Processes a line that contains either a start or end point for a horizontal or vertical line.
   * 
   * @param lines The set of lines to which a line will be added.
   * @param dimension The number of points in a row or column in the grid.
   */
  private void processLine(Set<Line> lines, int dimension) {
    String line = this.lines.remove(0);
    StringTokenizer tokenizer = new StringTokenizer(line);
    if (tokenizer.countTokens() == 3) {
      String direction = tokenizer.nextToken();
      int i = Integer.parseInt(tokenizer.nextToken());
      int j = Integer.parseInt(tokenizer.nextToken());
      if ("H".equals(direction)) {
        Line horizLine = new Line(Direction.HORIZONTAL, i, j);
        verifyLine(horizLine, dimension);
        lines.add(horizLine);
      }
      else if ("V".equals(direction)) {
        Line vertLine = new Line(Direction.VERTICAL, j, i);
        verifyLine(vertLine, dimension);
        lines.add(vertLine);
      }
      else {
        throw new IllegalArgumentException("Illegal value for direction: " + direction);
      }
    }
    else {
      String msg = "Expected direction, row index, and column index.";
      throw new IllegalArgumentException(msg + " Found [" + line + "].");
    }
  }

  /**
   * Finds squares by following line paths from a start point on one line to an end point on a
   * different line.
   * 
   * @param rowsColumns The map containing array lists of lines. A key represents a row or column in
   * which a line is found. A value is an array list containing lines found in that row or column.
   */
  private void findSquares(Map<Integer, ArrayList<Line>> rowsColumns) {
    List<Entry<Integer, ArrayList<Line>>> rowsColumnsList =
        new ArrayList<Entry<Integer, ArrayList<Line>>>(rowsColumns.entrySet());
    Set<Line> linesSet = new LinkedHashSet<Line>();
    Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
    for (int i = 0; i < rowsColumnsList.size() - 1; i++) {
      for (int j = i + 1; j < rowsColumnsList.size(); j++) {
        for (Line line : rowsColumnsList.get(i).getValue()) {
          addHorizLineToSet(linesSet, line, rowsColumnsList.get(j).getValue());
          addVertLineToSet(linesSet, line, rowsColumnsList.get(j).getValue());
        }

        List<Line> lines = new ArrayList<Line>(linesSet);

        List<Line> horizLines = followLinePath(Direction.VERTICAL, lines);
        List<Line> vertLines = followLinePath(Direction.HORIZONTAL, lines);
        if (horizLines != null && vertLines != null
            && horizLines.get(1).getEndPoint().equals(vertLines.get(1).getEndPoint())) {
          int start = horizLines.get(0).getStartPoint().getX().intValue();
          int end = horizLines.get(1).getEndPoint().getX().intValue();
          int size = (start > end) ? start - end : end - start;
          if (!counts.containsKey(size)) {
            counts.put(size, 0);
          }
          counts.put(size, counts.get(size) + 1);
        }
        linesSet.clear();
      }
    }
    printResults(counts);
  }

  /**
   * Prints the number of squares of a particular size found.
   * 
   * @param counts The map containing the number of squares of a particular size found.
   */
  private void printResults(Map<Integer, Integer> counts) {
    if (counts.isEmpty()) {
      System.out.println("No completed squares can be found.");
    }
    else {
      for (Entry<Integer, Integer> entry : counts.entrySet()) {
        System.out.print(entry.getValue() + " square");
        System.out.print((entry.getValue() > 1) ? "s" : "");
        System.out.println(" of size " + entry.getKey());
      }
    }
    System.out.println();
    System.out.println("**********************************\n");
  }

  /**
   * Adds a horizontal line to a set of lines.
   * 
   * @param lines The set of lines to which a horizontal line will be added.
   * @param lineToAdd The horizontal line to add.
   * @param list The list of lines containing horizontal and vertical lines from which all
   * horizontal lines in a specific row will be added to the given set.
   */
  private void addHorizLineToSet(Set<Line> lines, Line lineToAdd, List<Line> list) {
    if (lineToAdd.getDirection().equals(Direction.HORIZONTAL)) {
      for (Line line : list) {
        if (line.getDirection().equals(Direction.HORIZONTAL)
            && lineToAdd.getStartPoint().getY().equals(line.getStartPoint().getY())
            && lineToAdd.getEndPoint().getY().equals(line.getEndPoint().getY())) {
          lines.add(lineToAdd);
          lines.add(line);
        }
      }
    }
  }

  /**
   * Adds a vertical line to a set of lines.
   * 
   * @param lines The set of lines to which a vertical line will be added.
   * @param lineToAdd The vertical line to add.
   * @param list The list of lines containing horizontal and vertical lines from which all vertical
   * lines in a specific column will be added to the given set.
   */
  private void addVertLineToSet(Set<Line> lines, Line lineToAdd, List<Line> list) {
    if (lineToAdd.getDirection().equals(Direction.VERTICAL)) {
      for (Line line : list) {
        if (line.getDirection().equals(Direction.VERTICAL)
            && lineToAdd.getStartPoint().getX().equals(line.getStartPoint().getX())
            && lineToAdd.getEndPoint().getX().equals(line.getEndPoint().getX())) {
          lines.add(lineToAdd);
          lines.add(line);
        }
      }
    }
  }

  /**
   * Follows a path from a start point on one line to an end point on a different line. The start
   * point should have the same x- and y-values. Otherwise, the value for <code>lastLine</code> will
   * be <code>null</code>, and the method will return. If both x- and y-values are the same, the
   * method will iterate through the given list of lines until a line with the same x- and y-values
   * for the end point is found or the end of the list is reached (in which case a square was not
   * found).
   * 
   * @param direction The direction of the line from which to start.
   * @param lines The list of lines containing both horizontal and vertical lines.
   * @return A list containing the first and last lines, or <code>null</code> if the line from which
   * to start did not have the same x- and y-values for the start point.
   */
  private List<Line> followLinePath(Direction direction, List<Line> lines) {
    Line firstLine = null, lastLine = null;
    Point<Number, Number> startPoint = null, endPoint = null;

    for (int index = 0; index < lines.size(); index++) {
      firstLine = lines.get(index);
      startPoint = firstLine.getStartPoint();
      boolean isSameValues = startPoint.getX().equals(startPoint.getY());
      if (firstLine.getDirection().equals(direction) && isSameValues) {
        lastLine = getLine(firstLine.getEndPoint(), lines);
        break;
      }
    }
    if (lastLine == null) {
      return null;
    }

    for (int index = 0; index < lines.size(); index++) {
      startPoint = lines.get(index).getStartPoint();
      endPoint = lastLine.getEndPoint();
      if (startPoint.equals(endPoint) || endPoint.getX().equals(endPoint.getY())) {
        if (endPoint.getX().equals(endPoint.getY())) {
          // System.out.println(firstLine + " * " + lastLine);
          List<Line> results = new ArrayList<Line>();
          results.add(firstLine);
          results.add(lastLine);
          return results;
        }
        lastLine = getLine(lines.get(index).getEndPoint(), lines);
        index = 0;
      }
    }
    return null;
  }

  /**
   * Returns a line that has the same x- and y-values for the start point as <code>point</code>.
   * 
   * @param point The point to check against.
   * @param lines The list of horizontal and vertical lines.
   * @return A line that has the same x- and y-values for the start point as <code>point</code>, or
   * <code>null</code> if no line is found.
   */
  private Line getLine(Point<Number, Number> point, List<Line> lines) {
    for (Line line : lines) {
      if (line.getStartPoint().equals(point)) {
        return line;
      }
    }
    return null;
  }

  /**
   * Puts all horizontal and vertical lines into a hash map where the keys represent either rows or
   * columns in which the lines in the array lists are found.
   * 
   * @param lines The list containing both horizontal and vertical lines.
   * @param rowsColumns An initially empty hash map that will contain array lists of horizontal and
   * vertical lines.
   */
  private void organizeLines(List<Line> lines, Map<Integer, ArrayList<Line>> rowsColumns) {
    // Gather all horizontal lines.
    Map<Integer, ArrayList<Line>> rows = new HashMap<Integer, ArrayList<Line>>();
    for (Line line : lines) {
      if (line.getDirection().equals(Direction.HORIZONTAL)) {
        int row = line.getStartPoint().getX().intValue();
        if (!rows.containsKey(row)) {
          rows.put(row, new ArrayList<Line>());
        }
        rows.get(row).add(line);
      }
    }

    // Gather all vertical lines.
    Map<Integer, ArrayList<Line>> columns = new HashMap<Integer, ArrayList<Line>>();
    for (Line line : lines) {
      if (line.getDirection().equals(Direction.VERTICAL)) {
        int row = line.getStartPoint().getY().intValue();
        if (!columns.containsKey(row)) {
          columns.put(row, new ArrayList<Line>());
        }
        columns.get(row).add(line);
      }
    }

    // Gather all horizontal lines in row N and all vertical lines in column N, put them together in
    // an array list, and store the array list at key N.
    for (Entry<Integer, ArrayList<Line>> entry : rows.entrySet()) {
      if (!rowsColumns.containsKey(entry.getKey())) {
        rowsColumns.put(entry.getKey(), new ArrayList<Line>());
      }
      rowsColumns.get(entry.getKey()).addAll(entry.getValue());
    }
    for (Entry<Integer, ArrayList<Line>> entry : columns.entrySet()) {
      if (!rowsColumns.containsKey(entry.getKey())) {
        rowsColumns.put(entry.getKey(), new ArrayList<Line>());
      }
      rowsColumns.get(entry.getKey()).addAll(entry.getValue());
    }
  }

  /**
   * Verifies that a line is within the grid.
   * 
   * @param line The line to verify.
   * @param dimension The number of points in a row or column in the grid.
   * @throws IllegalArgumentException If the start or end point of the line is not within the grid.
   */
  private void verifyLine(Line line, int dimension) {
    if (line.getStartPoint().getX().intValue() > dimension
        || line.getStartPoint().getY().intValue() > dimension) {
      throw new IllegalArgumentException("Point lies outside the grid: " + line.getStartPoint());
    }
    if (line.getEndPoint().getX().intValue() > dimension
        || line.getEndPoint().getY().intValue() > dimension) {
      throw new IllegalArgumentException("Point lies outside the grid: " + line.getEndPoint());
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing lines to check whether they form squares.
   */
  public static void main(String... args) {
    String filename = Squares.class.getResource("example.kata").getPath();

    Squares squares = new Squares();
    squares.setLines(KataUtils.readLines(filename));

    if (squares.getLines() != null) {
      squares.processLines();
    }
  }

}
