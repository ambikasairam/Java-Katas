package org.katas;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * A table model that will display employee information.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class EmployeeTableModel extends AbstractTableModel {

  private static final long serialVersionUID = -1L;
  private final List<Employee> employees = new ArrayList<Employee>();
  protected static final int COLUMN_COUNT = 4;

  /**
   * Creates a new EmployeeTableModel.
   * 
   * @param employees The list of employees.
   */
  public EmployeeTableModel(List<Employee> employees) {
    this.employees.addAll(employees);
  }

  /** {@inheritDoc} */
  @Override
  public int getColumnCount() {
    return COLUMN_COUNT;
  }

  /** {@inheritDoc} */
  @Override
  public int getRowCount() {
    return employees.size();
  }

  /** {@inheritDoc} */
  @Override
  public Class<?> getColumnClass(int column) {
    return getValueAt(0, column).getClass();
  }

  /** {@inheritDoc} */
  @Override
  public String getColumnName(int column) {
    switch (column) {
    case 0:
      return "First Name";
    case 1:
      return "Last Name";
    case 2:
      return "Middle Initial";
    case 3:
      return "Salary";
    default:
      throw new IllegalArgumentException("Invalid column index.");
    }
  }

  /** {@inheritDoc} */
  @Override
  public Object getValueAt(int row, int column) {
    Employee employee = this.employees.get(row);
    switch (column) {
    case 0:
      return employee.getFirstName();
    case 1:
      return employee.getLastName();
    case 2:
      return employee.getMiddleInitial();
    case 3:
      return employee.getIncome();
    default:
      throw new IllegalArgumentException("Invalid column index.");
    }
  }

}
