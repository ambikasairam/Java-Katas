package org.katas;

import javax.swing.table.AbstractTableModel;

/**
 * A table model that will display the total salaries for all employees.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class EmployeeTotalsTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -1L;
  private final int totalSalaries;

  /**
   * Creates a new EmployeeTotalsTableModel.
   * 
   * @param totalSalaries The total salaries for all employees.
   */
  public EmployeeTotalsTableModel(int totalSalaries) {
    this.totalSalaries = totalSalaries;
  }

  /** {@inheritDoc} */
  @Override
  public int getColumnCount() {
    return EmployeeTableModel.COLUMN_COUNT;
  }

  /** {@inheritDoc} */
  @Override
  public int getRowCount() {
    return 1;
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> getColumnClass(int column) {
    return getValueAt(0, column).getClass();
  }

  /** {@inheritDoc} */
  @Override
  public Object getValueAt(int row, int column) {
    if (row != 0) {
      throw new IllegalArgumentException("Invalid column index.");
    }
    switch (column) {
    case 0:
      return "";
    case 1:
      return "";
    case 2:
      return "Total:";
    case 3:
      return this.totalSalaries;
    default:
      throw new IllegalArgumentException("Invalid column index.");
    }
  }

}
