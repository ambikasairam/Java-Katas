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
package org.katas.roulette;

import java.util.ArrayList;
import java.util.List;
import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;
import com.bpd.utils.ListUtils;

/**
 * My solution to the Josephus problem (also known as Roman Roulette).
 * 
 * @author BJ Peter DeLaCruz
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/Roman_Roulette.pdf">Roman Roulette</a>
 */
public class RomanRoulette extends Kata {

  /** The ID of the last person standing in a circle of people. */
  private final int survivor;

  /**
   * Creates a new RomanRoulette object.
   * 
   * @param survivor The ID of the last person standing in a circle of people.
   */
  public RomanRoulette(int survivor) {
    this.survivor = survivor;
  }

  /**
   * For each line, finds the position that a person should stand at in a circle of N people, where
   * every K-th person dies, in order to survive.
   */
  @Override
  public void processLines() {
    while (!this.lines.isEmpty()) {
      String line = this.lines.remove(0);
      @SuppressWarnings("unchecked")
      List<Integer> params = (List<Integer>) KataUtils.createList(line, " ", KataEnums.INTEGER);
      if (params == null) {
        continue;
      }
      if (params.size() != 2) {
        System.err.println("Need only two parameters: N and K. Found " + line.length() + ".");
        continue;
      }

      int n = params.get(0); // Number of people in the circle
      int k = params.get(1); // Kill every k-th person

      if (n == 0 && k == 0) { // EOF
        return;
      }
      // If there is only one person in the circle from the very beginning, then that person is the
      // survivor.
      if (n == 1) {
        System.out.println("N: " + n + "\tK: " + k + "\tPosition: " + n);
        continue;
      }

      findPosition(n, k, params);
    }
  }

  /**
   * Finds the position that a person should stand at in a circle of N people, where every K-th
   * person dies, in order to survive.
   * 
   * @param n The number of people in a circle.
   * @param k Every K-th person dies.
   * @param params Contains N and K.
   */
  private void findPosition(int n, int k, List<Integer> params) {
    List<Integer> idNumbers = ListUtils.createIntegersList(1, n);
    int burier; // The person who buries the person who just died (the victim)
    boolean isFirstVictim = true;
    boolean isPositionFound = false;
    int numPeople = n;
    int kthPerson = k;

    List<Integer> tempList = new ArrayList<Integer>();
    tempList.addAll(idNumbers);

    for (int index = 0, tempIndex, indexOfVictim, indexOfBurier; index < numPeople; index++) {
      tempIndex = index;
      // Start counting from the person at position index.
      indexOfVictim = (index + kthPerson - 1) % numPeople;
      while (!idNumbers.isEmpty()) {
        if (isFirstVictim) {
          // Start counting from 1 to k beginning with the person at position index.
          indexOfBurier = (indexOfVictim + kthPerson) % numPeople;
          isFirstVictim = false;
        }
        else {
          // Start counting from 1 to k beginning with the person to the left of the victim.
          indexOfBurier = (indexOfVictim + kthPerson + 1) % numPeople;
        }
        burier = idNumbers.get(indexOfBurier);
        // Don't remove the element at position indexOfVictim, i.e. the victim. Just set its
        // value to that of burier. Then remove the element at position indexOfBurier, i.e. the
        // burier.
        idNumbers.set(indexOfVictim, burier);
        idNumbers.remove(indexOfBurier);
        numPeople--;
        if (idNumbers.size() == 1) {
          // We found the position in the circle that a person should stand at in order to
          // survive.
          if (idNumbers.get(0) == this.survivor) {
            System.out.print("N: " + params.get(0) + "\tK: " + params.get(1));
            System.out.println("\tPosition: " + (index + 1));
            isPositionFound = true;
            break;
          }
          idNumbers.clear();
        }
        indexOfVictim = (indexOfVictim + kthPerson) % numPeople;
      }
      if (isPositionFound) {
        break;
      }
      // Try the next position, but first, reset all values.
      idNumbers.clear();
      idNumbers.addAll(tempList);
      index = tempIndex;
      numPeople = params.get(0);
      isFirstVictim = true;
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing lines with two values, N and K.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    RomanRoulette roulette = new RomanRoulette(1);
    roulette.setLines(KataUtils.readLines(args[0]));

    if (roulette.getLines() != null) {
      roulette.processLines();
    }
  }
}
