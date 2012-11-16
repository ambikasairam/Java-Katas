package org.katas.bowling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * JUnit tests for the Bowling kata.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestBowling {

  /**
   * Tests scores that are valid.
   */
  @Test
  public void testValidScores() {
    int score = Bowling.getScore("XXXXXXXXXXXX");
    assertEquals("should be 300", 300, score);

    score = Bowling.getScore("9-9-9-9-9-9-9-9-9-9-");
    assertEquals("should be 90", 90, score);

    score = Bowling.getScore("5/5/5/5/5/5/5/5/5/5/5");
    assertEquals("should be 150", 150, score);

    score = Bowling.getScore("11111111111111111111");
    assertEquals("should be 20", 20, score);
  }

  /**
   * Tests scores that are invalid.
   */
  @Test
  public void testInvalidScores() {
    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/5/5-");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/X/5/5");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/555");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("555/5/5/5/5/5/5/5/5-");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }
  }

}
