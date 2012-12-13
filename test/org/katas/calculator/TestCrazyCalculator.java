package org.katas.calculator;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * JUnit tests for the Crazy Calculator kata.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestCrazyCalculator {

  /**
   * Tests the Crazy Calculator kata.
   */
  @Test
  public void testCrazyCalculator() {
    CrazyCalculator calculator = new CrazyCalculator();
    List<String> lines = new ArrayList<>();
    lines.add("1");
    lines.add("");
    lines.add("+@1L");
    lines.add("-+3R");
    lines.add("*-2R");
    lines.add("//2R");
    lines.add("1@1");
    lines.add("5@5+4");
    lines.add("2@3-12/6/5+3");
    lines.add("-1+3");
    lines.add("-1--3");
    calculator.setLines(lines);
    calculator.processLines();
    List<String> results = calculator.getResults();
    assertEquals("should be 5", 5, results.size());
    assertEquals("should be 2", 2, Integer.parseInt(results.get(0)));
    assertEquals("should be 6", 6, Integer.parseInt(results.get(1)));
    assertEquals("should be 14", 14, Integer.parseInt(results.get(2)));
    assertEquals("should be -4", -4, Integer.parseInt(results.get(3)));
    assertEquals("should be 3", 3, Integer.parseInt(results.get(4)));
  }

}
