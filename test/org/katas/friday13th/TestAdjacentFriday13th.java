package org.katas.friday13th;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.util.Locale;
import org.junit.Test;

/**
 * JUnit tests for the {@link AdjacentFriday13th} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestAdjacentFriday13th {

  /**
   * Tests the {@link AdjacentFriday13th#find(java.util.Date)} method with a date that is close to
   * but before July 13th.
   */
  @Test
  public void testFindBeforeJuly13th() {
    Calendar calendar = Calendar.getInstance(Locale.US);
    calendar.set(Calendar.MONTH, Calendar.JULY);
    calendar.set(Calendar.DAY_OF_MONTH, 6);
    calendar.set(Calendar.YEAR, 2012);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Calendar c = Calendar.getInstance();
    c.setTime(AdjacentFriday13th.find(calendar.getTime()));

    assertTrue("should be July 13th",
        c.get(Calendar.MONTH) == Calendar.JULY && c.get(Calendar.DAY_OF_MONTH) == 13);
  }

  /**
   * Tests the {@link AdjacentFriday13th#find(java.util.Date)} method with a date that is close to
   * but after July 13th.
   */
  @Test
  public void testFindAfterJuly13th() {
    Calendar calendar = Calendar.getInstance(Locale.US);
    calendar.set(Calendar.MONTH, Calendar.JULY);
    calendar.set(Calendar.DAY_OF_MONTH, 20);
    calendar.set(Calendar.YEAR, 2012);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Calendar c = Calendar.getInstance();
    c.setTime(AdjacentFriday13th.find(calendar.getTime()));

    assertTrue("should be July 13th",
        c.get(Calendar.MONTH) == Calendar.JULY && c.get(Calendar.DAY_OF_MONTH) == 13);
  }

  /**
   * Tests the {@link AdjacentFriday13th#find(java.util.Date)} method with a date that is not close
   * to July 13th.
   */
  @Test
  public void testFindNotJuly13th() {
    Calendar calendar = Calendar.getInstance(Locale.US);
    calendar.set(Calendar.MONTH, Calendar.APRIL);
    calendar.set(Calendar.DAY_OF_MONTH, 20);
    calendar.set(Calendar.YEAR, 2012);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Calendar c = Calendar.getInstance();
    c.setTime(AdjacentFriday13th.find(calendar.getTime()));

    assertFalse("should not be July 13th",
        c.get(Calendar.MONTH) == Calendar.JULY && c.get(Calendar.DAY_OF_MONTH) == 13);
  }

}
