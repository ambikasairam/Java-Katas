package org.katas.wrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * A JUnit test for the {@link LineWrapper} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestLineWrapper {

  /**
   * Tests the getWrappedString method.
   */
  @Test
  public void testGetWrappedString() {
    StringBuffer buffer = new StringBuffer();
    String testString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. ";
    buffer.append(testString);
    testString = "Aenean in nulla eros. Proin eget eros vitae turpis sodales tincidunt. ";
    buffer.append(testString);
    testString = "Aliquam at orci ut diam placerat laoreet sit amet et nunc. ";
    buffer.append(testString);
    testString = "Suspendisse nisl neque, fermentum et rhoncus at, posuere eget massa.";
    buffer.append(testString);

    char[] chars = LineWrapper.getWrappedString(buffer.toString(), 20);
    String[] strings = new String(chars).split("\n");
    for (String string : strings) {
      assertTrue("should be less than 20", string.length() < 20);
    }

    // Assert that no words were cut off in the middle.

    List<String> list1 = new ArrayList<>();
    String[] words = buffer.toString().split("\\s+");
    for (String word : words) {
      list1.add(word);
    }

    List<String> list2 = new ArrayList<>();
    for (String string : strings) {
      words = string.split("\\s+");
      for (String word : words) {
        list2.add(word);
      }
    }

    assertEquals("both lists should be equal", list1, list2);
  }

}
