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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.bpd.utils.validation.Validator;

/**
 * This class contains methods that find the number of representations for a given positive integer.
 * 
 * @author BJ Peter DeLaCruz
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/1210_Consecutive_Primes.pdf">Consecutive
 * Primes</a>
 */
final class ConsecutivePrimes {

  private final Set<Integer> primes = new LinkedHashSet<Integer>();
  private final Set<Integer> composites = new HashSet<Integer>();
  private final Map<Integer, List<List<Integer>>> resultsMap =
      new HashMap<Integer, List<List<Integer>>>();

  /**
   * Finds all prime numbers that are less than the given number.
   * 
   * @param number The number for which to find the number of representations.
   */
  private void sieveEratosthenes(int number) {
    Validator.checkNegative(number);

    for (int n = 2; n <= number; n++) {
      if (composites.contains(n)) {
        continue;
      }
      primes.add(n);
      for (int sum = n + n; sum <= number; sum += n) {
        composites.add(sum);
      }
    }
  }

  /**
   * Counts the number of representations for the given number and stores the results in a map.
   * 
   * @param number The number for which to find the number of representations.
   */
  private void countSum(int number) {
    Validator.checkNegative(number);

    if (resultsMap.get(number) != null) {
      return;
    }

    // Find all prime numbers first.
    sieveEratosthenes(number);

    List<Integer> primes = new ArrayList<Integer>(this.primes);
    List<List<Integer>> resultsList = new ArrayList<List<Integer>>();
    for (int i = 0; i < primes.size() && primes.get(i) <= number; i++) {
      List<Integer> results = new ArrayList<Integer>();
      int sum = primes.get(i);
      results.add(sum);
      for (int j = i + 1; j < primes.size() && sum < number; j++) {
        sum += primes.get(j);
        results.add(primes.get(j));
      }
      if (sum == number) {
        resultsList.add(results);
      }
    }
    resultsMap.put(number, resultsList);
  }

  /**
   * Returns the number of representations for the given number.
   * 
   * @param number The number for which to find the number of representations.
   * @return The number of representations.
   */
  protected Integer getCount(int number) {
    Validator.checkNegative(number);

    countSum(number);
    return resultsMap.get(number).size();
  }

  /**
   * Returns a list of representations for the given number.
   * 
   * @param number The number for which to find the number of representations.
   * @return A list of representations for the given number.
   */
  protected String getResults(int number) {
    StringBuffer buffer = new StringBuffer();
    if (resultsMap.get(number) == null && getCount(number) == 0) {
      return "[]";
    }
    for (List<Integer> list : resultsMap.get(number)) {
      String temp = list + " ";
      buffer.append(temp);
    }
    return buffer.toString().substring(0, buffer.toString().length() - 1);
  }

}
