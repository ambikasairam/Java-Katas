package org.jlpt.common.db;

import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.Validator;

/**
 * A request for updating an entry in the database on the server.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class UpdateRequest implements Request {

  private final JapaneseEntry oldEntry;
  private final JapaneseEntry newEntry;

  /**
   * Creates a new UpdateRequest.
   * 
   * @param newEntry The new entry to put in the database.
   * @param oldEntry The old entry to update.
   */
  public UpdateRequest(JapaneseEntry newEntry, JapaneseEntry oldEntry) {
    Validator.checkNull(newEntry);
    Validator.checkNull(oldEntry);

    this.newEntry = newEntry;
    this.oldEntry = oldEntry;
  }

  /** @return The new entry to put in the database. */
  public JapaneseEntry getNewEntry() {
    return this.newEntry;
  }

  /** @return The old entry to update. */
  public JapaneseEntry getOldEntry() {
    return this.oldEntry;
  }

  /** {@inheritDoc} */
  @Override
  public Commands getCommand() {
    return Commands.UPDATE;
  }

}
