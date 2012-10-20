package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;
import org.katas.currency.CurrencyExchange;
import org.katas.currency.CurrencyExchangeComparator;

/**
 * This program creates one or more currency exchange tables and finds the exchange sequence that
 * results in the highest profit for each currency exchange table.
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
    while (!this.getLines().isEmpty()) {
      // Get the dimension of the currency exchange table.
      int dimension;
      try {
        dimension = Integer.parseInt(this.getLines().get(0));
        this.getLines().remove(0);
      }
      catch (NumberFormatException e) {
        System.err.println("Invalid dimension found: " + this.getLines().get(0));
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
          (List<Double>) KataUtils.createList(this.getLines().remove(0), " ", KataEnums.DOUBLE);
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
    Collections.sort(exchanges, new CurrencyExchangeComparator());
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
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Arbitrage arbitrage = new Arbitrage();
    arbitrage.setLines(KataUtils.readLines(args[0]));

    if (arbitrage.getLines() != null) {
      arbitrage.processLines();
    }
  }
}
