package org.jlpt.common.db;

import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.Validator;

/**
 * A request for adding an entry to the database or removing an entry from the database.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class AddRemoveRequest implements Request {

  private final Commands command;
  private final JapaneseEntry entry;

  /**
   * Creates a new AddRemoveRequest.
   * 
   * @param command The command, either add or remove.
   * @param entry The entry to add or remove.
   */
  public AddRemoveRequest(Commands command, JapaneseEntry entry) {
    Validator.checkNull(command);
    Validator.checkNull(entry);

    this.command = command;
    this.entry = entry;
  }

  /** @return The entry to add or remove. */
  public JapaneseEntry getEntry() {
    return this.entry;
  }

  /** {@inheritDoc} */
  @Override
  public Commands getCommand() {
    return this.command;
  }

}
