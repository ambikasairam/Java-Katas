package org.katas;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Creates a table and adds rows and content to it using the custom table model.
 * 
 * @author BJ Peter DeLaCruz
 */
public class TestSampleTableModel {

  /**
   * Adds data to the sample JTable.
   * 
   * @param tableModel The table model behind the JTable.
   */
  private static void addData(SampleTableModel tableModel) {
    tableModel.setValueAt(true, 0, 0);
    tableModel.setValueAt(false, 1, 0);
    tableModel.setValueAt(true, 2, 0);
    tableModel.setValueAt(false, 3, 0);
    tableModel.setValueAt(true, 4, 0);

    tableModel.setValueAt("ICS 690", 0, 1);
    tableModel.setValueAt("ICS 613", 1, 1);
    tableModel.setValueAt("ICS 611", 2, 1);
    tableModel.setValueAt("ICS 432", 3, 1);
    tableModel.setValueAt("ICS 413", 4, 1);

    tableModel.setValueAt("H. Casanova", 0, 2);
    tableModel.setValueAt("P. Johnson", 1, 2);
    tableModel.setValueAt("D. Pager", 2, 2);
    tableModel.setValueAt("H. Casanova", 3, 2);
    tableModel.setValueAt("P. Johnson", 4, 2);

    tableModel.setValueAt("1200-1315", 0, 3);
    tableModel.setValueAt("0900-1015", 1, 3);
    tableModel.setValueAt("1030-1145", 2, 3);
    tableModel.setValueAt("1330-1445", 3, 3);
    tableModel.setValueAt("1500-1615", 4, 3);

    tableModel.setValueAt(30, 0, 4);
    tableModel.setValueAt(20, 1, 4);
    tableModel.setValueAt(15, 2, 4);
    tableModel.setValueAt(20, 3, 4);
    tableModel.setValueAt(20, 4, 4);
  }

  /**
   * The main program.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }
    catch (ClassNotFoundException e1) {
      e1.printStackTrace();
      return;
    }
    catch (InstantiationException e1) {
      e1.printStackTrace();
      return;
    }
    catch (IllegalAccessException e1) {
      e1.printStackTrace();
      return;
    }
    catch (UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
      return;
    }

    JFrame frame = new JFrame();

    SampleTableModel tableModel = new SampleTableModel(5, 5);
    tableModel.addRow(new Object[5]);
    tableModel.addRow(new Object[5]);
    tableModel.addRow(new Object[5]);
    tableModel.addRow(new Object[5]);
    tableModel.addRow(new Object[5]);

    addData(tableModel);

    JTable table = new JTable(tableModel);
    table.setTableHeader(new CustomTableHeader(table.getColumnModel()));

    JScrollPane scrollPane = new JScrollPane(table);

    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setSize(500, 250);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
