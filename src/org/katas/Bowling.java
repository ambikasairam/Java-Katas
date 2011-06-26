package org.katas;

/**
 * Java Kata: Bowling.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class Bowling {

  /**
   * Given a bowling record, determines final score.
   * 
   * @param score Bowling record.
   * @return Final score.
   * @throws IllegalArgumentException If bowling record is invalid.
   */
  public int getBowlingScore(String score) throws IllegalArgumentException {
    if (score.length() > 21 || score.length() < 12) {
      String msg = "Invalid bowling record: invalid length.";
      throw new IllegalArgumentException(msg);
    }

    char[] scores = score.toCharArray();
    int totalScore = 0;
    int numFrames = 0;
    short counter = 0;

    if (score.length() == 21) {
      boolean isValid = true;
      if (scores[18] != 'X' && scores[19] != '/') {
        isValid = false;
      }
      if (!isValid) {
        String msg = "Invalid bowling record: invalid tenth frame.";
        throw new IllegalArgumentException(msg);
      }
    }

    for (int pos = 0; pos < score.length(); pos++) {
      if (scores[pos] == 'X') {
        if (pos < score.length() - 2) {
          // A slash should not come after an X.
          if (scores[pos + 1] == '/') {
            String msg = "Invalid bowling record: X preceding /.";
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
          // If the bowler got a strike on the first try in the 10th frame but didn't knock down
          // all of the pins on the bonus throw, don't add the last two scores.
          if (pos == score.length() - 1 && scores[pos - 2] == 'X') {
            numFrames--;
            totalScore -= (getScore(scores[pos]) + getScore(scores[pos - 1]));
          }
        }
      }
    }

    if (numFrames != 10) {
      String msg = "Invalid bowling record: invalid number of frames.";
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
  private int getScore(char score) {
    if (score == 'X' || score == '/') {
      return 10;
    }
    else if (score == '-') {
      return 0;
    }
    else {
      return Integer.parseInt(Character.toString(score));
    }
  }

  /**
   * Tests the getBowlingScore method above.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    Bowling bowling = new Bowling();
    int score = bowling.getBowlingScore("XXXXXXXXXXXX");
    System.out.println("XXXXXXXXXXXX : " + score);
    score = bowling.getBowlingScore("9-9-9-9-9-9-9-9-9-9-");
    System.out.println("9-9-9-9-9-9-9-9-9-9- : " + score);
    score = bowling.getBowlingScore("5/5/5/5/5/5/5/5/5/5/5");
    System.out.println("5/5/5/5/5/5/5/5/5/5/5 : " + score);
    score = bowling.getBowlingScore("11111111111111111111");
    System.out.println("11111111111111111111 : " + score);
    score = bowling.getBowlingScore("XXXXXXXXX5/5");
    System.out.println("XXXXXXXXX5/5 : " + score);
    score = bowling.getBowlingScore("XX5/XXXXXXXXX");
    System.out.println("XX5/XXXXXXXXX : " + score);
    score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/55");
    System.out.println("X5-5/5-5/5-5/5-5/55 : " + score);
    score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/5/5");
    System.out.println("X5-5/5-5/5-5/5-5/5/5 : " + score);
    score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/X54");
    System.out.println("X5-5/5-5/5-5/5-5/X54 : " + score);
    score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/X5/");
    System.out.println("X5-5/5-5/5-5/5-5/X5/ : " + score);
    score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/XX5");
    System.out.println("X5-5/5-5/5-5/5-5/XX5 : " + score);

    try {
      score = bowling.getBowlingScore("X5-5/5-5/5-5/5-5/555");
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      score = bowling.getBowlingScore("5/5/5/5/5/5/5/5/5/5/5-");
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      score = bowling.getBowlingScore("5/5/5/5/5/5/5/5/5/");
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      score = bowling.getBowlingScore("5/5/5/5/5/5/5/5/X/5/5");
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      score = bowling.getBowlingScore("5/5/5/5/5/5/5/5/5/555");
    }
    catch (IllegalArgumentException e) {
      System.out.println(e);
    }

  }
}
