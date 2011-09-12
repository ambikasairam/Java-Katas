package org.katas;

import java.util.List;

/**
 * This program will form double leading crosses if common letters are found in each pair of
 * vertical and horizontal words.
 * 
 * @author BJ Peter DeLaCruz
 */
public class WordCross extends Kata {

  /** Number of spaces between each horizontal word. */
  private static final int NUM_SPACES = 3;

  /**
   * Constructs a new WordCross object.
   */
  public WordCross() {
    // Empty constructor.
  }

  /**
   * Makes double leading crosses with the words found on each line if each line contains four and
   * only four words, and also if there are common letters in each pair of vertical and horizontal
   * words.
   */
  @Override
  public void processLines() {
    while (!this.getLines().isEmpty()) {
      String line = this.getLines().remove(0);
      if ("#".equals(line)) { // EOF
        return;
      }

      List<String> strings = KataUtils.stringToList(line);
      if (strings.size() != 4) {
        System.err.print("Line does not contain four and only four words: ");
        System.err.println(line);
        continue;
      }

      // positionNumbers is used to keep track of indices at which common letters are found.
      // positionNumbers[0] contains the index for the first horizontal word.
      // positionNumbers[1] contains the index for the second horizontal word.
      // positionNumbers[2] contains the index for the first vertical word.
      // positionNumbers[3] contains the index for the second vertical word.
      int[] positionNumbers = new int[4];

      // Process the next line since no double leading crosses could be made.
      if (!areCommonCharsFound(strings, positionNumbers)) {
        continue;
      }

      printDoubleLeadingCrosses(strings, positionNumbers);
    }
  }

  /**
   * For each pair of vertical and horizontal words, finds the first common letter in each pair. The
   * letter that is found is located as near as possible towards the beginning of the horizontal
   * word.
   * 
   * @param strings The list of four words.
   * @param positionNumbers The integer array containing indices of common letters found.
   * @return True if common letters are found in <span style="text-decoration:underline">both</span>
   * pairs, false otherwise.
   */
  private boolean areCommonCharsFound(List<String> strings, int[] positionNumbers) {
    int hPos = 0;
    int vPos = 2;

    List<Character> horizontalLetters;
    List<Character> verticalLetters;

    boolean isCommonCharFound = false;
    for (int index = 0; index < strings.size(); index += 2) {
      horizontalLetters = KataUtils.getChars(strings.get(index));
      verticalLetters = KataUtils.getChars(strings.get(index + 1));

      isCommonCharFound = false;
      for (Character c : horizontalLetters) {
        if (verticalLetters.contains(c)) {
          positionNumbers[hPos++] = horizontalLetters.indexOf(c) + 1;
          positionNumbers[vPos++] = verticalLetters.indexOf(c) + 1;
          isCommonCharFound = true;
          break;
        }
      }

      if (!isCommonCharFound) {
        System.out.println("Unable to make double leading crosses.\n");
        break;
      }
    }

    return isCommonCharFound;
  }

  /**
   * Prints double leading crosses if common letters are found in each pair of vertical and
   * horizontal words.
   * 
   * @param strings The list of four words.
   * @param positionNumbers The integer array containing indices of common letters found.
   */
  private void printDoubleLeadingCrosses(List<String> strings, int[] positionNumbers) {
    int numSpacesBetweenVerticalWords =
        strings.get(0).length() - positionNumbers[0] + NUM_SPACES + positionNumbers[1] - 1;
    int verticalDistanceBetweenFirstLetters = positionNumbers[2] - positionNumbers[3];

    List<Character> firstVerticalWord = KataUtils.getChars(strings.get(1));
    List<Character> secondVerticalWord = KataUtils.getChars(strings.get(3));

    for (int index = 0, secondVerticalWordIndex = 0;; index++) {
      boolean isFirstVerticalWordPrinted = index >= firstVerticalWord.size();
      boolean isSecondVerticalWordPrinted = secondVerticalWordIndex >= secondVerticalWord.size();
      if (isFirstVerticalWordPrinted && isSecondVerticalWordPrinted) {
        System.out.println();
        break;
      }
      // Print the horizontal words once the index containing the common letter in both first
      // vertical and first horizontal words is reached.
      if (index == positionNumbers[2] - 1) {
        System.out.print(strings.get(0));
        // Print spaces between the two horizontal words.
        for (int pos = 0; pos < NUM_SPACES; pos++) {
          System.out.print(" ");
        }
        System.out.println(strings.get(2));
        secondVerticalWordIndex++; // Skip to the next letter in the second vertical word.
        continue;
      }
      // Print spaces before printing a letter in the first vertical word.
      for (int pos = 0; pos < positionNumbers[0] - 1; pos++) {
        System.out.print(" ");
      }
      if (index < firstVerticalWord.size()) {
        System.out.print(firstVerticalWord.get(index));
      }
      // There are no more letters in the first vertical word to print, so print spaces
      // instead if there are more letters in the second vertical word to print.
      else {
        System.out.print(" ");
      }
      // Print spaces between the two vertical words.
      for (int pos = 0; pos < numSpacesBetweenVerticalWords; pos++) {
        System.out.print(" ");
      }
      // Once the vertical distance between the current letter in the first vertical word and the
      // first letter in the second vertical word is zero, i.e. index is now equal to the vertical
      // distance between the two first letters, start printing the letters in the second vertical
      // word. Otherwise, just print a newline character.
      if (index >= verticalDistanceBetweenFirstLetters) {
        if (secondVerticalWordIndex < secondVerticalWord.size()) {
          System.out.println(secondVerticalWord.get(secondVerticalWordIndex++));
        }
        else {
          System.out.println();
        }
      }
      else {
        System.out.println();
      }
    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing lines of four words each.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    WordCross wordCross = new WordCross();
    wordCross.setLines(KataUtils.readLines(args[0]));

    if (wordCross.getLines() != null) {
      wordCross.processLines();
    }
  }
}
