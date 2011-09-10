package org.katas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given N letters and a list of constraints, this program will return a list of strings that
 * satisfy <span style="text-decoration:underline">all</span> of the constraints.
 * 
 * @author BJ Peter DeLaCruz
 */
public class FollowingOrders {

  /** The lines in the file. */
  private final List<String> lines = new ArrayList<String>();

  /**
   * Creates a new FollowingOrders object.
   */
  public FollowingOrders() {
    // Empty constructor.
  }

  /**
   * Gets lines read in from the file.
   * 
   * @return The lines read in from the file.
   */
  public List<String> getLines() {
    return this.lines;
  }

  /**
   * Processes each pair of lines (one is the letters to order, the other is the list of
   * constraints), and prints out a list of strings whose characters satisfy <span
   * style="text-decoration:underline">all</span> of the constraints.
   */
  public void processLines() {
    while (this.lines.size() != 0) {

      String line = this.lines.remove(0);
      List<Character> letters = KataUtils.getLine(line);
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
      List<Character> constraintsList = KataUtils.getLine(constraints);
      if (constraintsList.size() % 2 != 0) {
        try {
          throw new IOException("Odd number of constraints: " + constraints);
        }
        catch (IOException e) {
          System.err.println(e.getMessage());
          return;
        }
      }

      List<String> strings = KataUtils.makeList(new ArrayList<Character>(letters), letters.size());

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
   * Prints the results of the algorithm in the <code>processLines</code> method.
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
   * The main program: given a filename, extracts all of the lines in the file and then processes
   * them.
   * 
   * @param args The filename containing the letters and constraints.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    BufferedReader reader = null;
    String line = "";
    FollowingOrders orders = new FollowingOrders();
    try {
      reader = new BufferedReader(new FileReader(args[0]));
      while ((line = reader.readLine()) != null) {
        orders.getLines().add(line);
      }
      if (orders.getLines().size() % 2 != 0) {
        throw new IOException("Invalid file. File contains odd number of lines.");
      }
      orders.processLines();
    }
    catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
