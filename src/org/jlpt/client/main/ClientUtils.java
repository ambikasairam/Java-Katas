package org.jlpt.client.main;

import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
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
  public static void displayEditEntryDialogBox(DbManager databaseManager, ClientMain clientMain,
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

}
