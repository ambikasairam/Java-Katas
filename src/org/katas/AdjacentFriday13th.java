package org.katas;

import java.util.Calendar;
import java.util.Date;

/**
 * Java Kata: Friday the 13th.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class AdjacentFriday13th {

    /** One day in milliseconds. */
    public static final long DAY_IN_MILLISECONDS  = 86400000;

    /** One week in milliseconds. */
    public static final long WEEK_IN_MILLISECONDS = DAY_IN_MILLISECONDS * 7;

    /**
     * Given a date, returns a Date object that contains the date of the nearest
     * Friday the 13th.
     * 
     * @param target
     *            Date specified by the user.
     * @return Date object containing the date for the nearest Friday the 13th.
     */
    public static Date adjacentFriday13th(Date target) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(target);

        Calendar before = (Calendar) calendar.clone();
        Calendar after = (Calendar) calendar.clone();

        // Go to nearest Friday.
        while (before.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            before.setTimeInMillis(before.getTimeInMillis()
                    - DAY_IN_MILLISECONDS);
        }
        while (after.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            after.setTimeInMillis(after.getTimeInMillis() + DAY_IN_MILLISECONDS);
        }

        // Go to first Friday the 13th before and after target date.
        int day = before.get(Calendar.DAY_OF_MONTH);
        while (day != 13) {
            before.setTimeInMillis(before.getTimeInMillis()
                    - WEEK_IN_MILLISECONDS);
            day = before.get(Calendar.DAY_OF_MONTH);
        }
        day = after.get(Calendar.DAY_OF_MONTH);
        while (day != 13) {
            after.setTimeInMillis(after.getTimeInMillis()
                    + WEEK_IN_MILLISECONDS);
            day = after.get(Calendar.DAY_OF_MONTH);
        }

        // Determine which Friday the 13th is closest to target date.
        long numMillisBefore = calendar.getTimeInMillis()
                - before.getTimeInMillis();
        long numMillisAfter = after.getTimeInMillis()
                - calendar.getTimeInMillis();

        if (numMillisBefore < numMillisAfter) {
            return before.getTime();
        }
        else {
            return after.getTime();
        }
    }

    /**
     * Tests the adjacentFriday13th method above.
     * 
     * @param args
     *            None.
     */
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        System.out.println("The Friday the 13th that is closest to " + date
                + " is " + adjacentFriday13th(date) + ".\n");

        calendar.set(Calendar.YEAR, 1989);
        calendar.set(Calendar.MONTH, 10);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        date = calendar.getTime();

        System.out.println("The Friday the 13th that is closest to " + date
                + " is " + adjacentFriday13th(date) + ".");
    }
}
