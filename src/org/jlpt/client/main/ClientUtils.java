package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.common.utils.Validator;

/**
 * Utility methods for the client UI.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class ClientUtils {

  /** Do not instantiate this class. */
  private ClientUtils() {
    // Empty constructor.
  }

  /**
   * Displays the Add New Entry dialog box.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   */
  public static void displayAddEntryDialogBox(DbManager databaseManager, ClientMain clientMain) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);

    JlptEntryDialogBox addEntryDialogBox = new JlptEntryDialogBox(databaseManager, clientMain);
    addEntryDialogBox.setTitle("Add New Entry");
    addEntryDialogBox.setOkButtonAction(new AddEntryAction(addEntryDialogBox));
    addEntryDialogBox.setOkButtonText("Add");
    addEntryDialogBox.setKeyListener(new AddKeyListener(addEntryDialogBox));
    UiUtils.centerComponentOnParent(clientMain.getClientMainFrame(), addEntryDialogBox);
    addEntryDialogBox.setVisible(true);
  }

  /**
   * Displays the Edit Entry dialog box.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @param entry The entry to be updated.
   */
  private static void displayEditEntryDialogBox(DbManager databaseManager, ClientMain clientMain,
      JapaneseEntry entry) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);
    Validator.checkNull(entry);

    JlptEntryDialogBox editEntryDialogBox = new JlptEntryDialogBox(databaseManager, clientMain);
    editEntryDialogBox.setTitle("Edit Entry");
    editEntryDialogBox.setTextFields(entry);
    editEntryDialogBox.setOkButtonAction(new EditEntryAction(editEntryDialogBox, clientMain
        .getTable()));
    editEntryDialogBox.setOkButtonText("Update");
    editEntryDialogBox.setKeyListener(new EditKeyListener(editEntryDialogBox, entry));
    UiUtils.centerComponentOnParent(clientMain.getClientMainFrame(), editEntryDialogBox);
    editEntryDialogBox.setVisible(true);
  }

  /**
   * Returns the action to refresh the table.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @return The action to refresh the table.
   */
  public static ActionListener getRefreshTableAction(final DbManager databaseManager,
      final ClientMain clientMain) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);

    return new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        clientMain.updateTable(databaseManager.getEntries());
      }

    };
  }

  /**
   * Returns the action to add a new entry to the database.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @return The action to add a new entry to the database.
   */
  public static ActionListener getAddNewEntryAction(final DbManager databaseManager,
      final ClientMain clientMain) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);

    return new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        ClientUtils.displayAddEntryDialogBox(databaseManager, clientMain);
      }

    };
  }

  /**
   * Returns the action to edit the selected entry.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @return The action to edit the selected entry.
   */
  public static ActionListener getEditSelectedEntryAction(final DbManager databaseManager,
      final ClientMain clientMain) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);

    return new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        ClientUtils.displayEditEntryDialogBox(databaseManager, clientMain,
            clientMain.getSelectedEntry());
      }

    };
  }

  /**
   * Returns the action to remove the selected entry from the database.
   * 
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @return The action to remove the selected entry from the database.
   */
  public static ActionListener getRemoveSelectedEntryAction(final DbManager databaseManager,
      final ClientMain clientMain) {
    Validator.checkNull(databaseManager);
    Validator.checkNull(clientMain);

    return new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        displayRemoveEntryConfirmDialogBox("Remove Selected Entry", databaseManager, clientMain,
            clientMain.getSelectedEntry());
      }

    };
  }

  /**
   * Displays the Remove Entry confirm dialog box.
   * 
   * @param title The title of the dialog box.
   * @param databaseManager The database manager.
   * @param clientMain The client application.
   * @param selectedEntry The selected entry.
   */
  private static void displayRemoveEntryConfirmDialogBox(String title, DbManager databaseManager,
      ClientMain clientMain, JapaneseEntry selectedEntry) {
    Validator.checkNotEmptyString(title);

    String msg = "Are you sure you want to delete the following entry from the database?\n\n";
    msg += selectedEntry.getEntryAsString("   ") + "\n\n";
    int option =
        JOptionPane.showConfirmDialog(clientMain.getClientMainFrame(), msg, title,
            JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.NO_OPTION) {
      return;
    }
    try {
      databaseManager.removeEntry(selectedEntry);
    }
    catch (EntryDoesNotExistException e) {
      // TODO: Add logger.
      System.err.println("Unable to remove selected entry from database: " + selectedEntry);
      return;
    }
    msg = "   Successfully removed " + selectedEntry.getJword() + " from the database.";
    clientMain.getStatusLabel().setText(msg);
    clientMain.updateTable();
  }

}
