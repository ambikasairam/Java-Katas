package org.katas;

import org.utils.Point;

/**
 * A class representing a line for the {@link Squares} kata.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class Line implements Comparable<Line> {

  /**
   * An enum representing a direction for a line.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
   */
  enum Direction {
    HORIZONTAL("H"), VERTICAL("V");

    private String direction;

    /**
     * Creates a new Direction enum.
     * 
     * @param direction The direction.
     */
    Direction(String direction) {
      this.direction = direction;
    }

    /** @return The direction. */
    public String getDisplayName() {
      return this.direction;
    }
  }

  private final Direction direction;
  private final Point<Number, Number> start;
  private final Point<Number, Number> end;

  /**
   * Creates a new line.
   * 
   * @param direction The direction of the line.
   * @param x The X-coordinate for a start or end point.
   * @param y The Y-coordinate for a start or end point.
   */
  public Line(Direction direction, int x, int y) {
    this.direction = direction;
    this.start = new Point<Number, Number>(x, y);
    switch (this.direction) {
    case HORIZONTAL:
      this.end = new Point<Number, Number>(x, y + 1);
      break;
    case VERTICAL:
      this.end = new Point<Number, Number>(x + 1, y);
      break;
    default:
      throw new IllegalArgumentException("Illegal value for direction: " + direction);
    }
  }

  /**
   * Creates a new line from an existing line.
   * 
   * @param line The line whose information is copied to this line.
   */
  public Line(Line line) {
    this.direction = line.direction;
    this.start = line.start;
    this.end = line.end;
  }

  /** @return The direction of the line. */
  public Direction getDirection() {
    return this.direction;
  }

  /** @return The start point. */
  public Point<Number, Number> getStartPoint() {
    return this.start;
  }

  /** @return The end point. */
  public Point<Number, Number> getEndPoint() {
    return this.end;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    String msg = "";
    switch (this.direction) {
    case HORIZONTAL:
      msg = "H";
      break;
    case VERTICAL:
      msg = "V";
      break;
    default:
      throw new IllegalArgumentException("Illegal value for direction: " + this.direction);
    }
    msg += " (" + this.start.getX() + ", " + this.start.getY() + ") ";
    return msg + "(" + this.end.getX() + ", " + this.end.getY() + ")";
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(Line line) {
    if (this.start.getX().intValue() - line.start.getX().intValue() == 0) {
      return this.start.getY().intValue() - line.start.getY().intValue();
    }
    return this.start.getX().intValue() - line.start.getX().intValue();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Line)) {
      return false;
    }
    Line line = (Line) obj;
    if (this.direction.equals(line.direction) && this.start.equals(line.start)
        && this.end.equals(line.end)) {
      return true;
    }
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int hashCode = this.direction.getDisplayName().hashCode() + this.start.hashCode();
    hashCode += this.end.hashCode() + 7;
    return hashCode;
  }
}
