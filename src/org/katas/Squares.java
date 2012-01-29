package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.katas.Line.Direction;
import org.utils.Point;

/**
 * This program reads in lines representing start or end points for horizontal or vertical lines and
 * counts the number of squares that the lines form.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Squares extends Kata {

  private int dimension;
  private final Set<Line> lines = new HashSet<Line>();

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    try {
      while (!this.getLines().isEmpty()) {

        this.dimension = Integer.parseInt(this.getLines().remove(0));
        int numLines = Integer.parseInt(this.getLines().remove(0));

        while (!this.getLines().isEmpty() && this.getLines().get(0).length() > 1) {
          processLine();
        }

        if (this.lines.size() != numLines) {
          String msg = "Expected " + numLines + " lines.";
          throw new IllegalArgumentException(msg + " Found " + this.lines.size());
        }

        List<Line> lines = new ArrayList<Line>(this.lines);
        Collections.sort(lines);

        Map<Integer, Integer> numSquaresMap = new TreeMap<Integer, Integer>();
        List<String> coordinates = new ArrayList<String>();
        for (int index = 0; index < lines.size() - 1; index++) {
          Point<Number, Number> startPoint = lines.get(index).getStartPoint();
          if (startPoint.equals(lines.get(index + 1).getStartPoint())
              && startPoint.getX().equals(startPoint.getY())) {
            findSquare(lines, index, numSquaresMap, coordinates);
          }
        }
        printResults(numSquaresMap, coordinates);
        this.lines.clear();
      }
    }
    catch (NumberFormatException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Processes a line that contains either a start or end point for a horizontal or vertical line.
   */
  private void processLine() {
    String line = this.getLines().remove(0);
    StringTokenizer tokenizer = new StringTokenizer(line);
    if (tokenizer.countTokens() == 3) {
      String direction = tokenizer.nextToken();
      int i = Integer.parseInt(tokenizer.nextToken());
      int j = Integer.parseInt(tokenizer.nextToken());
      if ("H".equals(direction)) {
        Line horizLine = new Line(Direction.HORIZONTAL, i, j);
        verifyLine(horizLine);
        this.lines.add(horizLine);
      }
      else if ("V".equals(direction)) {
        Line vertLine = new Line(Direction.VERTICAL, i, j);
        verifyLine(vertLine);
        this.lines.add(vertLine);
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
   * Verifies that a line is within the grid.
   * 
   * @param line The line to verify.
   * @throws IllegalArgumentException If the start or end point of the line is not within the grid.
   */
  private void verifyLine(Line line) {
    if (line.getStartPoint().getX().intValue() > this.dimension
        || line.getStartPoint().getY().intValue() > this.dimension) {
      throw new IllegalArgumentException("Point lies outside the grid: " + line.getStartPoint());
    }
    if (line.getEndPoint().getX().intValue() > this.dimension
        || line.getStartPoint().getY().intValue() > this.dimension) {
      throw new IllegalArgumentException("Point lies outside the grid: " + line.getEndPoint());
    }
  }

  /**
   * Finds a square; if found, the coordinates for the square will be printed to the screen, and the
   * number of squares of that size will be incremented by one.
   * 
   * @param lines The lines to check whether they form a square.
   * @param startIndex The index of the line whose start point from which to start the search.
   * @param numSquaresMap The map containing the number of squares of a particular size found.
   * @param coordinates A list of strings representing the coordinates for the squares found.
   */
  private void findSquare(List<Line> lines, int startIndex, Map<Integer, Integer> numSquaresMap,
      List<String> coordinates) {
    for (int idx = 0; idx < lines.size() - 1; idx++) {
      List<Point<Number, Number>> horizPoints = new ArrayList<Point<Number, Number>>();
      List<Point<Number, Number>> vertPoints = new ArrayList<Point<Number, Number>>();
      Point<Number, Number> horizEndPoint, vertEndPoint;
      // Sometimes, a start point for a vertical line would appear first. Other times, a
      // start point for a horizontal line would appear first.
      if (lines.get(idx).getDirection() == Direction.HORIZONTAL) {
        horizEndPoint = findEndPoint(lines, startIndex, horizPoints);
        vertEndPoint = findEndPoint(lines, startIndex + 1, vertPoints);
      }
      else {
        vertEndPoint = findEndPoint(lines, startIndex, vertPoints);
        horizEndPoint = findEndPoint(lines, startIndex + 1, horizPoints);
      }

      if (horizEndPoint.equals(vertEndPoint)) {
        coordinates.add(processSquare(horizPoints, vertPoints, numSquaresMap));
        break;
      }
    }
  }

  /**
   * Finds all of the points from the start point at the given index to the end point, inclusive.
   * 
   * @param lines The lines whose start and end points are to be checked.
   * @param startIndex The index of the line whose start point from which to start the search.
   * @param points An initially empty list of points that will contain all of the points from the
   * start point to the end point, inclusive.
   * @return The end point.
   */
  private Point<Number, Number> findEndPoint(List<Line> lines, int startIndex,
      List<Point<Number, Number>> points) {
    Point<Number, Number> end = lines.get(startIndex).getEndPoint();
    points.add(lines.get(startIndex).getStartPoint());
    for (Line line : lines) {
      if (line.getStartPoint().equals(end)) {
        points.add(end);
        end = line.getEndPoint();
      }
    }
    points.add(end);
    return end;
  }

  /**
   * Processes a square by incrementing the number of squares of that particular size found and
   * constructing the coordinates for that square.
   * 
   * @param horizPoints The horizontal points for the square.
   * @param vertPoints The vertical points for the square.
   * @param numSquaresMap The map containing the number of squares of a particular size found.
   * @return A string representing the coordinates for a square.
   */
  private String processSquare(List<Point<Number, Number>> horizPoints,
      List<Point<Number, Number>> vertPoints, Map<Integer, Integer> numSquaresMap) {
    Point<Number, Number> lastHorizPoint = horizPoints.get(horizPoints.size() - 1);
    int size = lastHorizPoint.getX().intValue() - horizPoints.get(0).getX().intValue();
    if (numSquaresMap.containsKey(size)) {
      numSquaresMap.put(size, numSquaresMap.get(size) + 1);
    }
    else {
      numSquaresMap.put(size, 1);
    }
    StringBuffer coord = new StringBuffer();
    String space = " ";
    for (Point<Number, Number> p : horizPoints) {
      coord.append(p);
      coord.append(space);
    }
    String nl = "\n";
    coord.append(nl);
    for (Point<Number, Number> p : vertPoints) {
      coord.append(p);
      coord.append(space);
    }
    return coord.toString();
  }

  /**
   * Prints the results of processing a group of lines to the screen.
   * 
   * @param numSquaresMap The map containing the number of squares of a particular size found.
   * @param coordinates A list of strings representing the coordinates for the squares found.
   */
  private void printResults(Map<Integer, Integer> numSquaresMap, List<String> coordinates) {
    for (Entry<Integer, Integer> entry : numSquaresMap.entrySet()) {
      String msg = entry.getValue() + " ";
      if (entry.getValue() == 1) {
        msg += "square";
      }
      else {
        msg += "squares";
      }
      msg += " of size " + entry.getKey();
      System.out.println(msg);
    }
    System.out.println("\n=========================\n\nSquares found:\n");
    for (String coords : coordinates) {
      System.out.println(coords + "\n");
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing lines to check whether they form squares.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Squares squares = new Squares();
    squares.setLines(KataUtils.readLines(args[0]));

    if (squares.getLines() != null) {
      squares.processLines();
    }
  }

}
