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
package org.katas.wrapper;

/**
 * This class inserts line breaks into a string so that no line is longer than a given number.
 * 
 * @author BJ Peter DeLaCruz
 */
public class LineWrapper {

  /**
   * Returns a string that is the same as the given string but with line breaks inserted at the
   * right places so that no line is longer than the column number.
   * 
   * @param str String to modify.
   * @param numColumns Maximum number of characters on a line.
   * @return A string whose lines do not have more than the maximum number of characters specified.
   */
  public static char[] getWrappedString(String str, int numColumns) {
    char[] string = str.toCharArray();
    for (int counter = 0, position = 0; position < str.length(); counter++, position++) {
      if (counter == numColumns) {
        while (string[position] != ' ') {
          position--;
        }
        string[position] = '\n';
        counter = 0;
      }
    }
    return string;
  }

}
