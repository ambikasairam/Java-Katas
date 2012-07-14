package org.jlpt.client.table;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jlpt.common.datamodel.JapaneseEntry;

/**
 * A table model that displays Japanese words and their English meanings in a JTable.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptTableModel extends AbstractTableModel {

  private final List<JapaneseEntry> rows;

  private static final int NUM_COLUMNS = 3;

  /**
   * Creates a new JlptTableModel instance.
   * 
   * @param rows The rows containing JLPT entries used to populate the JTable.
   */
  public JlptTableModel(List<JapaneseEntry> rows) {
    this.rows = new ArrayList<>(rows);
  }

  /** {@inheritDoc} */
  @Override
  public int getColumnCount() {
    return NUM_COLUMNS;
  }

  /** {@inheritDoc} */
  @Override
  public int getRowCount() {
    return this.rows.size();
  }

  /** {@inheritDoc} */
  @Override
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return "Kana/Kanji";
    case 1:
      return "Romaji";
    case 2:
      return "English Meaning";
    default:
      throw new IllegalArgumentException("Invalid column index: " + column);
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object getValueAt(int row, int column) {
    JapaneseEntry entry = this.rows.get(row);
    switch (column) {
    case 0:
      return entry.getJword();
    case 1:
      return entry.getReading();
    case 2:
      return entry.getEnglishMeaning();
    default:
      throw new IllegalArgumentException("Invalid column index: " + column);
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  /** @return A copy of the entries in this table model. */
  public List<JapaneseEntry> getEntries() {
    return new ArrayList<>(this.rows);
  }

}
