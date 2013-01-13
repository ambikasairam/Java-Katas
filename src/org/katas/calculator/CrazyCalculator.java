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
package org.katas.calculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.katas.common.Kata;
import com.bpd.utils.validation.Validator;

/**
 * A calculator that uses custom operators, and precedence and associativity rules.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/354_Crazy_Calculator.pdf">Crazy
 * Calculator</a>
 */
final class CrazyCalculator extends Kata {

  private static final char LEFT_ASSOC = 'L';
  private static final char RIGHT_ASSOC = 'R';

  private static final String OPERATORS = "~@#$%^&*()_+=-{}[]:;|<>,.?/";

  private final List<String> results = new ArrayList<>();

  /**
   * Represents a rule for this calculator, which contains an operator, a crazy operator, and
   * precedence and associativity for the operator.
   * 
   * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
   */
  private class Rule implements Comparable<Rule> {

    private final char op;
    private final char crazyOp;
    private final int precedence;
    private final char associativity;
    private final int lastPosition;

    /**
     * Creates a new Rule.
     * 
     * @param op The operator.
     * @param crazyOp The crazy operator.
     * @param precedence The precedence of the operator.
     * @param associativity The associativity of the operator.
     * @param lastPosition The last position that the given crazy operator appears in a string.
     */
    Rule(char op, char crazyOp, int precedence, char associativity, int lastPosition) {
      this.op = op;
      if (!OPERATORS.contains(crazyOp + "")) {
        throw new IllegalArgumentException("Not a valid crazy operator: " + crazyOp);
      }
      this.crazyOp = crazyOp;
      this.precedence = precedence;
      if (associativity != LEFT_ASSOC && associativity != RIGHT_ASSOC) {
        throw new IllegalArgumentException("Not a valid associativity: " + associativity);
      }
      this.associativity = associativity;
      this.lastPosition = lastPosition;
    }

    /** @return The operator. */
    public char getOp() {
      return op;
    }

    /** @return The crazy operator. */
    public char getCrazyOp() {
      return crazyOp;
    }

    /** @return The associativity of the operator. */
    public char getAssociativity() {
      return associativity;
    }

    /**
     * Sets the last position that the given crazy operator appears in a string.
     * 
     * @param lastPosition The last position that the given crazy operator appears in a string.
     * @return A new Rule with a new value for the last position.
     */
    public Rule setLastPosition(int lastPosition) {
      return new Rule(op, crazyOp, precedence, associativity, lastPosition);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
      if (!(object instanceof Rule)) {
        return false;
      }
      Rule rule = (Rule) object;
      return op == rule.op && crazyOp == rule.crazyOp && precedence == rule.precedence
          && associativity == rule.associativity && lastPosition == rule.lastPosition;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return Objects.hash(op, crazyOp, precedence, associativity, lastPosition);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return op + "" + crazyOp + (precedence + "") + associativity
          + ((lastPosition == -1) ? "" : " " + lastPosition);
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(Rule rule) {
      int result = Integer.compare(precedence, rule.precedence);
      return result == 0 ? Integer.compare(lastPosition, rule.lastPosition) : result;
    }

  }

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    if (lines.size() < 2) {
      return;
    }
    int numCases = Integer.parseInt(lines.remove(0));
    lines.remove(0);
    for (int index = 0; index < numCases; index++) {
      Map<Character, Rule> rulesMap = new TreeMap<>();
      results.clear();
      for (int idx = 0; idx < 4; idx++) {
        String line = lines.remove(0);
        if (line.isEmpty()) {
          return;
        }
        if (line.length() != 4) {
          throw new IllegalArgumentException("Line doesn't contain four characters.");
        }
        int precedence = Integer.parseInt(line.charAt(2) + "");
        Rule newRule = new Rule(line.charAt(0), line.charAt(1), precedence, line.charAt(3), -1);
        Rule oldRule = rulesMap.put(newRule.getCrazyOp(), newRule);
        if (oldRule != null) {
          throw new IllegalArgumentException("Rule already defined.");
        }
      }
      while (!lines.isEmpty()) {
        String oldLine = lines.remove(0);
        if (oldLine.isEmpty()) {
          break;
        }
        String line = replaceCrazyOps(rulesMap, oldLine);
        List<Rule> rulesList = new ArrayList<>();
        for (Entry<Character, Rule> entry : rulesMap.entrySet()) {
          int position = oldLine.lastIndexOf(entry.getValue().getCrazyOp());
          rulesList.add(entry.getValue().setLastPosition(position));
        }
        Collections.sort(rulesList);
        Collections.reverse(rulesList);
        System.out.print(line + " = ");
        for (Rule rule : rulesList) {
          line = calculate(line, rule);
        }
        System.out.println(line);
        System.out.println();
        results.add(line);
      }
    }
  }

