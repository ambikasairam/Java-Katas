package org.katas.common;

import java.util.List;

/**
 * The base class that contains fields and methods used by all katas.
 * 
 * @author BJ Peter DeLaCruz
 */
public abstract class Kata {

  /** The lines read in from a file. */
  private List<String> lines;

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
