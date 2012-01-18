package org.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A utility class that can be used to linearly interpolate data.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public final class Interpolator {

  /**
   * A data point for a JFreeChart.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
   * 
   * @param <X> A Number representing an X-axis value.
   * @param <Y> A Number representing a Y-axis value.
   */
  public static class Point<X extends Number, Y extends Number> {
    private X xValue;
    private Y yValue;

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

  /** Don't instantiate this class. */
  private Interpolator() {
    // Empty constructor.
  }

  private static final Number NO_DATA = -1;

  /**
   * Interpolates a data point using the following linear interpolation formula:<br>
   * <br>
   * 
   * <code><b>y = y0 + (x - x0) ((y1 - y0) / (x1 - x0))</b></code><br>
   * <br>
   * 
   * where x = timestamp and y = unknown value at x.
   * 
   * @param leftDataPoint The left data point (x0, y0).
   * @param rightDataPoint The second data point (x1, y1).
   * @param timestamp The timestamp at which the Y-axis value is unknown.
   * @return A Point with an interpolated Y-axis value.
   */
  private static Point<Number, Number> interpolateDataPoint(Point<Number, Number> leftDataPoint,
      Point<Number, Number> rightDataPoint, Number timestamp) {
    Validator.checkNull(leftDataPoint);
    Validator.checkNull(rightDataPoint);
    Validator.checkNull(timestamp);

    long deltaX = rightDataPoint.getX().longValue() - leftDataPoint.getX().longValue();
    float deltaY = rightDataPoint.getY().floatValue() - leftDataPoint.getY().floatValue();

    long timeDifference = timestamp.longValue() - leftDataPoint.getX().longValue();

    float result = leftDataPoint.getY().floatValue() + timeDifference * (deltaY / deltaX);

    Point<Number, Number> interpolatedResult = new Point<Number, Number>();
    interpolatedResult.setValue(timestamp, result);

    return interpolatedResult;
  }

  /**
   * Creates a list of Points that includes timestamps from all lists. The resulting list will
   * contain Y-axis values that need to be interpolated.
   * 
   * @param list A list of Points.
   * @param lists A list of lists of Points, which include <code>list</code>.
   * @return A list of Points with timestamps from all lists and with Y-axis values that need to be
   * interpolated.
   */
  private static List<Point<Number, Number>> mergeLists(List<Point<Number, Number>> list,
      List<List<Point<Number, Number>>> lists) {
    Validator.checkNull(list);
    Validator.checkNull(lists);

    // Create a list that has all timestamps from all lists. Start with list.
    List<Point<Number, Number>> newList = new ArrayList<Point<Number, Number>>(list);

    // Add all timestamps from all lists except list to newList.
    for (List<Point<Number, Number>> l : lists) {
      if (!l.equals(list)) {
        for (Point<Number, Number> dataPoint : l) {
          Point<Number, Number> interpolatedDataPoint =
              new Point<Number, Number>();
          interpolatedDataPoint.setValue(dataPoint.getX(), NO_DATA);
          newList.add(interpolatedDataPoint);
        }
      }
    }

    // Sort the data points in newList in ascending order, e.g. from oldest to newest.
    Collections.sort(newList, new Comparator<Point<Number, Number>>() {

      @Override
      public int compare(Point<Number, Number> firstDataPoint,
          Point<Number, Number> secondDataPoint) {
        return (firstDataPoint.getX().longValue() < secondDataPoint.getX().longValue()) ? -1
            : (firstDataPoint.getX().longValue() == secondDataPoint.getX().longValue()) ? 0 : 1;
      }

    });

    // If two data points with the same timestamps are in newList,
    // remove the data point that has a placeholder for interpolated data,
    // i.e. only keep the data point that has real data, because that data was in
    // list and no interpolation is needed.
    for (int index = 0; index < newList.size() - 1; index++) {
      if (newList.get(index).getX().equals(newList.get(index + 1).getX())) {
        newList.remove(newList.get(index + 1));
        index--;
      }
    }

    return newList;
  }

  /**
   * If values at the beginning of a list are missing, fills them in using the first value that is
   * found; if values at the end of a list are missing, fills them in using the last value that is
   * found.
   * 
   * @param list A list of Points with values that need to be extrapolated at both ends.
   */
  private static void extrapolateEndpoints(List<Point<Number, Number>> list) {
    // Head of list
    int index = 0;
    while (list.get(index).getY().equals(NO_DATA)) {
      index++;
    }
    long value = list.get(index).getY().longValue();
    for (int idx = 0; idx < index; idx++) {
      list.get(idx).setValue(list.get(idx).getX(), value);
    }

    // Tail of list
    index = list.size() - 1;
    while (list.get(index).getY().equals(NO_DATA)) {
      index--;
    }
    value = list.get(index).getY().longValue();
    for (int idx = list.size() - 1; idx > index; idx--) {
      list.get(idx).setValue(list.get(idx).getX(), value);
    }
  }

  /**
   * Iterates through a list of Points and interpolates all missing values.
   * 
   * @param list A list of Points with values that need to be interpolated.
   */
  private static void interpolateDataPoints(List<Point<Number, Number>> list) {
    Validator.checkNull(list);

    for (int startIndex = 0, endIndex = 2; endIndex < list.size(); startIndex++, endIndex++) {
      if (list.get(endIndex).getY().equals(NO_DATA)) {
        int temp = endIndex;
        while (list.get(endIndex).getY().equals(NO_DATA)) {
          endIndex++;
        }
        list.set(temp,
            interpolateDataPoint(list.get(startIndex), list.get(endIndex), list.get(temp).getX()));
        endIndex = temp;
      }
      list.set(
          startIndex + 1,
          interpolateDataPoint(list.get(startIndex), list.get(endIndex), list.get(startIndex + 1)
              .getX()));
    }
  }

  /**
   * Returns a list containing timestamps from all lists, interpolating all missing values using
   * linear interpolation. See {@link #interpolateDataPoint(Point, Point, Number)}.
   * 
   * @param list A list of Points.
   * @param lists A list of lists of Points, which include <code>list</code>.
   * @return A list of Points with timestamps, and real and interpolated data.
   */
  public static List<Point<Number, Number>> interpolate(List<Point<Number, Number>> list,
      List<List<Point<Number, Number>>> lists) {
    List<Point<Number, Number>> pointsList = mergeLists(list, lists);
    extrapolateEndpoints(pointsList);
    interpolateDataPoints(pointsList);
    return pointsList;
  }
}
