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
