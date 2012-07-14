package org.jlpt.client.table;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

/**
 * A custom JTable for the JLPT Study program.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptTable extends JTable {

  /**
   * Creates a JlptTable instance.
   * @param model The table model that contains Japanese words and their English meanings.
   */
  public JlptTable(JlptTableModel model) {
    super(model);

    setShowVerticalLines(false);
    addMouseListener(new MouseInputAdapter() {

      @Override
      public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
          JlptMenu menu = new JlptMenu(JlptTable.this);
          menu.show(JlptTable.this, event.getX(), event.getY());
        }
      }

    });
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

}