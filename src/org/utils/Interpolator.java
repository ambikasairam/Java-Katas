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
    if (!list.isEmpty() && list.get(start).getY().equals(Point.NO_DATA)) {
      throw new IllegalArgumentException("Missing left endpoint.");
    }

    if (list.size() == 2 && list.get(1).getY().equals(Point.NO_DATA)) {
      throw new IllegalArgumentException("Missing right endpoint.");
    }

    for (int startIndex = start, endIndex = start + 2; endIndex < list.size(); startIndex += 2,
        endIndex += 2) {
      // More than one data point is missing, so all of the data points between
      // startIndex and endIndex have to be interpolated in this iteration.
      if (list.get(endIndex).getY().equals(Point.NO_DATA)) {
        int tempEnd = endIndex;
        // Find the first data point that has data.
        while (tempEnd < list.size() && list.get(tempEnd).getY().equals(Point.NO_DATA)) {
          tempEnd++;
        }
        if (tempEnd == list.size()) {
          throw new IllegalArgumentException("Missing right endpoint.");
        }

        int tempStart = startIndex;
        // Interpolate all of the data points between tempStart and tempEnd.
        while (tempStart + 1 < tempEnd) {
          Number tstamp = list.get(tempStart + 1).getX();
          list.set(tempStart + 1,
              interpolateDataPoint(list.get(tempStart), list.get(tempEnd), tstamp));
          tempStart++;
        }
        // Reset the indices.
        startIndex = tempStart - 1;
        endIndex = tempStart + 1;
      }
      // Only one data point is missing.
      else if (list.get(startIndex + 1).getY().equals(Point.NO_DATA)) {
        Number tstamp = list.get(startIndex + 1).getX();
        list.set(startIndex + 1,
            interpolateDataPoint(list.get(startIndex), list.get(endIndex), tstamp));
      }
    }
  }

  /**
   * Adds points for missing data to lists. For example:
   * 
   * <pre>
   * Timestamp | Value:
   * ------------------
   * 12000000  | 500
   * 12000001  | 0
   * 12500000  | 0
   * ...
   * 15000000  | 500
   * 15999999  | 500
   * 16000000  | 0
   * </pre>
   * 
   * <b>Important:</b> Call this method <u>first</u> before calling <code>interpolate</code>.<br>
   * <br>
   * 
   * @param lists The list that contains lists to which to add points for missing data.
   * @see Interpolator#interpolate(List, List)
   */
  public static void addZeroDataPoints(List<List<Point<Number, Number>>> lists) {
    Validator.checkNull(lists);

    for (List<Point<Number, Number>> list : lists) {
      for (int index = 1; index < list.size() - 1; index++) {
        if (list.get(index).getY().floatValue() == 0.0
            && !list.get(index - 1).getX().equals(list.get(index).getX())) {
          list.add(index, new Point<Number, Number>(list.get(index - 1).getX().longValue() + 1, 0));
          index++;
        }
        if (list.get(index).getY().floatValue() == 0.0
            && !list.get(index + 1).getX().equals(list.get(index).getX())) {
          list.add(index + 1, new Point<Number, Number>(list.get(index + 1).getX().longValue() - 1,
              0));
          index++;
        }
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
