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
package org.katas.minesweeper;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the {@link Minesweeper} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestMinesweeper {

  private Minesweeper minesweeper;
  private static final String ONE = "should be one";
  private static final String TWO = "should be two";
  private static final String EMPTY_ROW = ". . . .";

  /**
   * Creates an empty grid and number of rows and columns in the grid.
   */
  @Before
  public void setUp() {
    this.minesweeper = new Minesweeper();
    this.minesweeper.setNumRows(2);
    this.minesweeper.setNumColumns(4);
  }

  /**
   * Tests the upper left corner in the grid.
   */
  @Test
  public void testUpperLeftCorner() {
    this.minesweeper.addRow("* . . .", 1);
    this.minesweeper.addRow(EMPTY_ROW, 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(1, 1));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 1).getNumSurroundingMines());
  }

  /**
   * Tests the upper right corner in the grid.
   */
  @Test
  public void testUpperRightCorner() {
    this.minesweeper.addRow(". . . *", 1);
    this.minesweeper.addRow(EMPTY_ROW, 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(1, 4));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 4).getNumSurroundingMines());
  }

  /**
   * Tests the lower right corner in the grid.
   */
  @Test
  public void testLowerRightCorner() {
    this.minesweeper.addRow(EMPTY_ROW, 1);
    this.minesweeper.addRow(". . . *", 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 4));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 4).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
  }

  /**
   * Tests the lower left corner in the grid.
   */
  @Test
  public void testLowerLeftCorner() {
    this.minesweeper.addRow(EMPTY_ROW, 1);
    this.minesweeper.addRow("* . . .", 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 1));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 2).getNumSurroundingMines());
  }

  /**
   * Tests the first row in the grid.
   */
  @Test
  public void testFirstRow() {
    this.minesweeper.addRow(". * . *", 1);
    this.minesweeper.addRow(EMPTY_ROW, 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(1, 2));
    this.minesweeper.processMine(this.minesweeper.getSquareAt(1, 4));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 2).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 4).getNumSurroundingMines());
  }

  /**
   * Tests the last row in the grid.
   */
  @Test
  public void testLastRow() {
    this.minesweeper.addRow(EMPTY_ROW, 1);
    this.minesweeper.addRow(". * . *", 2);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 2));
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 4));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 2).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 4).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 1).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
  }

  /**
   * Tests the first column in the grid.
   */
  @Test
  public void testFirstColumn() {
    this.minesweeper.setNumRows(4);
    this.minesweeper.addRow(EMPTY_ROW, 1);
    this.minesweeper.addRow("* . . .", 2);
    this.minesweeper.addRow(EMPTY_ROW, 3);
    this.minesweeper.addRow(". * . .", 4);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 1));
    this.minesweeper.processMine(this.minesweeper.getSquareAt(4, 2));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 2).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(3, 1).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(3, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(4, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(4, 3).getNumSurroundingMines());
  }

  /**
   * Tests the last column in the grid.
   */
  @Test
  public void testLastColumn() {
    this.minesweeper.setNumRows(4);
    this.minesweeper.addRow(". * . .", 1);
    this.minesweeper.addRow(". * . *", 2);
    this.minesweeper.addRow(EMPTY_ROW, 3);
    this.minesweeper.addRow(EMPTY_ROW, 4);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(1, 2));
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 2));
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 4));
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(2, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 2).getNumSurroundingMines());
    assertEquals("Should be 3", 3, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals("Should be 3", 3, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
    assertEquals(TWO, 2, this.minesweeper.getSquareAt(3, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 4).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 4).getNumSurroundingMines());
  }

  /**
   * Tests a square inside the grid.
   */
  @Test
  public void testInsideGrid() {
    this.minesweeper.setNumColumns(3);
    this.minesweeper.setNumRows(3);
    this.minesweeper.addRow(". . .", 1);
    this.minesweeper.addRow(". * .", 2);
    this.minesweeper.addRow(". . .", 3);
    this.minesweeper.processMine(this.minesweeper.getSquareAt(2, 2));
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(1, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(2, 3).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 1).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 2).getNumSurroundingMines());
    assertEquals(ONE, 1, this.minesweeper.getSquareAt(3, 3).getNumSurroundingMines());
  }

}
