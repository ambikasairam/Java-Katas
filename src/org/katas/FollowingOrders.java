package org.katas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;

/**
 * Given N letters and a list of constraints, this program will return a list of strings that
 * satisfy <span style="text-decoration:underline">all</span> of the constraints.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/124_Following_Orders.pdf">Following
 * Orders</a>
 * 
 * @author BJ Peter DeLaCruz
 */
public class FollowingOrders extends Kata {

  /**
   * Creates a new FollowingOrders object.
   */
  public FollowingOrders() {
    // Empty constructor.
  }

  /**
   * Processes each pair of lines (one is the letters to order, the other is the list of
   * constraints), and prints out a list of strings whose characters satisfy <span
   * style="text-decoration:underline">all</span> of the constraints.
   */
  @SuppressWarnings({ "deprecation", "unchecked" })
  @Override
  public void processLines() {
    while (!this.lines.isEmpty()) {

      String line = this.lines.remove(0);
      List<Character> letters =
          (List<Character>) KataUtils.createList(line, " ", KataEnums.CHARACTER);
      Set<Character> tempSet = new HashSet<Character>(letters);
      if (tempSet.size() < letters.size()) {
        try {
          throw new IOException("Duplicate letters exist: " + line);
        }
        catch (IOException e) {
          System.err.println(e.getMessage());
          return;
        }
      }

      String constraints = this.lines.remove(0);
      List<Character> constraintsList =
          (List<Character>) KataUtils.createList(constraints, " ", KataEnums.CHARACTER);
      if (constraintsList.size() % 2 != 0) {
        try {
          throw new IOException("Odd number of constraints: " + constraints);
        }
        catch (IOException e) {
          System.err.println(e.getMessage());
          return;
        }
      }

      List<String> strings = KataUtils.makeStringsList(new ArrayList<Character>(letters));

      List<String> results = new ArrayList<String>();
      for (String s : strings) {
        letters = new ArrayList<Character>();
        for (char c : s.toCharArray()) {
          letters.add(c);
        }
        if (this.checkConstraints(letters, constraintsList)) {
          results.add(s);
        }
      }

      Collections.sort(results);
      this.printResults(line, constraintsList, results);

    }
  }

  /**
   * Checks if all constraints are satisfied.
   * 
   * @param letters The letters to check.
   * @param constraints The constraints to meet.
   * @return True if all constraints are satisfied, false otherwise.
   */
  private boolean checkConstraints(List<Character> letters, List<Character> constraints) {
    for (int index = 0; index < constraints.size(); index += 2) {
      if (letters.indexOf(constraints.get(index)) > letters.indexOf(constraints.get(index + 1))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Prints the results to the screen.
   * 
   * @param letters The original list of letters.
   * @param constraints All of the constraints.
   * @param results The list of results.
   */
  private void printResults(String letters, List<Character> constraints, List<String> results) {
    System.out.println("Given the letters: " + letters);

    System.out.print("Given the constraints: ");
    for (int index = 0; index < constraints.size(); index += 2) {
      System.out.print(constraints.get(index) + " < " + constraints.get(index + 1));
      if (index + 2 < constraints.size()) {
        System.out.print(" and ");
      }
    }

    if (results.isEmpty()) {
      System.out.println("\n\nNo ordering exists.\n");
    }
    else {
      System.out.println("\n\nHere are the results (" + results.size() + "):");
      for (String str : results) {
        System.out.println(str);
      }
      System.out.println();
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing the letters and constraints.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    FollowingOrders orders = new FollowingOrders();
    orders.setLines(KataUtils.readLines(args[0]));

    try {
      if (orders.getLines() != null) {
        if (orders.getLines().size() % 2 != 0) {
          throw new IOException("Invalid file. File contains odd number of lines.");
        }
        orders.processLines();
      }
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
