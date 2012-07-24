package org.jlpt.client.table;

import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.Validator;

/**
 * A custom JTable for the JLPT Study program.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptTable extends JTable {

  private final JlptTableModel model;
  private final JlptMenu menu;

  /**
   * Creates a JlptTable instance.
   * 
   * @param model The table model that contains Japanese words and their English meanings.
   */
  public JlptTable(JlptTableModel model) {
    super(model);
    Validator.checkNull(model);

    this.model = model;
    this.menu = new JlptMenu(this);

    setShowVerticalLines(false);
    addMouseListener(new MouseInputAdapter() {

      @Override
      public void mouseClicked(MouseEvent event) {
        if (SwingUtilities.isRightMouseButton(event)) {
          int row = rowAtPoint(event.getPoint());
          if (row >= 0 && row < getRowCount()) {
            setRowSelectionInterval(row, row);
          }
          else {
            clearSelection();
          }

          menu.setPoint(event.getPoint());
          menu.show(JlptTable.this, event.getX(), event.getY());
        }
      }

    });
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  /** @return The right-click popup menu. */
  public JlptMenu getPopupMenu() {
    return this.menu;
  }

  /**
   * Sets the given entry in the table.
   * 
   * @param entry The entry to set.
   */
  public void setSelectedEntry(JapaneseEntry entry) {
    if (entry == null) {
      return;
    }

    int rowCount = getRowCount();
    for (int index = 0; index < rowCount; index++) {
      if (getValueAt(index, 0).toString().equals(entry.getJword())) {
        getSelectionModel().setSelectionInterval(index, index);
        return;
      }
      else if (getValueAt(index, 1).toString().equals(entry.getReading())) {
        getSelectionModel().setSelectionInterval(index, index);
        return;
      }
      else if (getValueAt(index, 2).toString().equals(entry.getEnglishMeaning())) {
        getSelectionModel().setSelectionInterval(index, index);
        return;
      }
    }
  }

  /**
   * Returns the entry at the given row.
   * 
   * @param row The row where the entry is located.
   * @return The entry at the given row, or <code>null</code> if it is not found.
   */
  public JapaneseEntry getEntry(int row) {
    Validator.checkNonNegative(row);

    String jword = getValueAt(row, 0).toString();
    return getEntry(jword);
  }

  /**
   * Returns the entry at the given row.
   * 
   * @param jword The Japanese word to use for the search.
   * @return The entry that contains the given Japanese word.
   */
  public JapaneseEntry getEntry(String jword) {
    Validator.checkNotEmptyString(jword);

    for (JapaneseEntry entry : this.model.getEntries()) {
      if (entry.getJword().equals(jword)) {
        return entry;
      }
    }
    return null;
  }

}
