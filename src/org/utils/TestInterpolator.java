/* 
 * @file InterpolatorTest.java 
 * 
 * Created on Jan 20, 2012 
 * 
 * Copyright 2002-2012 Referentia Systems Inc. All Rights Reserved. 
 * 155 Kapalulu Place, Suite 200, Honolulu, Hawaii 96819, U.S.A. 
 * 
 * This software is the confidential and proprietary information 
 * of Referentia Systems Incorporated ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use 
 * it only in accordance with the terms of the license agreement 
 * you entered into with Referentia Systems Incorporated. 
 */
package org.utils;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the {@link Interpolator} class.
 * 
 * @author BJ Peter DeLaCruz <bdelacruz@referentia.com>
 */
public class TestInterpolator {

  private List<Point<Number, Number>> dataPoints1;
  private List<Point<Number, Number>> dataPoints2;
  private List<List<Point<Number, Number>>> dataPointsLists;

  private static final String MESSAGE = "should be equal to 100";

  /** Sets up the lists used in the tests. */
  @Before
  public void setUp() {
    this.dataPoints1 = new ArrayList<Point<Number, Number>>();
    this.dataPoints1.add(new Point<Number, Number>(100, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(150, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(200, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(250, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(300, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(350, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(400, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(450, Point.NO_DATA));
    this.dataPoints1.add(new Point<Number, Number>(500, Point.NO_DATA));
    this.dataPoints2 = new ArrayList<Point<Number, Number>>();
    this.dataPoints2.add(new Point<Number, Number>(75, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(125, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(175, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(225, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(275, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(325, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(375, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(425, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(475, Point.NO_DATA));
    this.dataPoints2.add(new Point<Number, Number>(525, Point.NO_DATA));
    this.dataPointsLists = new ArrayList<List<Point<Number, Number>>>();
    this.dataPointsLists.add(this.dataPoints1);
    this.dataPointsLists.add(this.dataPoints2);
  }

  /**
   * Tests the {@link Interpolator#interpolateDataPoint(Point, Point, Number)} method.
   */
  @Test
  public void interpolateDataPointTest() {
    // One missing data point between the end points.
    this.dataPoints1.get(2).setValue(200, 100);
    this.dataPoints1.get(4).setValue(300, 150);
    Point<Number, Number> dataPoint =
        Interpolator.interpolateDataPoint(this.dataPoints1.get(2), this.dataPoints1.get(4), 250);
    assertEquals("should be equal to 125", dataPoint.getY(), 125.0);

    setUp();
    // More than one missing data point between the end points.
    this.dataPoints1.get(2).setValue(200, 100);
    this.dataPoints1.get(8).setValue(500, 250);
    dataPoint =
        Interpolator.interpolateDataPoint(this.dataPoints1.get(2), this.dataPoints1.get(8), 400);
    assertEquals("should be equal to 200", dataPoint.getY(), 200.0);
  }

  /**
   * Tests the {@link Interpolator#mergeLists(List, List)} method.
   */
  @Test
  public void mergeListsTest() {
    List<Point<Number, Number>> dataPoints =
        Interpolator.mergeLists(this.dataPoints1, this.dataPointsLists);

    // The list should not have any data at 275.
    this.dataPoints1.get(2).setValue(200, 100);
    this.dataPoints2.get(4).setValue(275, 175);
    for (Point<Number, Number> dataPoint : dataPoints) {
      if (dataPoint.getX().equals(200)) {
        assertEquals(MESSAGE, dataPoint.getY(), 100);
      }
      else if (dataPoint.getX().equals(275)) {
        assertEquals("should be equal to a placeholder value", dataPoint.getY(), Point.NO_DATA);
      }
    }

    setUp();
    // The list should contain 150, not 175, at 250.
    this.dataPoints1.get(2).setValue(200, 100);
    this.dataPoints1.get(3).setValue(250, 150);
    this.dataPoints2.get(4).setValue(250, 175);
    dataPoints = Interpolator.mergeLists(this.dataPoints1, this.dataPointsLists);
    for (Point<Number, Number> dataPoint : dataPoints) {
      if (dataPoint.getX().equals(200)) {
        assertEquals(MESSAGE, dataPoint.getY(), 100);
      }
      else if (dataPoint.getX().equals(250)) {
        assertEquals("should be equal to 150", dataPoint.getY(), 150);
      }
    }
  }

  /**
   * Tests the {@link Interpolator#extrapolateEndpoints(List)} method with two data points.
   */
  @Test
  public void extrapolateEndpointsTwoDataPointsTest() {
    this.dataPoints1.get(4).setValue(300, 10.0);
    this.dataPoints1.get(6).setValue(400, 20.0);
    Interpolator.extrapolateEndpoints(this.dataPoints1);
    for (int index = 0; index < this.dataPoints1.size(); index++) {
      if (index < 5) {
        assertEquals("should be equal to 10", this.dataPoints1.get(index).getY(), 10.0);
      }
      else if (index == 5) {
        assertEquals("should be equal to a placeholder value", this.dataPoints1.get(index).getY(),
            Point.NO_DATA);
      }
      else if (index > 5) {
        assertEquals("should be equal to 20", this.dataPoints1.get(index).getY(), 20.0);
      }
    }
  }

  /**
   * Tests the {@link Interpolator#extrapolateEndpoints(List)} method with one data point.
   */
  @Test
  public void extrapolateEndpointsOneDataPointTest() {
    this.dataPoints1.get(4).setValue(300, 10.0);
    Interpolator.extrapolateEndpoints(this.dataPoints1);
    for (Point<Number, Number> Point : this.dataPoints1) {
      assertEquals("should be equal to 10", Point.getY(), 10.0);
    }
  }

  /**
   * Tests the {@link Interpolator#extrapolateEndpoints(List)} method with no data points.
   */
  @SuppressWarnings("static-access")
  @Test
  public void extrapolateEndpointsNoDataPointsTest() {
    Interpolator.extrapolateEndpoints(this.dataPoints1);
    for (Point<Number, Number> Point : this.dataPoints1) {
      assertEquals("should be equal to a placeholder value", Point.getY(), Point.NO_DATA);
    }
  }

  /**
   * Tests the {@link Interpolator#interpolateDataPoints(List)} method.
   */
  @Test
  public void interpolateDataPointsTest() {
    this.dataPoints1.get(0).setValue(100, 100.0);
    this.dataPoints1.get(8).setValue(500, 300.0);
    Interpolator.interpolateDataPoints(this.dataPoints1, 0);
    assertEquals(MESSAGE, this.dataPoints1.get(0).getY(), 100.0);
    assertEquals("should be equal to 125", this.dataPoints1.get(1).getY(), 125.0);
    assertEquals("should be equal to 150", this.dataPoints1.get(2).getY(), 150.0);
    assertEquals("should be equal to 175", this.dataPoints1.get(3).getY(), 175.0);
    assertEquals("should be equal to 200", this.dataPoints1.get(4).getY(), 200.0);
    assertEquals("should be equal to 225", this.dataPoints1.get(5).getY(), 225.0);
    assertEquals("should be equal to 250", this.dataPoints1.get(6).getY(), 250.0);
    assertEquals("should be equal to 275", this.dataPoints1.get(7).getY(), 275.0);
    assertEquals("should be equal to 300", this.dataPoints1.get(8).getY(), 300.0);
  }

  /**
   * Tests the {@link Interpolator#interpolateDataPoints(List)} method with a missing leftmost data
   * point.
   */
  @Test(expected = IllegalArgumentException.class)
  public void interpolateDataPointsMissingLeftDataPointTest() {
    this.dataPoints1.get(0).setValue(100, 100.0);
    Interpolator.interpolateDataPoints(this.dataPoints1, 0);
  }

  /**
   * Tests the {@link Interpolator#interpolateDataPoints(List)} method with two data points, one of
   * which is missing.
   */
  @Test(expected = IllegalArgumentException.class)
  public void interpolateDataPointsTwoDataPointsTest() {
    List<Point<Number, Number>> dataPoints = new ArrayList<Point<Number, Number>>();
    dataPoints.add(new Point<Number, Number>(100, 100));
    dataPoints.add(new Point<Number, Number>(200, Point.NO_DATA));
    Interpolator.interpolateDataPoints(dataPoints, 0);
  }

  /**
   * Tests the {@link Interpolator#interpolateDataPoints(List)} method with a missing rightmost data
   * point.
   */
  @Test(expected = IllegalArgumentException.class)
  public void interpolateDataPointsMissingRightDataPointTest() {
    this.dataPoints1.get(8).setValue(500, 300.0);
    Interpolator.interpolateDataPoints(this.dataPoints1, 0);
  }

  /**
   * Tests the {@link Interpolator#interpolate(List, List)} method with one data point missing
   * between two data points.
   */
  @Test
  public void interpolateMissingOneDataPointTest() {
    List<Point<Number, Number>> dataPoints1 = new ArrayList<Point<Number, Number>>();
    dataPoints1.add(new Point<Number, Number>(100, 100));
    dataPoints1.add(new Point<Number, Number>(150, 150));
    dataPoints1.add(new Point<Number, Number>(200, Point.NO_DATA));
    dataPoints1.add(new Point<Number, Number>(300, Point.NO_DATA));
    dataPoints1.add(new Point<Number, Number>(400, Point.NO_DATA));
    dataPoints1.add(new Point<Number, Number>(500, 500));

    List<Point<Number, Number>> dataPoints2 = new ArrayList<Point<Number, Number>>();
    dataPoints2.add(new Point<Number, Number>(50, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(150, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(250, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(350, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(450, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(550, Point.NO_DATA));

    this.dataPointsLists.clear();
    this.dataPointsLists.add(dataPoints1);
    this.dataPointsLists.add(dataPoints2);

    List<Point<Number, Number>> dataPoints =
        Interpolator.interpolate(dataPoints1, this.dataPointsLists);
    assertEquals(MESSAGE, dataPoints.get(0).getY(), 100.0);
    assertEquals(MESSAGE, dataPoints.get(1).getY(), 100);
    assertEquals("should be equal to 150", dataPoints.get(2).getY(), 150.0);
    assertEquals("should be equal to 200", dataPoints.get(3).getY(), 200.0);
    assertEquals("should be equal to 250", dataPoints.get(4).getY(), 250.0);
    assertEquals("should be equal to 300", dataPoints.get(5).getY(), 300.0);
    assertEquals("should be equal to 350", dataPoints.get(6).getY(), 350.0);
    assertEquals("should be equal to 400", dataPoints.get(7).getY(), 400.0);
    assertEquals("should be equal to 450", dataPoints.get(8).getY(), 450.0);
    assertEquals("should be equal to 500", dataPoints.get(9).getY(), 500);
    assertEquals("should be equal to 500", dataPoints.get(10).getY(), 500.0);
  }

  /**
   * Tests the {@link Interpolator#addMissingPoints(List)} method.
   */
  @Test
  public void addMissingPointsTest() {
    List<Point<Number, Number>> dataPoints1 = new ArrayList<Point<Number, Number>>();
    dataPoints1.add(new Point<Number, Number>(0, 0));
    dataPoints1.add(new Point<Number, Number>(100, 100));
    dataPoints1.add(new Point<Number, Number>(150, 150));
    dataPoints1.add(new Point<Number, Number>(200, 0));
    dataPoints1.add(new Point<Number, Number>(300, 0));
    dataPoints1.add(new Point<Number, Number>(400, 0));
    dataPoints1.add(new Point<Number, Number>(500, 350));
    dataPoints1.add(new Point<Number, Number>(501, 0));
    dataPoints1.add(new Point<Number, Number>(600, 0));
    dataPoints1.add(new Point<Number, Number>(700, 500));
    dataPoints1.add(new Point<Number, Number>(701, 0));
    dataPoints1.add(new Point<Number, Number>(702, 600));
    dataPoints1.add(new Point<Number, Number>(704, 0));

    this.dataPointsLists.clear();
    this.dataPointsLists.add(dataPoints1);

    Interpolator.addMissingPoints(this.dataPointsLists);
    assertEquals("should be equal to 99", dataPoints1.get(1).getX().longValue(), 99L);
    assertEquals("should be equal to 151", dataPoints1.get(4).getX().longValue(), 151L);
    assertEquals("should be equal to 499", dataPoints1.get(8).getX().longValue(), 499L);
    assertEquals("should be equal to 501", dataPoints1.get(10).getX().longValue(), 501L);
    assertEquals("should be equal to 699", dataPoints1.get(12).getX().longValue(), 699L);
    assertEquals("should be equal to 701", dataPoints1.get(14).getX().longValue(), 701L);
    assertEquals("should be equal to 703", dataPoints1.get(16).getX().longValue(), 703L);
  }

  /**
   * Tests the {@link Interpolator#interpolate(List, List)} method with more than one data point
   * missing between two data points.
   */
  @Test
  public void interpolateMissingDataPointsTest() {
    List<Point<Number, Number>> dataPoints1 = new ArrayList<Point<Number, Number>>();
    dataPoints1.add(new Point<Number, Number>(100, 100));
    dataPoints1.add(new Point<Number, Number>(1000, 1000));

    List<Point<Number, Number>> dataPoints2 = new ArrayList<Point<Number, Number>>();
    dataPoints2.add(new Point<Number, Number>(50, Point.NO_DATA));
    dataPoints2.add(new Point<Number, Number>(150, 600));
    dataPoints2.add(new Point<Number, Number>(180, 180));
    dataPoints2.add(new Point<Number, Number>(200, 400));
    dataPoints2.add(new Point<Number, Number>(250, 500));
    dataPoints2.add(new Point<Number, Number>(300, 400));
    dataPoints2.add(new Point<Number, Number>(350, 600));
    dataPoints2.add(new Point<Number, Number>(360, 640));
    dataPoints2.add(new Point<Number, Number>(365, 650));
    dataPoints2.add(new Point<Number, Number>(400, 200));
    dataPoints2.add(new Point<Number, Number>(450, 400));
    dataPoints2.add(new Point<Number, Number>(500, 200));
    dataPoints2.add(new Point<Number, Number>(550, 750));
    dataPoints2.add(new Point<Number, Number>(600, 500));
    dataPoints2.add(new Point<Number, Number>(650, 250));
    dataPoints2.add(new Point<Number, Number>(700, 500));
    dataPoints2.add(new Point<Number, Number>(750, 750));
    dataPoints2.add(new Point<Number, Number>(800, 100));
    dataPoints2.add(new Point<Number, Number>(850, 200));
    dataPoints2.add(new Point<Number, Number>(900, 300));
    dataPoints2.add(new Point<Number, Number>(950, 300));
    dataPoints2.add(new Point<Number, Number>(1000, 200));
    dataPoints2.add(new Point<Number, Number>(1050, 100));

    this.dataPointsLists.clear();
    this.dataPointsLists.add(dataPoints1);
    this.dataPointsLists.add(dataPoints2);

    List<Point<Number, Number>> dataPoints =
        Interpolator.interpolate(dataPoints1, this.dataPointsLists);
    assertEquals(MESSAGE, dataPoints.get(0).getY(), 100.0);
    // Start at x = 150. Stop at x = 950. X should be equal to Y.
    for (int index = 2; index < dataPoints.size() - 2; index++) {
      String msg = "should be equal to " + dataPoints.get(index).getX().doubleValue();
      assertEquals(msg, dataPoints.get(index).getX().doubleValue(), dataPoints.get(index).getY());
    }
    assertEquals("should be equal to 1000", dataPoints.get(dataPoints.size() - 1).getY(), 1000.0);
  }
}
