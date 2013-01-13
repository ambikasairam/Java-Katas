/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
