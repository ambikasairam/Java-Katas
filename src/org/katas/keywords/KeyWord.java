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
package org.katas.keywords;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.katas.common.Kata;
import org.katas.common.KataEnums;
import org.katas.common.KataUtils;

/**
 * Given a file containing profiles and titles, this program determines which titles satisfy each
 * profile.
 * 
 * @author BJ Peter DeLaCruz
 */
public class KeyWord extends Kata {

  private final List<String> profiles;
  private final List<String> titles;
  private final List<String> results;
  private final Pattern pattern;

  /**
   * Creates a new Keywords object.
   */
  public KeyWord() {
    profiles = new ArrayList<String>();
    titles = new ArrayList<String>();
    results = new ArrayList<String>();
    // Regular expression for all non-alphabetic characters, excluding whitespace characters.
    pattern = Pattern.compile("[^a-zA-Z\\s]+");
  }

  /**
   * Reads in a file that contains profiles and titles, and determines which titles satisfy each
   * profile.
   */
  @SuppressWarnings("unchecked")
  @Override
  public void processLines() {
    processFile();

    for (String profile : this.profiles) {
      List<String> profileTokens =
          (List<String>) KataUtils.createList(profile, " ", KataEnums.STRING);

      int threshold = -1;
      try {
        threshold = Integer.parseInt(profileTokens.get(0)) + 1;
        profileTokens.remove(0);
      }
      catch (NumberFormatException e) {
        System.err.println("Invalid argument for threshold: " + profileTokens.get(0));
        continue;
      }

      String result = this.profiles.indexOf(profile) + ": ";
      StringBuffer buffer = new StringBuffer(result);
      for (String title : this.titles) {
        // Replace all non-alphabetic characters, excluding whitespace characters, with an empty
        // string.
        String newTitle = this.pattern.matcher(title).replaceAll("");

        List<String> titleTokens =
            (List<String>) KataUtils.createList(newTitle, " ", KataEnums.STRING);

        if (processTitle(threshold, profileTokens, titleTokens)) {
          result = (this.titles.indexOf(title) + 1) + ",";
          buffer.append(result);
        }
      }

      this.results.add(buffer.toString().substring(0, buffer.toString().length() - 1));
    }

    for (String result : this.results) {
      System.out.println(result);
    }
  }

  /**
   * Gets all of the profiles and titles found in a file.
   */
  private void processFile() {
    while (!this.lines.isEmpty()) {
      String line = this.lines.remove(0);
      // This line is a profile.
      if (line.contains("P:")) {
        line = line.replace("P:", "").toLowerCase();
        this.profiles.add(line);
      }
      // This line is a title.
      else if (line.contains("T:")) {
        line = line.replace("T:", "").replace("|", "").toLowerCase();
        this.titles.add(line);
      }
      else {
        line = line.replace("|", "").toLowerCase(); // The | marks the end of a title.
        this.titles.set(this.titles.size() - 1, this.titles.get(this.titles.size() - 1) + line);
      }
    }
  }

  /**
   * Given a threshold value, a profile, and a title, returns true if two keywords appear in the
   * title and also if the number of words between the two is not greater than the threshold value,
   * false otherwise.
   * 
   * @param threshold The maximum number of words that can appear between two keywords.
   * @param profileTokens The list of keywords.
   * @param titleTokens The title.
   * @return True if two keywords appear in the title and also if the number of words between the
   * two is not greater than the threshold value, false otherwise.
   */
  private boolean processTitle(int threshold, List<String> profileTokens,
      List<String> titleTokens) {
    for (int index = 0; index < profileTokens.size(); index += 2) {
      String firstProfileToken = profileTokens.get(index);
      String secondProfileToken = profileTokens.get(index + 1);
      int firstIndex = titleTokens.indexOf(firstProfileToken);
      int secondIndex = titleTokens.indexOf(secondProfileToken);
      int distance = Math.abs(firstIndex - secondIndex);
      if (firstIndex != -1 && secondIndex != -1 && distance <= threshold) {
        return true;
      }
    }
    return false;
  }

  /**
   * The main program; given the name of a file, extracts all of the lines in the file and then
   * processes them.
   * 
   * @param args Name of the file containing profiles and titles.
   */
  public static void main(String... args) {
    if (args.length != 1) {
      System.err.println("Need filename.");
      return;
    }

    KeyWord keywords = new KeyWord();
    keywords.setLines(KataUtils.readLines(args[0]));

    if (keywords.getLines() != null) {
      keywords.processLines();
    }
  }
}
