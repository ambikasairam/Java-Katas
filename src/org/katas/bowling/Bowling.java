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

/**
 * This class calculates a bowler's final score.
 * 
 * @author BJ Peter DeLaCruz
 */
final class Bowling {

  /**
   * Given a bowling record, this method will calculate a bowler's final score.
   * 
   * @param score Bowling record.
   * @return Final score.
   * @throws IllegalArgumentException If bowling record is invalid.
   */
  public static int getScore(String score) throws IllegalArgumentException {
    if (score.length() > 21 || score.length() < 11) {
      String msg = "Invalid bowling record: invalid bowling record length.";
      throw new IllegalArgumentException(msg);
    }

    char[] scores = score.toCharArray();
    int totalScore = 0;
    int numFrames = 0;
    short counter = 0;

    if (score.length() == 21 && scores[18] != 'X' && scores[19] != '/') {
      String msg = "Invalid bowling record: invalid tenth frame.";
      throw new IllegalArgumentException(msg);
    }

    for (int pos = 0; pos < score.length(); pos++) {
      if (scores[pos] == 'X') {
        if (pos < score.length() - 2) {
          // A slash should not come after an X.
          if (scores[pos + 1] == '/') {
            String msg = "Invalid bowling record: X should not immediately precede /.";
            throw new IllegalArgumentException(msg);
          }
          if (scores[pos + 2] == '/') {
            totalScore += 20;
          }
          else {
            totalScore += 10 + getScore(scores[pos + 1]) + getScore(scores[pos + 2]);
          }
          numFrames++;
        }
      }
      else if (scores[pos] == '/') {
        if (pos < score.length() - 1) {
          totalScore += 10 + getScore(scores[pos + 1]);
          numFrames++;
          counter = 0;
        }
      }
      else {
        // If the bowler didn't get a spare, add the first score to the total score.
        if (pos < score.length() - 1 && scores[pos + 1] != '/') {
          totalScore += getScore(scores[pos]);
        }
        // If the bowler didn't get a spare or strike in the 10th frame, add the second score to
        // the total score.
        else if (pos == score.length() - 1 && getScore(scores[pos - 1]) < 10) {
          totalScore += getScore(scores[pos]);
        }

        // If the bowler didn't knock down all of the pins in the 10th frame, there should not
        // be a score for the bonus throw.
        if (numFrames == 10 && getScore(scores[pos - 1]) < 10 && getScore(scores[pos - 2]) < 10) {
          throw new IllegalArgumentException("Invalid bowling record: invalid tenth frame.");
        }

        // If counter is equal to 2, then the bowler didn't knock down all of the pins on
        // the second try.
        counter++;
        if (counter % 2 == 0 && counter > 0) {
          numFrames++;
          counter = 0;
          if (getScore(scores[pos]) + getScore(scores[pos - 1]) >= 10) {
            String msg = "Invalid bowling record: second score in frame " + numFrames;
            throw new IllegalArgumentException(msg + " should be / or less than first score.");
          }
          // If the bowler got a strike on the first try in the 10th frame but didn't knock down
          // all of the pins on the bonus throw, don't add the last two scores.
          if (numFrames > 10 && pos == score.length() - 1 && scores[pos - 2] == 'X') {
            numFrames--;
            totalScore -= (getScore(scores[pos]) + getScore(scores[pos - 1]));
          }
        }
      }
    }

    if (numFrames != 10) {
      String msg = "Invalid bowling record: number of frames does not equal 10.";
      throw new IllegalArgumentException(msg);
    }

    return totalScore;
  }

  /**
   * Helper method to convert characters into scores.
   * 
   * @param score Character representing score.
   * @return A score.
   */
  private static int getScore(char score) {
    switch (score) {
    case 'X':
      return 10;
    case '/':
      return 10;
    case '-':
      return 0;
    default:
      return Integer.parseInt(Character.toString(score));
    }
  }

  /**
   * Tests the getBowlingScore method above.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    int score = Bowling.getScore("XXXXXXXXX5/5");
    System.out.println("XXXXXXXXX5/5 : " + score);
    score = Bowling.getScore("XX5/XXXXXXXXX");
    System.out.println("XX5/XXXXXXXXX : " + score);
    score = Bowling.getScore("X5-5/5-5/5-5/5-5/54");
    System.out.println("X5-5/5-5/5-5/5-5/54 : " + score);
    score = Bowling.getScore("X5-5/5-5/5-5/5-5/5/5");
    System.out.println("X5-5/5-5/5-5/5-5/5/5 : " + score);
    score = Bowling.getScore("X5-5/5-5/5-5/5-5/X54");
    System.out.println("X5-5/5-5/5-5/5-5/X54 : " + score);
    score = Bowling.getScore("X5-5/5-5/5-5/5-5/X5/");
    System.out.println("X5-5/5-5/5-5/5-5/X5/ : " + score);
    score = Bowling.getScore("X5-5/5-5/5-5/5-5/XX5");
    System.out.println("X5-5/5-5/5-5/5-5/XX5 : " + score);
    score = Bowling.getScore("XXXXX5/XXX5-");
    System.out.println("XXXXX5/XXX5- : " + score);
    score = Bowling.getScore("XXXXXXXXX5-");
    System.out.println("XXXXXXXXX5- : " + score);
  }
}
