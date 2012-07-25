package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import org.jlpt.common.db.EntryAlreadyExistsException;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.common.utils.Validator;

/**
 * An action that will add an entry to the database when the user clicks on the OK button.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class AddEntryAction extends AbstractAction {

  private static final Logger LOGGER = Logger.getGlobal();

  private final JlptEntryDialogBox dialogBox;

  /**
   * Creates a new AddEntryAction.
   * 
   * @param dialogBox The dialog box to which to add this action.
   */
  public AddEntryAction(JlptEntryDialogBox dialogBox) {
    Validator.checkNull(dialogBox);

    this.dialogBox = dialogBox;
  }

  /** {@inheritDoc} */
  @Override
  public void actionPerformed(ActionEvent event) {
    try {
      this.dialogBox.getDbManager().addEntry(this.dialogBox.getUpdatedEntry());
    }
    catch (EntryAlreadyExistsException | IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      // TODO: Show popup message.
      return;
    }
    try {
      this.dialogBox.getDbManager().save();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      // TODO: Show popup message.
      return;
    }
    this.dialogBox.getClientMainFrame().updateTable();
    UiUtils.closeFrame(this.dialogBox);
  }

}
