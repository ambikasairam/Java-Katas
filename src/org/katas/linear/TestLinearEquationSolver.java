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
package org.katas.linear;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the LinearEquationSolver class by solving some test equations.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestLinearEquationSolver {

  private List<String> variablesL;
  private List<String> constantsL;
  private List<String> variablesR;
  private List<String> constantsR;
  private static final String ONE = "Should be 1.";
  private static final String ZERO = "Should be 0.";
  private static final String EQUAL = "Should be equal.";

  /**
   * Sets up the lists.
   */
  @Before
  public void setUp() {
    variablesL = new ArrayList<String>();
    constantsL = new ArrayList<String>();
    variablesR = new ArrayList<String>();
    constantsR = new ArrayList<String>();
  }

  /**
   * Test equation 1. The solution should be -1/2.
   */
  @Test
  public void testEquation1() {
    String equation = "10x +5 -5 + 10 - 5 = 0";
    processLists(equation);
    assertEquals(ONE, 1, this.variablesL.size());
    assertEquals("Should be 4.", 4, this.constantsL.size());
    assertEquals(ZERO, 0, this.variablesR.size());
    assertEquals(ONE, 1, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(-0.5, result));
  }

  /**
   * Test equation 2. The solution should be -1.
   */
  @Test
  public void testEquation2() {
    String equation = "5 +10x -x + 4 + 0 = 0";
    processLists(equation);
    assertEquals("Should be 2.", 2, this.variablesL.size());
    assertEquals("Should be 3.", 3, this.constantsL.size());
    assertEquals(ZERO, 0, this.variablesR.size());
    assertEquals(ONE, 1, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(-1, result));
  }

  /**
   * Test equation 3. There should be infinite solutions.
   */
  @Test
  public void testEquation3() {
    String equation = "5 + x = x + 5";
    processLists(equation);
    assertEquals(ONE, 1, this.variablesL.size());
    assertEquals(ONE, 1, this.constantsL.size());
    assertEquals(ONE, 1, this.variablesR.size());
    assertEquals(ONE, 1, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(Double.POSITIVE_INFINITY, result));
  }

  /**
   * Test equation 4. There should be no solutions.
   */
  @Test
  public void testEquation4() {
    String equation = "1 - 2 + 3 - 4 = 4 - 3 + 2 - 1";
    processLists(equation);
    assertEquals(ZERO, 0, this.variablesL.size());
    assertEquals("Should be 4.", 4, this.constantsL.size());
    assertEquals(ZERO, 0, this.variablesR.size());
    assertEquals("Should be 4.", 4, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(Double.NaN, result));
  }

  /**
   * Test equation 5. The solution should be 3.
   */
  @Test
  public void testEquation5() {
    String equation = "1 - 2 + 3 - 4 + 5 = x";
    processLists(equation);
    assertEquals(ZERO, 0, this.variablesL.size());
    assertEquals("Should be 5.", 5, this.constantsL.size());
    assertEquals(ONE, 1, this.variablesR.size());
    assertEquals(ZERO, 0, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(3.0, result));
  }

  /**
   * Test equation 6. The solution should be -1.
   */
  @Test
  public void testEquation6() {
    String equation = "1 + 2 - 3 + 4 - 5 = x";
    processLists(equation);
    assertEquals(ZERO, 0, this.variablesL.size());
    assertEquals("Should be 5.", 5, this.constantsL.size());
    assertEquals(ONE, 1, this.variablesR.size());
    assertEquals(ZERO, 0, this.constantsR.size());
    double result =
        LinearEquationSolver.solve(this.variablesL, this.constantsL, this.variablesR,
            this.constantsR);
    assertEquals(EQUAL, 0, Double.compare(-1.0, result));
  }

  /**
   * Processes both sides of the equation.
   * 
   * @param equation The equation to solve.
   */
  private void processLists(String equation) {
    StringTokenizer tokenizer = new StringTokenizer(equation);
    while (tokenizer.hasMoreTokens()) {
      if (LinearEquationSolver.processSide(tokenizer, this.variablesL, this.constantsL)) {
        break;
      }
    }
    while (tokenizer.hasMoreTokens()) {
      LinearEquationSolver.processSide(tokenizer, this.variablesR, this.constantsR);
    }
  }

}
