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
    tableModel.addRow(new Object[2]);
    tableModel.addRow(new Object[2]);
    tableModel.addRow(new Object[2]);
    tableModel.addRow(new Object[2]);
    tableModel.addRow(new Object[2]);
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
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);
    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
    frame.setSize(300, 150);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
  }
}
