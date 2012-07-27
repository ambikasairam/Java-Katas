package org.jlpt.client.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

  private static final Logger LOGGER = Logger.getGlobal();

  private JTextField databaseLocationTextField;
  private JButton btnStartClient;

  /**
   * Creates a new StandaloneClientOpenFileDialogBox.
   */
  public StandaloneClientOpenFileDialogBox() {
    getContentPane().setLayout(null);
    UiUtils.setFrameProperties(this, "Select Database File");

    this.btnStartClient = new JButton("Start Client");
    this.btnStartClient.setMnemonic(KeyEvent.VK_S);
    this.btnStartClient.setEnabled(false);
    this.btnStartClient.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        startClient();
      }
    });
    getContentPane().add(this.btnStartClient);

    JLabel lblDatabaseLocation = new JLabel("Database Location:");
    lblDatabaseLocation.setBounds(10, 16, 97, 14);
    getContentPane().add(lblDatabaseLocation);

    this.databaseLocationTextField = new JTextField();
    this.databaseLocationTextField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        btnStartClient.setEnabled(!databaseLocationTextField.getText().isEmpty());
        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
          if (btnStartClient.isEnabled()) {
            startClient();
          }
          else {
            openFileDialogBox();
          }
        }
      }

    });
    this.databaseLocationTextField.setBounds(117, 13, 300, 20);
    getContentPane().add(this.databaseLocationTextField);
    this.databaseLocationTextField.setColumns(10);

    JButton btnOpenFile = new JButton("Open File...");
    btnOpenFile.setMnemonic(KeyEvent.VK_O);
    btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        openFileDialogBox();
      }

    });
    int width = btnOpenFile.getPreferredSize().width;
    int height = btnOpenFile.getPreferredSize().height;
    btnOpenFile.setBounds(427, 12, width, height);
    this.btnStartClient.setBounds(427, 46, width, height);
    getContentPane().add(btnOpenFile);

    setSize(545, 110);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Launches the Open File dialog box.
   */
  private void openFileDialogBox() {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Database file (*.db)", "db");
    chooser.setFileFilter(filter);
    int returnValue = chooser.showOpenDialog(StandaloneClientOpenFileDialogBox.this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      this.databaseLocationTextField.setText(chooser.getSelectedFile().getAbsolutePath());
      this.btnStartClient.setEnabled(true);
    }
  }

  /**
   * Starts the client application.
   */
  private void startClient() {
    File file = new File(this.databaseLocationTextField.getText());
    if (!file.exists()) {
      String msg = "The selected file does not exist. Please try again.";
      JOptionPane.showMessageDialog(StandaloneClientOpenFileDialogBox.this, msg, "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      // TODO: Handle different types of delimiters.
      new ClientMain(new DbManagerImpl(this.databaseLocationTextField.getText()));
      UiUtils.closeFrame(StandaloneClientOpenFileDialogBox.this);
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Unable to create database manager: " + e.getMessage());
    }
  }

}
