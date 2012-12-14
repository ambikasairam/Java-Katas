package org.katas.primes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * JUnit tests for the {@link ConsecutivePrimes} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestConsecutivePrimes {

  /**
   * Tests the {@link ConsecutivePrimes#getCount(int)} and {@link ConsecutivePrimes#getResults(int)}
   * methods.
   */
  @Test
  public void testPrimes() {
    ConsecutivePrimes primes = new ConsecutivePrimes();
    assertTrue("should be 1", primes.getCount(2) == 1);
    assertEquals("should have one representation", "[2]", primes.getResults(2));

    primes = new ConsecutivePrimes();
    assertTrue("should be 2", primes.getCount(53) == 2);
    String expected = "[5, 7, 11, 13, 17] [53]";
    assertEquals("should have two representations", expected, primes.getResults(53));

    primes = new ConsecutivePrimes();
    assertEquals("should have two representations", expected, primes.getResults(53));

    primes = new ConsecutivePrimes();
    assertTrue("should be 3", primes.getCount(41) == 3);
    expected = "[2, 3, 5, 7, 11, 13] [11, 13, 17] [41]";
    assertEquals("should have three representations", expected, primes.getResults(41));
  }

  /**
   * Tests the {@link ConsecutivePrimes#getCount(int)} and {@link ConsecutivePrimes#getResults(int)}
   * methods.
   */
  @Test
  public void testNoPrimes() {
    ConsecutivePrimes primes = new ConsecutivePrimes();
    assertTrue("should be 0", primes.getCount(20) == 0);
    assertEquals("should have no representations", "[]", primes.getResults(0));
  }

}
