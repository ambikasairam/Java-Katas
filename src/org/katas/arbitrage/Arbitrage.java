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
package org.katas.arbitrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;

/**
 * This program creates one or more currency exchange tables and finds the exchange sequence that
 * results in the highest profit for each currency exchange table.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/104_Arbitrage.pdf">Arbitrage</a>
 * 
 * @author BJ Peter DeLaCruz
 */
public class Arbitrage extends Kata {

  /**
   * Creates a new Arbitrage object.
   */
  public Arbitrage() {
    // Empty constructor.
  }

  /**
   * Processes a currency exchange table and finds the exchange sequence that results in the highest
   * profit.
   */
  @Override
  public void processLines() {
    while (!this.lines.isEmpty()) {
      // Get the dimension of the currency exchange table.
      int dimension;
      try {
        dimension = Integer.parseInt(this.lines.get(0));
        this.lines.remove(0);
      }
      catch (NumberFormatException e) {
        System.err.println("Invalid dimension found: " + this.lines.get(0));
        return;
      }

      List<Double[]> table = createTable(dimension);

      // Create list of sequences from 1 to dimension.
      List<Character> characters = new ArrayList<Character>();
      for (char index = 49; index < (dimension + 49); index++) {
        characters.add(index);
      }

      List<String> sequences = getAllExchangeSequences(characters);

      List<CurrencyExchange> exchanges = calculateProfits(table, sequences);

      updateList(exchanges);

      // Print the exchange sequence that results in the highest profit.
      if (exchanges.isEmpty()) {
        System.out.println("\nNo arbitrage sequence exists.\n");
      }
      else {
        System.out.println("\n" + exchanges.get(exchanges.size() - 1) + "\n");
      }
    }
  }

  /**
   * Creates a table with N rows and N columns.
   * 
   * @param dimension Dimension of the table (number of rows and columns).
   * @return The table used to store the exchange rates.
   */
  private List<Double[]> createTable(int dimension) {
    List<Double[]> table = new ArrayList<Double[]>();

    System.out.println("Currency Exchange Table:");
    for (int index = 0; index < dimension; index++) {
      Double[] row = new Double[dimension];
      @SuppressWarnings("unchecked")
      List<Double> data =
          (List<Double>) KataUtils.createList(this.lines.remove(0), " ", KataEnums.DOUBLE);
      for (int pos = 0; pos < row.length; pos++) {
        if (pos == index) {
          row[pos] = 1.0;
        }
        else {
          row[pos] = data.remove(0);
        }
      }
      table.add(row);

      for (Double d : row) {
        System.out.print(d + "\t");
      }
      System.out.println();
    }

    return table;
  }

  /**
   * Gets all exchange sequences.
   * 
   * @param characters The characters used to generate all exchange sequences.
   * @return The list of strings representing exchange sequences whose lengths are from 2 to N.
   */
  private List<String> getAllExchangeSequences(List<Character> characters) {
    List<String> permutations =
        KataUtils.getAllPermutationsOfSubsequences(new HashSet<Character>(characters));
    Collections.sort(permutations);

    // Delete all strings with lengths less than 2.
    List<String> deletedStrings = new ArrayList<String>();
    for (int index = 0; index < permutations.size(); index++) {
      if (permutations.get(index).length() <= 1) {
        deletedStrings.add(permutations.get(index));
      }
    }
    permutations.removeAll(deletedStrings);

    for (int index = 0; index < permutations.size(); index++) {
      String combo = permutations.get(index);
      permutations.set(index, combo + combo.substring(0, 1));
    }
    Collections.sort(permutations);

    return permutations;
  }

  /**
   * Calculates the profits made by all exchange sequence.
   * 
   * @param table The table containing the exchange rates.
   * @param sequences The list of exchange sequences.
   * @return A list of <code>CurrencyExchange</code> objects that contain exchange sequences and
   * profits.
   */
  private List<CurrencyExchange> calculateProfits(List<Double[]> table, List<String> sequences) {
    List<CurrencyExchange> exchanges = new ArrayList<CurrencyExchange>();
    for (String str : sequences) {
      char[] positions = str.toCharArray();
      int row = Integer.parseInt((positions[0] - 49) + "");
      double result = table.get(row)[row];
      for (int index = 0; index < positions.length; index++) {
        int col = Integer.parseInt((positions[index] - 49) + "");
        result *= table.get(row)[col];
        row = col;
      }
      if (result > 1.00) {
        exchanges.add(new CurrencyExchange(str, result));
      }
    }
    return exchanges;
  }

  /**
   * Removes all exchange sequences that are longer than the shortest exchange sequences and removes
   * all but one exchange sequence that result in the same profit.
   * 
   * @param exchanges The list of exchange sequences to update.
   */
  private void updateList(List<CurrencyExchange> exchanges) {
    Collections.sort(exchanges);
    // Remove the longest exchanges.
    for (int index = 0; index < exchanges.size() - 1; index++) {
      if (exchanges.get(index).getExchangeSequence().length() < exchanges.get(index + 1)
          .getExchangeSequence().length()) {
        exchanges.remove(index + 1);
        index--;
      }
    }

    // Remove all but one exchange that result in the same profit.
    for (int index = 0; index < exchanges.size() - 1; index++) {
      Double profit1 = exchanges.get(index).getProfit();
      Double profit2 = exchanges.get(index + 1).getProfit();
      if (profit1.compareTo(profit2) == 0) {
        exchanges.remove(index + 1);
        index--;
      }
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing the currency exchange table.
   */
  public static void main(String... args) {
    String filename = Arbitrage.class.getResource("example.kata").getPath();

    Arbitrage arbitrage = new Arbitrage();
    arbitrage.setLines(KataUtils.readLines(filename));

    if (arbitrage.getLines() != null) {
      arbitrage.processLines();
    }
  }
}
