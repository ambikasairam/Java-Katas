package org.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * JUnit tests for the {@link StringUtils} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestStringUtils {

  /**
   * Tests the {@link StringUtils#replaceLast(String, String, String)} method.
   */
  @Test
  public void testReplaceLast() {
    String hello = "hellohellohello";
    String string = StringUtils.replaceLast(hello, "ello", "i");
    assertEquals("should be \"hellohellohi\"", "hellohellohi", string);
  }

}
