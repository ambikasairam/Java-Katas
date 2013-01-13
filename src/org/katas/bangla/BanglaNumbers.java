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
package org.katas.bangla;

import java.util.ArrayList;
import java.util.List;
import org.katas.common.Kata;
import org.katas.common.KataUtils;

/**
 * This program converts a number into a Bangla number:<br>
 * <br>
 * 10^2 = shata<br>
 * 10^3 = hajar<br>
 * 10^5 = lakh<br>
 * 10^7 = kuti<br>
 * <br>
 * The number to convert cannot be greater than 999,999,999,999,999.
 * 
 * @see <a href="http://www.bjpeterdelacruz.com/files/katas/Bangla_Numbers.pdf">Bangla Numbers</a>
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
final class BanglaNumbers extends Kata {

  private static final long MAX = 999_999_999_999_999L;
  private static final String SHATA = " shata ";
  private static final String HAJAR = " hajar ";
  private static final String LAKH = " lakh ";
  private static final String KUTI = " kuti ";

  /** {@inheritDoc} */
  @Override
  public void processLines() {
    while (!this.lines.isEmpty()) {
      long number = Long.parseLong(this.lines.remove(0));
      if (number > MAX) {
        System.err.println("Error: " + number + " > " + MAX);
        continue;
      }
      char[] numbers = (number + "").toCharArray();

      List<Character> characters = null;
      characters = new ArrayList<Character>();
      for (int index = numbers.length - 1, counter = 1; index > -1; index--, counter++) {
        characters.add(0, numbers[index]);
        if (index == 0) {
          break;
        }
        switch (counter) {
        case 2:
          // fall-through
        case 9:
          insertWord(SHATA, characters);
          break;
        case 3:
          // fall-through
        case 10:
          insertWord(HAJAR, characters);
          break;
        case 5:
          // fall-through
        case 12:
          insertWord(LAKH, characters);
          break;
        case 7:
          // fall-through
        case 14:
          insertWord(KUTI, characters);
          break;
        default:
          // fall-through
        }
      }
      for (Character c : characters) {
        System.out.print(c);
      }
      System.out.println();
    }
  }

  /**
   * Inserts a word in front a list of numbers.
   * 
   * @param word The word to insert.
   * @param characters The list into which to insert the word.
   */
  private void insertWord(String word, List<Character> characters) {
    StringBuffer buffer = new StringBuffer();
    for (Character c : characters) {
      buffer.append(c);
    }
    String tempStr = word + buffer.toString();
    characters.clear();
    List<Character> temp = new ArrayList<Character>();
    for (Character c : tempStr.toCharArray()) {
      temp.add(c);
    }
    characters.addAll(temp);
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    String filename = BanglaNumbers.class.getResource("example.kata").getPath();

    BanglaNumbers banglaNumbers = new BanglaNumbers();
    banglaNumbers.setLines(KataUtils.readLines(filename));

    if (banglaNumbers.getLines() != null) {
      banglaNumbers.processLines();
    }
  }

}
