package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This program reads in strings from a file and determines their positions in the ordered sequence
 * of permutations of their constituent characters.
 * 
 * @author BJ Peter DeLaCruz
 */
public class Permalex extends Kata {

  /**
   * Creates a new Permalex object.
   */
  public Permalex() {
    // Empty constructor.
  }

  /**
   * For each string in the input file, prints out the position number at which the string is
   * located in the list of sorted permutations of the characters in the string.
   */
  @Override
  public void processLines() {
    while (!this.getLines().isEmpty()) {

      String line = this.getLines().remove(0);
      if ("#".equals(line)) { // EOF
        return;
      }

      List<Character> letters = KataUtils.getChars(line);
      List<String> strings = KataUtils.makeStringsList(new ArrayList<Character>(letters));
      Set<String> set = new HashSet<String>(strings);
      strings.clear();
      strings.addAll(set);
      Collections.sort(strings);

      System.out.println(strings.indexOf(KataUtils.getString(letters)) + 1);

    }
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args The name of the file containing strings of characters to permute.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    Permalex permalex = new Permalex();
    permalex.setLines(KataUtils.readLines(args[0]));

    if (permalex.getLines() != null) {
      permalex.processLines();
    }
  }
}
