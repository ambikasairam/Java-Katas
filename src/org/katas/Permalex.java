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
package org.katas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * This program reads in strings from a file and determines their positions in the ordered sequence
 * of permutations of their constituent characters.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/153_Permalex.pdf">Permalex</a>
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
  @SuppressWarnings("deprecation")
  @Override
  public void processLines() {
    while (!this.lines.isEmpty()) {

      String line = this.lines.remove(0);
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
