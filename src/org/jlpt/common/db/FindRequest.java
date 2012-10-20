package org.jlpt.common.db;

import org.jlpt.common.utils.Validator;

/**
 * A request for finding one or more entries in the database using a regular expression pattern.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class FindRequest implements Request {

  private final String regex;

  /**
   * Creates a new FindRequest.
   * 
   * @param regex The regular expression pattern.
   */
  public FindRequest(String regex) {
    Validator.checkNotEmptyString(regex);

    this.regex = regex;
  }

  /** @return The regular expression pattern. */
  public String getRegex() {
    return this.regex;
  }

  /** {@inheritDoc} */
  @Override
  public Commands getCommand() {
    return Commands.FIND;
  }

}
