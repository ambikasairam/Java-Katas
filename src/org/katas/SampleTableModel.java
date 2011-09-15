package org.katas;

import javax.swing.table.DefaultTableModel;

/**
 * Used to demonstrate how to use a table model with a JTable.
 * 
 * @author BJ Peter DeLaCruz
 */
public class SampleTableModel extends DefaultTableModel {

  /** Used for object serialization. */
  private static final long serialVersionUID = 1L;
  /** Maximum number of columns. */
  private static final int NUM_COLUMNS = 2;
  /** Holds the data stored in all of the columns. */
  private final Object[][] DATA;

  /**
   * Creates a new SampleTableModel object with a table that will have a user-specified number of
   * rows and columns.
   * 
   * @param row Number of rows.
   * @param col Number of columns.
   */
  public SampleTableModel(int row, int col) {
    this.DATA = new Object[row][col];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object value, int row, int col) {
    this.DATA[row][col] = value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getValueAt(int row, int col) {
    return this.DATA[row][col];
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getColumnName(int columnIndex) {
    switch (columnIndex) {
    case 0:
      return "";
    case 1:
      return "Class";
    default:
      throw new IllegalArgumentException("Invalid column index.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(int row, int col) {
    return col == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    if (columnIndex >= NUM_COLUMNS) {
      throw new IllegalArgumentException("Invalid column index.");
    }
    return (columnIndex == 0) ? Boolean.class : String.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getColumnCount() {
    return NUM_COLUMNS;
  }
}