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

import java.util.Calendar;
import java.util.Date;

/**
 * This program finds the closest Friday the 13th before or after the current date.
 * 
 * @author BJ Peter DeLaCruz
 */
final class AdjacentFriday13th {

  /** One day in milliseconds. */
  public static final long DAY_IN_MILLISECONDS = 86_400_000;

  /** One week in milliseconds. */
  public static final long WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;

  /**
   * Given a date, returns a Date object that contains the date of the nearest Friday the 13th.
   * 
   * @param target Date specified by the user.
   * @return Date object containing the date for the nearest Friday the 13th.
   */
  public static Date find(Date target) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(target);

    Calendar before = (Calendar) calendar.clone();
    Calendar after = (Calendar) calendar.clone();

    // Go to nearest Friday.
    while (before.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
      before.setTimeInMillis(before.getTimeInMillis() - DAY_IN_MILLISECONDS);
    }
    while (after.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
      after.setTimeInMillis(after.getTimeInMillis() + DAY_IN_MILLISECONDS);
    }

    // Go to first Friday the 13th before and after target date.
    int day = before.get(Calendar.DAY_OF_MONTH);
    while (day != 13) {
      before.setTimeInMillis(before.getTimeInMillis() - WEEK_IN_MILLISECONDS);
      day = before.get(Calendar.DAY_OF_MONTH);
    }
    day = after.get(Calendar.DAY_OF_MONTH);
    while (day != 13) {
      after.setTimeInMillis(after.getTimeInMillis() + WEEK_IN_MILLISECONDS);
      day = after.get(Calendar.DAY_OF_MONTH);
    }

    // Determine which Friday the 13th is closest to target date.
    long numMillisBefore = calendar.getTimeInMillis() - before.getTimeInMillis();
    long numMillisAfter = after.getTimeInMillis() - calendar.getTimeInMillis();

    if (numMillisBefore < numMillisAfter) {
      return before.getTime();
    }
    else {
      return after.getTime();
    }
  }

}
