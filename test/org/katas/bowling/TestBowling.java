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
package org.katas.bowling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * JUnit tests for the {@link Bowling} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestBowling {

  /**
   * Tests scores that are valid.
   */
  @Test
  public void testValidScores() {
    int score = Bowling.getScore("XXXXXXXXXXXX");
    assertEquals("should be 300", 300, score);

    score = Bowling.getScore("9-9-9-9-9-9-9-9-9-9-");
    assertEquals("should be 90", 90, score);

    score = Bowling.getScore("5/5/5/5/5/5/5/5/5/5/5");
    assertEquals("should be 150", 150, score);

    score = Bowling.getScore("11111111111111111111");
    assertEquals("should be 20", 20, score);
  }

  /**
   * Tests scores that are invalid.
   */
  @Test
  public void testInvalidScores() {
    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/5/5-");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/X/5/5");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("5/5/5/5/5/5/5/5/5/555");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Bowling.getScore("555/5/5/5/5/5/5/5/5-");
      fail();
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }
  }

}
