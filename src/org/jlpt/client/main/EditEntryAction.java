package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.utils.Validator;

/**
 * An action that will update an already existing entry in the database when the user clicks on the
 * OK button.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class EditEntryAction extends AbstractAction {

  private final JlptEntryDialogBox dialogBox;

  /**
   * Creates a new EditEntryAction.
   * 
   * @param dialogBox The dialog box to which to add this action.
   */
  public EditEntryAction(JlptEntryDialogBox dialogBox) {
    Validator.checkNull(dialogBox);

    this.dialogBox = dialogBox;
  }

  /** {@inheritDoc} */
  @Override
  public void actionPerformed(ActionEvent event) {
    String jword = this.dialogBox.getJwordText();
    String reading = this.dialogBox.getReadingText();
    String engMeaning = this.dialogBox.getEngMeaningText();
    JapaneseEntry entry = new JapaneseEntry(jword, reading, engMeaning);
    try {
      this.dialogBox.getDbManager().updateEntry(entry);
    }
    catch (EntryDoesNotExistException e) {
      // TODO: Add logger. Show popup message. Then return.

      return;
    }
    try {
      this.dialogBox.getDbManager().save();
    }
    catch (IOException e) {
      // TODO: Add logger.
      System.err.println(e);
    }
    this.dialogBox.getClientMainFrame().updateTable();
    WindowEvent windowClosing = new WindowEvent(this.dialogBox, WindowEvent.WINDOW_CLOSING);
    this.dialogBox.dispatchEvent(windowClosing);
    // TODO: Programmically select the entry that was just updated.
  }

}
