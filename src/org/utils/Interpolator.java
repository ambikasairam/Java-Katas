package org.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class that can be used to linearly interpolate data.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public final class Interpolator {

  /** Don't instantiate this class. */
  private Interpolator() {
    // Empty constructor.
  }

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
  protected static Point<Number, Number> interpolateDataPoint(Point<Number, Number> leftDataPoint,
      Point<Number, Number> rightDataPoint, Number timestamp) {
    Validator.checkNull(leftDataPoint);
    Validator.checkNull(rightDataPoint);
    Validator.checkNull(timestamp);

    double deltaX = rightDataPoint.getX().longValue() - leftDataPoint.getX().longValue();
    double deltaY = rightDataPoint.getY().doubleValue() - leftDataPoint.getY().doubleValue();

    double timeDifference = timestamp.longValue() - leftDataPoint.getX().longValue();

    double result = leftDataPoint.getY().doubleValue() + (timeDifference * (deltaY / deltaX));

    Point<Number, Number> interpolatedResult = new Point<Number, Number>();
    interpolatedResult.setValue(timestamp, result);

    return interpolatedResult;
  }

  /**
   * Adds all of the data points in all of the lists to a new list. The new list will be returned
   * and will contain Y-axis values that need to be interpolated.
   * 
   * @param list A list of data points.
   * @param lists A list of lists of data points, which include <code>list</code>.
   * @return A list of data points whose Y-axis values need to be interpolated.
   */
  protected static List<Point<Number, Number>> mergeLists(List<Point<Number, Number>> list,
      List<List<Point<Number, Number>>> lists) {
    Validator.checkNull(list);
    Validator.checkNull(lists);

    // Create a list that has all timestamps from all lists. Start with list.
    List<Point<Number, Number>> newList = new ArrayList<Point<Number, Number>>(list);

    // Add all timestamps from all lists except list to newList.
    for (List<Point<Number, Number>> l : lists) {
      if (!l.equals(list)) {
        for (Point<Number, Number> dataPoint : l) {
          Point<Number, Number> interpolatedDataPoint = new Point<Number, Number>();
          interpolatedDataPoint.setValue(dataPoint.getX(), Point.NO_DATA);
          newList.add(interpolatedDataPoint);
        }
      }
    }

    // Sort the data points in newList in ascending order, e.g. from oldest to
    // newest.
    Collections.sort(newList);

    // If two data points with the same timestamp are in newList,
    // remove the data point that has a placeholder for interpolated data,
    // i.e. only keep the data point that has real data, because that data point
    // was originally in list and no interpolation is needed.
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
   * @param list A list of data points with values that need to be extrapolated at both ends.
   * @return The index from which to start interpolation.
   */
  protected static int extrapolateEndpoints(List<Point<Number, Number>> list) {
    Validator.checkNull(list);
    if (list.isEmpty()) {
      throw new IllegalArgumentException("list is empty.");
    }

    // Head of list
    int index = 0;
    while (index < list.size() && list.get(index).getY().equals(Point.NO_DATA)) {
      index++;
    }
    // Save the index where the first data point is located. Interpolation should begin at that
    // index.
    int startIndex = index;
    double value;
    if (index < list.size()) {
      value = list.get(index).getY().doubleValue();
      for (int idx = 0; idx < index; idx++) {
        list.get(idx).setValue(list.get(idx).getX(), value);
      }
    }

    // Tail of list
    index = list.size() - 1;
    if (index > -1) {
      while (index > -1 && list.get(index).getY().equals(Point.NO_DATA)) {
        index--;
      }
      if (index > -1) {
        value = list.get(index).getY().doubleValue();
        for (int idx = list.size() - 1; idx > index; idx--) {
          list.get(idx).setValue(list.get(idx).getX(), value);
        }
      }
    }
    return startIndex;
  }

  /**
   * Iterates through a list of data points and interpolates all missing values.<br>
   * <br>
   * 
   * <b>Important</b>: The {@link #extrapolateEndpoints(List)} method should be called before
   * calling this method. Otherwise, an <code>IllegalArgumentException</code> will be thrown if a
   * data point at either end of the list is missing.
   * 
   * @param list A list of data points with values that need to be interpolated.
   * @param start The index from which to start interpolation. It is assumed that any missing data
   * points at the beginning of <code>list</code> have been extrapolated already, so begin
   * interpolation at <code>start</code>.
   */
  protected static void interpolateDataPoints(List<Point<Number, Number>> list, int start) {
    Validator.checkNull(list);
    if (!list.isEmpty() && list.get(0).getY().equals(Point.NO_DATA)) {
      throw new IllegalArgumentException("Missing left endpoint.");
    }

    if (list.size() == 2 && list.get(1).getY().equals(Point.NO_DATA)) {
      throw new IllegalArgumentException("Missing right endpoint.");
    }

    for (int startIndex = start, endIndex = start + 2; endIndex < list.size();
        startIndex += 2, endIndex += 2) {
      if (list.get(endIndex).getY().equals(Point.NO_DATA)) {
        int temp = endIndex;
        while (endIndex < list.size() && list.get(endIndex).getY().equals(Point.NO_DATA)) {
          endIndex++;
        }
        if (endIndex < list.size()) {
          Number timestamp = list.get(temp).getX();
          list.set(temp,
              interpolateDataPoint(list.get(startIndex), list.get(endIndex), timestamp));
        }
        else {
          throw new IllegalArgumentException("Missing right endpoint.");
        }
        endIndex = temp;
      }
      if (list.get(startIndex + 1).getY().equals(Point.NO_DATA)) {
        list.set(
            startIndex + 1,
            interpolateDataPoint(list.get(startIndex), list.get(endIndex), list.get(startIndex + 1)
                .getX()));
      }
    }
  }

  /**
   * Returns a list containing data points, interpolating all missing values using linear
   * interpolation. See {@link #interpolateDataPoint(Point, Point, Number)}.
   * 
   * @param list A list of data points.
   * @param lists A list of lists of data points, which include <code>list</code>.
   * @return A list of data points with timestamps, and real and interpolated data.
   */
  public static List<Point<Number, Number>> interpolate(List<Point<Number, Number>> list,
      List<List<Point<Number, Number>>> lists) {
    Validator.checkNull(list);
    Validator.checkNull(lists);

    List<Point<Number, Number>> pointsList = mergeLists(list, lists);
    int startIndex = extrapolateEndpoints(pointsList);
    // Start interpolation from startIndex, which is the first data point in list BEFORE
    // the merge. The reason we start here is that any missing data points at the beginning
    // of list have been extrapolated already, and there could be missing data immediately
    // following the data point at startIndex.
    interpolateDataPoints(pointsList, startIndex);
    return pointsList;
  }

}
