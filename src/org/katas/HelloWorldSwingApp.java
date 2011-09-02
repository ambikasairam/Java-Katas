package org.katas;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A sample program to test the Windows/Mac OS X look and feel of a Swing desktop application.
 * 
 * @author BJ Peter DeLaCruz
 */
public class HelloWorldSwingApp extends JPanel {

  /**
   * Used to serialize this object.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new HelloWorldSwingApp object.
   */
  public HelloWorldSwingApp() {
    JButton button = new JButton("Click Me");
    button.addActionListener(new ActionListener() {

      /**
       * When the button is clicked, show four different types of messages.
       *
       * @param arg0 Not used.
       */
      @Override
      public void actionPerformed(ActionEvent arg0) {
        String title = "Swing App";
        JOptionPane.showMessageDialog(HelloWorldSwingApp.this, "Hello World!", title,
            JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(HelloWorldSwingApp.this, "How do I look?", title,
            JOptionPane.QUESTION_MESSAGE);
        String msg = "Don't use the Java look and feel!";
        JOptionPane.showMessageDialog(HelloWorldSwingApp.this, msg, title,
            JOptionPane.WARNING_MESSAGE);
        JOptionPane.showMessageDialog(HelloWorldSwingApp.this, "Bye Bye!", title,
            JOptionPane.ERROR_MESSAGE);
      }

    });
    add(button);
  }

  /**
   * Set the look and feel of this Swing desktop application to a Windows/Mac OS X look and feel,
   * and then display a window with a button in it.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      /*
       * If you are using a Mac OS X computer, replace the string above with the following:
       * 
       * "com.sun.java.swing.mac.MacLookAndFeel"
       * 
       * If you are using the wrong look and feel for an OS, one of the following exceptions will be
       * thrown and caught.
       */
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
    frame.add(new HelloWorldSwingApp());
    frame.setSize(new Dimension(100, 100));
    frame.setLocationRelativeTo(null);

    frame.setVisible(true);
  }

}
