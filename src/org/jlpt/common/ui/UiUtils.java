package org.jlpt.common.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.jlpt.client.main.AddEntryAction;
import org.jlpt.client.main.AddKeyListener;
import org.jlpt.client.main.ClientMain;
import org.jlpt.client.main.EditEntryAction;
import org.jlpt.client.main.EditKeyListener;
import org.jlpt.client.main.JlptEntryDialogBox;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.utils.Validator;

/**
 * Contains utility methods for modifying UI components.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class UiUtils {

  /** Do not instantiate this class. */
  private UiUtils() {
    // Empty constructor.
  }

  /**
   * Centers a component in the center and on top of its parent.
   * 
   * @param parent The parent of the component to center.
   * @param child The component to center.
   */
  public static void centerComponentOnParent(JFrame parent, JFrame child) {
    Validator.checkNull(parent);
    Validator.checkNull(child);

    Point topLeft = parent.getLocationOnScreen();
    Dimension parentSize = parent.getSize();

    Dimension childSize = child.getSize();

    int x, y;
    if (parentSize.width > childSize.width) {
      x = ((parentSize.width - childSize.width) / 2) + topLeft.x;
    }
    else {
      x = topLeft.x;
    }

    if (parentSize.height > childSize.height) {
      y = ((parentSize.height - childSize.height) / 2) + topLeft.y;
    }
    else {
      y = topLeft.y;
    }

    child.setLocation(x, y);
  }

  /**
   * Sets the ESC key to close the frame that is currently in focus.
   * 
   * @param frame The frame that will be closed when the ESC key is closed.
   */
  public static void setEscKey(final JFrame frame) {
    KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
    @SuppressWarnings("serial")
    Action escapeAction = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
      }
    };
    frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(escapeKeyStroke, "ESCAPE");
    frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
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
    editEntryDialogBox.setOkButtonAction(new EditEntryAction(editEntryDialogBox));
    editEntryDialogBox.setOkButtonText("Update");
    editEntryDialogBox.setKeyListener(new EditKeyListener(editEntryDialogBox, entry));
    UiUtils.centerComponentOnParent(clientMain.getClientMainFrame(), editEntryDialogBox);
    editEntryDialogBox.setVisible(true);
  }

}
