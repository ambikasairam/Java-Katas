package org.katas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * A demo program that shows how to add a table displaying totals below the main table.
 * 
 * @author BJ Peter DeLaCruz <bjpeter@hawaii.edu>
 */
public class TwoTablesDemo {

  /**
   * The main program.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        List<Employee> employees = new ArrayList<Employee>();

        employees.add(new Employee("Emiko", "Taniguchi", "", 57000));
        employees.add(new Employee("Kanako", "Nakanishi", "", 59000));
        employees.add(new Employee("Mayu", "Watanabe", "", 62000));
        employees.add(new Employee("Bill", "Pierce", "W", 65000));
        employees.add(new Employee("Jack", "Smith", "B", 60000));
        employees.add(new Employee("John", "Williams", "C", 69000));

        JTable table = new JTable(new EmployeeTableModel(employees));
        table.setAutoCreateRowSorter(true);
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        int total = calculateSalaryTotals(employees);
        JTable totalsTable = new JTable(new EmployeeTotalsTableModel(total));
        totalsTable.setShowGrid(false);
        panel.add(totalsTable, BorderLayout.SOUTH);

        JFrame frame = new JFrame();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setTitle("Employee Salaries");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
      }
    });
  }

  /**
   * Sums the salaries of all employees.
   * 
   * @param employees The list of employees.
   * @return The total salaries of all employees.
   */
  private static int calculateSalaryTotals(List<Employee> employees) {
    int total = 0;
    for (Employee e : employees) {
      total += e.getIncome();
    }
    return total;
  }

}
