package org.utils;

/**
 * A data point for a JFreeChart.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * 
 * @param <X> A Number representing an X-axis value.
 * @param <Y> A Number representing a Y-axis value.
 */
public class Point<X extends Number, Y extends Number> {
  private X xValue;
  private Y yValue;

  protected static final Number NO_DATA = -1;

  /** Creates a Point object with no values. */
  public Point() {
    // Empty constructor.
  }

  /**
   * Creates a Point object that can be used to represent a data point on a JFreeChart.
   * 
   * @param xValue The X-axis value.
   * @param yValue The Y-axis value.
   */
  public Point(X xValue, Y yValue) {
    setValue(xValue, yValue);
  }

  /** @return The X-axis value. */
  public X getX() {
    return this.xValue;
  }

  /** @return The Y-axis value. */
  public Y getY() {
    return this.yValue;
  }

  /**
   * Sets the X-axis and Y-axis values in this object.
   * 
   * @param xValue The X-axis value.
   * @param yValue The Y-axis value.
   */
  public final void setValue(X xValue, Y yValue) {
    Validator.checkNull(xValue);
    Validator.checkNull(yValue);
    this.xValue = xValue;
    this.yValue = yValue;
  }
}
