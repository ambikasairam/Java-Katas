package org.katas.treelevel;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * JUnit tests for the {@link TreeLevel} class.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TestTreeLevel {

  /**
   * Tests the {@link TreeLevel#getLevelOrder(String)} method.
   */
  @Test
  public void testLevelOrder() {
    String tree = "(11,LL) (7,LLL) (8,R) (5,) (4,L) (13,RL) (2,LLR) (1,RRR) (4,RR) ()";
    assertEquals("should be 5 4 8 11 13 4 7 2 1", "5 4 8 11 13 4 7 2 1",
        TreeLevel.getLevelOrder(tree));

    tree = "(5,LLL) (6,LLR) (3,LL) (1,L) (0,) (2,R) (4,RR) (7,RRR) (8,RRL) ()";
    assertEquals("should be 0 1 2 3 4 5 6 8 7", "0 1 2 3 4 5 6 8 7", TreeLevel.getLevelOrder(tree));

    tree = "(5,LLL) (6,LLR) (3,LL) (1,L) (0,LR) (2,R) (4,RR) (7,RRR) (8,RRL) ()";
    assertEquals("should be null", null, TreeLevel.getLevelOrder(tree));
  }

}
