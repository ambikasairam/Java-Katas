package org.katas.linear;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * A program that will solve linear equations with only one variable.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/1200_Linear_Equation.pdf">Linear
 * Equation</a>
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class LinearEquationSolver extends Kata {

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    int numLines = 0;
    try {
      numLines = Integer.parseInt(this.getLines().get(0));
      this.getLines().remove(0);
    }
    catch (NumberFormatException e) {
      System.err.print("Invalid argument: " + this.getLines().get(0));
      System.err.println(". Expected a value for the number of equations to process.");
      return;
    }

    if (numLines != this.getLines().size()) {
      System.err.print("Expected " + numLines + " equations. ");
      System.err.println("Found " + this.getLines().size() + ".");
      return;
    }

    while (!this.getLines().isEmpty()) {
      String equation = this.getLines().remove(0);
      List<String> variablesL = new ArrayList<String>();
      List<String> constantsL = new ArrayList<String>();
      List<String> variablesR = new ArrayList<String>();
      List<String> constantsR = new ArrayList<String>();

      if (!equation.contains("=")) {
        System.err.println("Invalid equation: " + equation + ". Missing right-hand side.");
        continue;
      }

      // Process left-hand side first.
      StringTokenizer tokenizer = new StringTokenizer(equation);
      while (tokenizer.hasMoreTokens()) {
        if (processSide(tokenizer, variablesL, constantsL)) {
          break;
        }
      }

      // Then process right-hand side.
      while (tokenizer.hasMoreTokens()) {
        processSide(tokenizer, variablesR, constantsR);
      }

      solve(variablesL, constantsL, variablesR, constantsR);
    }
  }

  /**
   * Solves the current equation by processing the lists of variables and constants.
   * 
   * @param variablesL The list of variables that appear on the left-hand side.
   * @param constantsL The list of constants that appear on the right-hand side.
   * @param variablesR The list of variables that appear on the left-hand side.
   * @param constantsR The list of constants that appear on the right-hand side.
   * @return <ul>
   * <li><b>NaN</b> if the equation cannot be solved.</li>
   * <li><b>Infinity</b> if there are an infinite number of solutions.</li>
   * <li>The solution, if one exists.</li>
   * </ul>
   */
  protected static double solve(List<String> variablesL, List<String> constantsL,
      List<String> variablesR, List<String> constantsR) {
    // Sum variables and constants on left-hand side first and then right-hand side.
    int variableL = sumList(variablesL);
    int constantL = sumList(constantsL);

    int variableR = sumList(variablesR);
    int constantR = sumList(constantsR);

    // Solve for each side.
    int variableSum = variableL - variableR;
    int constantSum = constantR - constantL;
    if (variableSum < 0 && constantSum < 0) {
      variableSum *= -1;
      constantSum *= -1;
    }
    else if (constantR == 0 && constantL < 0) {
      constantSum *= -1;
    }

    // Print results.
    if (variableSum == 0 && constantSum == 0) {
      System.out.println("IDENTITY");
      return Double.POSITIVE_INFINITY;
    }
    else if (variableSum == 0) {
      System.out.println("IMPOSSIBLE");
      return Double.NaN;
    }
    else if (constantSum == 0) {
      System.out.println(0);
      return 0.0;
    }
    else {
      double result = 0.0;
      if (variableSum > 1) {
        result = (double) constantSum / (double) variableSum;
      }
      else {
        result = constantSum;
      }
      System.out.println(result);
      return result;
    }
  }

  /**
   * Returns the sum of the contents of a given list.
   * 
   * @param strings The list of variables or constants whose contents will be summed together.
   * @return The sum of the contents of the given list.
   */
  private static int sumList(List<String> strings) {
    int sum = 0;
    for (String s : strings) {
      if (s.contains("x")) {
        sum += extractConstant(s);
      }
      else if (s.contains("+")) {
        sum += Integer.parseInt(s.substring(1));
      }
      else {
        sum += Integer.parseInt(s);
      }
    }
    return sum;
  }

  /**
   * Processes a side of the equation.
   * 
   * @param tokenizer The StringTokenizer.
   * @param variables The list of variables to which a token will be added.
   * @param constants The list of constants to which a token will be added.
   * @return True if an '=' was reached (in which case the left-hand side of the equation is
   * finished processing), false otherwise (in which case the right-hand side is finished
   * processing).
   */
  protected static boolean processSide(StringTokenizer tokenizer, List<String> variables,
      List<String> constants) {
    String token = tokenizer.nextToken();
    if (token.indexOf('x') != -1) {
      variables.add(token);
      return false;
    }
    if ((token.contains("-") || token.contains("+")) && token.length() == 1) {
      if (tokenizer.hasMoreTokens()) {
        token += tokenizer.nextToken();
        if (token.contains("x")) {
          variables.add(token);
        }
        else {
          constants.add(token);
        }
      }
      else {
        throw new IllegalArgumentException("Invalid equation: missing operand.");
      }
    }
    else if ((token.contains("-") || token.contains("+")) && token.length() > 1) {
      constants.add(token);
    }
    else if (!token.contains("=") && token.length() >= 1) {
      constants.add(token);
    }
    else if (token.contains("=")) {
      return true;
    }
    return false;
  }

  /**
   * Returns the constant that is in front of a variable.
   * 
   * @param variable An operand whose constant this method will return.
   * @return The constant that is in front of the given variable.
   */
  private static int extractConstant(String variable) {
    if (variable.contains("+x") || (variable.contains("x") && variable.length() == 1)) {
      return 1;
    }
    else if (variable.contains("-x")) {
      return -1;
    }
    else if ((variable.contains("-") || variable.contains("+")) && variable.contains("x")) {
      if (variable.contains("+")) {
        return Integer.parseInt(variable.substring(1, variable.indexOf('x')));
      }
      return Integer.parseInt(variable.substring(0, variable.indexOf('x')));
    }
    else if (variable.contains("x")) {
      return Integer.parseInt(variable.substring(0, variable.indexOf('x')));
    }
    else {
      throw new IllegalArgumentException("Invalid string: " + variable);
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing one-variable linear equations to solve.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    LinearEquationSolver solver = new LinearEquationSolver();
    solver.setLines(KataUtils.readLines(args[0]));

    if (solver.getLines() != null) {
      solver.processLines();
    }
  }

}
