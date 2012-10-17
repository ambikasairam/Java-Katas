package org.katas;

import java.awt.Point;
import java.util.List;

/**
 * This program determines whether a line intersects a rectangle by checking if a point on the line
 * is inside the rectangle.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Intersection extends Kata {

  /**
   * Creates a rectangle given two points: one at the top left corner and one at the bottom right
   * corner.
   * 
   * @author BJ Peter DeLaCruz
   */
  private static class Rectangle {
    private final Point topLeft;
    private final Point bottomRight;

    /**
     * Creates a new Rectangle object given the coordinates for the top left and bottom right
     * corners.
     * 
     * @param topLeft The point representing the top left corner.
     * @param bottomRight The point representing the bottom right corner.
     */
    public Rectangle(Point topLeft, Point bottomRight) {
      if (topLeft == null) {
        throw new IllegalArgumentException("topLeft is null.");
      }
      if (bottomRight == null) {
        throw new IllegalArgumentException("bottomRight is null.");
      }
      this.topLeft = topLeft;
      this.bottomRight = bottomRight;
    }

    /**
     * Gets the point at the top left corner.
     * 
     * @return The point at the top left corner.
     */
    public Point getTopLeft() {
      return this.topLeft;
    }

    /**
     * Gets the point at the bottom right corner.
     * 
     * @return The point at the bottom right corner.
     */
    public Point getBottomRight() {
      return this.bottomRight;
    }
  }

  /**
   * Creates a new Intersection object.
   */
  public Intersection() {
    // Empty constructor.
  }

  /**
   * For each line and rectangle, determines whether the line intersects the rectangle by checking
   * if a point on the line is inside the rectangle.
   */
  @Override
  public void processLines() {
    int numLines = 0;
    try {
      numLines = Integer.parseInt(this.getLines().get(0));
      this.getLines().remove(0);
    }
    catch (NumberFormatException e) {
      System.err.println("Non-numeric character(s) found: " + this.getLines().get(0));
      return;
    }

    if (numLines != this.getLines().size()) {
      System.err.print("Expected " + numLines + " line(s). ");
      System.err.println("Found " + this.getLines().size() + ".");
      return;
    }

    while (!this.getLines().isEmpty()) {
      @SuppressWarnings("unchecked")
      List<Integer> coordinates =
          (List<Integer>) KataUtils.createList(this.getLines().remove(0), " ", KataEnums.INTEGER);

      if (coordinates == null) {
        continue;
      }

      if (coordinates.size() != 8) {
        System.err.println("Expected 8 integers. Found " + coordinates.size() + ":");
        System.err.println(KataUtils.printArrayContents(coordinates));
        continue;
      }

      // Get the start and end points.
      Point start = new Point(coordinates.get(0), coordinates.get(1));
      Point end = new Point(coordinates.get(2), coordinates.get(3));
      if (start.equals(end)) {
        System.err.println("Length of line cannot be zero:");
        System.err.println("  Start: [" + start.x + ", " + start.y + "]");
        System.err.println("    End: [" + end.x + ", " + end.y + "]");
        continue;
      }

      Point slope = calculateSlope(start, end);

      // Create the rectangle.
      Point topLeft = new Point(coordinates.get(4), coordinates.get(5));
      Point bottomRight = new Point(coordinates.get(6), coordinates.get(7));
      Rectangle rectangle = new Rectangle(topLeft, bottomRight);

      // Check if the start point is inside the rectangle.
      Point p = start; // createNewPoint(start, slope);
      boolean isInside = isInsideRectangle(p, rectangle);

      // If the start point is not inside the rectangle, check every point on the line until one is
      // found inside the rectangle or until the end point is reached.
      while (!isInside && !p.equals(end)) {
        p = createNewPoint(p, slope);
        isInside = isInsideRectangle(p, rectangle);
      }

      if (isInside) {
        System.out.println("T");
      }
      else {
        System.out.println("F");
      }
    }
  }

  /**
   * Given a point on a line and the slope of the line, returns the next point on the line.
   * 
   * @param current The current point (x, y).
   * @param slope The slope of the line (run, rise).
   * @return The new point at (x + run, y + rise).
   */
  private Point createNewPoint(Point current, Point slope) {
    return new Point(current.x + slope.x, current.y + slope.y);
  }

  /**
   * Finds the greatest common divisor for two numbers, e.g. 7 is the greatest common divisor for
   * both 14 and 21.
   * 
   * @param number1 The first number.
   * @param number2 The second number.
   * @return The common divisor for both numbers.
   */
  private int getGreatestCommonDivisor(int number1, int number2) {
    int num1 = number1;
    int num2 = number2;

    if (num1 > num2) {
      int temp = num1;
      num1 = num2;
      num2 = temp;
    }
    int divisor = -1;
    for (int i = 2; i <= num1; i++) {
      if (num1 % i == 0 && num2 % i == 0) {
        divisor = i;
      }
    }
    return divisor;
  }

  /**
   * Calculates the slope of a line given the following formula:
   * <code>slope = (y1 - y2) / (x1 - x2)</code>. Returns the slope in simplified form, e.g. if the
   * slope is (21, 14), then (3, 2) is returned.
   * 
   * @param start The start point (x1, y1).
   * @param end The end point (x2, y2).
   * @return The slope stored in a <code>Point</code> ((x1 - x2), (y1 - y2)).
   */
  private Point calculateSlope(Point start, Point end) {
    if (start.equals(end)) {
      return new Point(1, 1);
    }
    int[] slope = new int[2];
    slope[0] = Math.abs(start.y - end.y);
    slope[1] = Math.abs(start.x - end.x);
    int divisor = getGreatestCommonDivisor(slope[0], slope[1]);
    if (divisor != -1) {
      slope[1] /= divisor;
      slope[0] /= divisor;
    }
    if (start.x > end.x) {
      slope[1] *= -1;
    }
    if (start.y > end.y) {
      slope[0] *= -1;
    }
    return new Point(slope[1], slope[0]);
  }

  /**
   * Checks if a point on a line is inside a rectangle.
   * 
   * @param point The point to check.
   * @param rectangle The rectangle.
   * @return True if the point is inside the rectangle, false otherwise.
   */
  private boolean isInsideRectangle(Point point, Rectangle rectangle) {
    if (point.x >= rectangle.getTopLeft().x && point.y <= rectangle.getTopLeft().y
        && point.x <= rectangle.getBottomRight().x && point.y >= rectangle.getBottomRight().y) {
      return true;
    }
    return false;
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing coordinates for lines and rectangles.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Intersection intersection = new Intersection();
    intersection.setLines(KataUtils.readLines(args[0]));

    if (intersection.getLines() != null) {
      intersection.processLines();
    }
  }

}
