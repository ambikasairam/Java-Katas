package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.jlpt.client.table.JlptTable;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.db.StaleEntryException;
import org.jlpt.common.utils.Validator;

/**
 * An action that will update an already existing entry in the database when the user clicks on the
 * OK button.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class EditEntryAction extends AbstractAction {

  private static final Logger LOGGER = Logger.getGlobal();

  private final JlptEntryDialogBox dialogBox;
  private final JlptTable table;

  /**
   * Creates a new EditEntryAction.
   * 
   * @param dialogBox The dialog box to which to add this action.
   * @param table The table that contains all of the entries.
   */
  public EditEntryAction(JlptEntryDialogBox dialogBox, JlptTable table) {
    Validator.checkNull(dialogBox);
    Validator.checkNull(table);

    this.dialogBox = dialogBox;
    this.table = table;
  }

  /** {@inheritDoc} */
  @Override
  public void actionPerformed(ActionEvent event) {
    JapaneseEntry oldEntry = this.table.getEntry(this.dialogBox.getJwordText());
    try {
      this.dialogBox.getDbManager().updateEntry(this.dialogBox.getUpdatedEntry(), oldEntry);
    }
    catch (EntryDoesNotExistException | StaleEntryException | IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      String msg = "Unable to update entry in the database. Reason:\n\n" + e;
      JOptionPane.showMessageDialog(dialogBox, msg, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    try {
      this.dialogBox.getDbManager().save();
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      String msg = "Unable to save entries to the database. Reason:\n\n" + e;
      JOptionPane.showMessageDialog(dialogBox, msg, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    this.dialogBox.getClientMainFrame().updateTable();
    WindowEvent windowClosing = new WindowEvent(this.dialogBox, WindowEvent.WINDOW_CLOSING);
    this.dialogBox.dispatchEvent(windowClosing);
  }

}
