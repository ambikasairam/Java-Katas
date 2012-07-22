package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.ui.UiUtils;

/**
 * A dialog box in which a user inputs the name of the database file that is located on his or her
 * computer.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class StandaloneClientOpenFileDialogBox extends JFrame {

  private JTextField textField;

  /**
   * Creates a new StandaloneClientOpenFileDialogBox.
   */
  public StandaloneClientOpenFileDialogBox() {
    getContentPane().setLayout(null);
    UiUtils.setFrameProperties(this, "Select Database File");

    final JButton btnLaunchClient = new JButton("Start Client");
    btnLaunchClient.setEnabled(false);
    btnLaunchClient.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        File file = new File(textField.getText());
        if (!file.exists()) {
          String msg = "The selected file does not exist. Please try again.";
          JOptionPane.showMessageDialog(StandaloneClientOpenFileDialogBox.this, msg, "Error",
              JOptionPane.ERROR_MESSAGE);
          return;
        }

        try {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          // TODO: Handle different types of delimiters.
          new ClientMain(new DbManagerImpl(textField.getText()));
          UiUtils.closeFrame(StandaloneClientOpenFileDialogBox.this);
        }
        catch (IOException e) {
          // TODO: Add logger.
          System.err.println("Unable to create database manager: " + e.getMessage());
        }
      }
    });
    getContentPane().add(btnLaunchClient);

    JLabel lblDatabaseLocation = new JLabel("Database Location:");
    lblDatabaseLocation.setBounds(10, 11, 97, 14);
    getContentPane().add(lblDatabaseLocation);

    this.textField = new JTextField();
    this.textField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        btnLaunchClient.setEnabled(!textField.getText().isEmpty());
      }

    });
    this.textField.setBounds(117, 8, 300, 20);
    getContentPane().add(this.textField);
    this.textField.setColumns(10);

    JButton btnOpenFile = new JButton("Open File...");
    btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Database file (*.db)", "db");
        chooser.setFileFilter(filter);
        int returnValue = chooser.showOpenDialog(StandaloneClientOpenFileDialogBox.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          textField.setText(chooser.getSelectedFile().getAbsolutePath());
          btnLaunchClient.setEnabled(true);
        }
      }

    });
    int width = btnOpenFile.getPreferredSize().width;
    int height = btnOpenFile.getPreferredSize().height;
    btnOpenFile.setBounds(427, 7, width, height);
    btnLaunchClient.setBounds(427, 41, width, height);
    getContentPane().add(btnOpenFile);

    setSize(545, 110);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

}
