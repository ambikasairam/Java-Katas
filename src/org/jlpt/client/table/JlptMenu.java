package org.jlpt.client.table;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import org.jlpt.common.utils.Validator;

/**
 * A popup menu that is displayed when the user right-clicks on an entry in the table.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptMenu extends JPopupMenu {

  /**
   * Creates a new JlptMenu instance.
   * 
   * @param table The table that contains the entries of Japanese words and English meanings.
   */
  public JlptMenu(JTable table) {
    super();
    Validator.checkNull(table);

    add(new ExportAction(table, this));

    // TODO: Add a Copy Entry to Clipboard menu item later.

  }

}