  /**
   * Replaces all crazy operators with regular operators (i.e. '+', '-', '*', and '/').
   * 
   * @param rules The map of arithmetic rules.
   * @param line The line that contains crazy operators that need to be replaced.
   * @return A line with regular operators.
   */
  private static String replaceCrazyOps(Map<Character, Rule> rules, String line) {
    Validator.checkNull(rules);
    Validator.checkEmptyString(line);

    char[] newLine = line.toCharArray();
    for (int index = 0; index < newLine.length; index++) {
      if (rules.get(newLine[index]) == null) {
        continue;
      }
      if (index == 0 && newLine[index] == '-') {
        continue;
      }
      try {
        if (newLine[index] == '-') {
          Integer.parseInt(newLine[index - 1] + "");
        }
      }
      catch (NumberFormatException nfe) {
        // For example: 1--2
        continue;
      }
      newLine[index] = rules.get(newLine[index]).getOp();
    }
    return new String(newLine);
  }

  /**
   * Calculates the given equation.
   * 
   * @param line The line that represents an equation.
   * @param rule An arithmetic rule.
   * @return The original line if the given operator is not found in the line, or a partially or
   * fully solved equation.
   */
  private static String calculate(String line, Rule rule) {
    Validator.checkEmptyString(line);
    Validator.checkNull(rule);

    String newLine = line;
    int position;

    while (true) {
      switch (rule.getAssociativity()) {
      case LEFT_ASSOC:
        position = newLine.indexOf(rule.getOp());
        break;
      case RIGHT_ASSOC:
        position = newLine.lastIndexOf(rule.getOp());
        break;
      default:
        throw new IllegalArgumentException("Invalid argument: " + rule.getAssociativity());
      }

      if (position < 0) {
        return newLine;
      }

      char[] chars = newLine.toCharArray();

      // Get value for left operand
      int leftmostPosition = position;
      if (!isNumeric(chars, leftmostPosition - 1)) {
        return newLine;
      }
      int end = leftmostPosition - 1;
      while (isNumeric(chars, --leftmostPosition)) {
        ;
      }
      int start = leftmostPosition + 1;
      StringBuffer buffer = new StringBuffer();
      for (int index = start; index < end + 1; index++) {
        buffer.append(chars[index]);
      }
      int left = Integer.parseInt(buffer.toString());

      // Get value for right operand
      int rightmostPosition = position;
      start = end = rightmostPosition + 1;
      while (isNumeric(chars, end)) {
        end++;
      }
      end--;
      buffer = new StringBuffer();
      for (int index = start; index < end + 1; index++) {
        buffer.append(chars[index]);
      }
      int right = Integer.parseInt(buffer.toString());

      String oldSubstring = left + "" + rule.getOp() + right;
      int result;

      switch (rule.getOp()) {
      case '+':
        result = left + right;
        break;
      case '-':
        result = left - right;
        break;
      case '*':
        result = left * right;
        break;
      case '/':
        if (right == 0) {
          throw new IllegalArgumentException("Attempted to divide by 0.");
        }
        result = left / right;
        break;
      default:
        throw new IllegalArgumentException("Invalid operator: " + rule.getOp());
      }

      switch (rule.getAssociativity()) {
      case LEFT_ASSOC:
        newLine = StringUtils.replace(newLine, oldSubstring, result + "");
        break;
      case RIGHT_ASSOC:
        newLine = com.bpd.utils.StringUtils.replaceLast(newLine, oldSubstring, result + "");
        break;
      default:
        throw new IllegalArgumentException("Invalid argument: " + rule.getAssociativity());
      }

      try {
        Integer.parseInt(newLine);
        return newLine;
      }
      catch (NumberFormatException nfe) {
        continue;
      }

    }
  }

  /**
   * Returns true if the given array of characters represents a number, or false otherwise.
   * 
   * @param chars The array of characters.
   * @param position The position in the array.
   * @return True if the given array of characters represents a number; or false if position < 0,
   * position >= chars.length, or the given array of characters does not represent a number.
   */
  private static boolean isNumeric(char[] chars, int position) {
    Validator.checkNull(chars);

    if (position < 0 || position >= chars.length) {
      return false;
    }
    if (position == 0 && chars[position] == '-') {
      return true;
    }
    try {
      if (position - 1 >= 0) {
        Integer.parseInt(chars[position - 1] + "");
      }
    }
    catch (NumberFormatException nfe) {
      if (chars[position] == '-') {
        return true;
      }
    }
    return chars[position] != '+' && chars[position] != '-' && chars[position] != '*'
        && chars[position] != '/';
  }

  /** @return A copy of the results for one set of calculations. */
  public List<String> getResults() {
    return new ArrayList<>(results);
  }

}
