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
package org.katas.common;

import java.util.List;

/**
 * The base class that contains fields and methods used by all katas.
 * 
 * @author BJ Peter DeLaCruz
 */
public abstract class Kata {

  /** The lines read in from a file. */
  protected List<String> lines;

  /**
   * Sets lines read in from a file.
   * 
   * @param lines The lines read in from a file.
   */
  public void setLines(List<String> lines) {
    this.lines = lines;
  }

  /**
   * Gets lines read in from a file.
   * 
   * @return The lines read in from a file.
   */
  public List<String> getLines() {
    return this.lines;
  }

  /** Processes all of the lines in a file. */
  public abstract void processLines();
}
