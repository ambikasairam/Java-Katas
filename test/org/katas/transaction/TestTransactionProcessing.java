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
package org.katas.transaction;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for the {@link TransactionProcessing} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestTransactionProcessing {

  private TransactionProcessing tp;
  private List<String> lines;

  /**
   * Performs initialization before a test.
   */
  @Before
  public void init() {
    tp = new TransactionProcessing();
    lines = new ArrayList<>();
  }

  /**
   * Tests the {@link TransactionProcessing#process()} method with a valid transaction.
   */
  @Test
  public void testValidTransaction() {
    lines.add("111Cash");
    lines.add("211Accounts Payable");
    lines.add("000No such account");
    lines.add("100111 12345");
    lines.add("100211 -12345");
    lines.add("000000 0");

    tp.setLines(lines);
    assertTrue("should be empty", tp.process().isEmpty());
  }

  /**
   * Tests the {@link TransactionProcessing#process()} method with an invalid transaction.
   */
  @Test
  public void testInvalidTransaction() {
    lines.add("111Cash");
    lines.add("211Accounts Payable");
    lines.add("000No such account");
    lines.add("100111 12345");
    lines.add("100211 -12344");
    lines.add("000000 0");

    tp.setLines(lines);
    String results = tp.process();
    if (results.isEmpty()) {
      fail("Expected some results");
    }
    if (!results.contains("Out of Balance")) {
      fail("Expected a transaction to be out of balance.");
    }
    String[] s = results.split("\n");
    for (String line : s) {
      if (line.contains("Out of Balance")) {
        assertTrue("should end with 0.01", line.endsWith("0.01"));
      }
    }
  }

}
