package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.ui.StatusBar;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.common.utils.ThreadUtils;

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
  private JButton btnOpenFile;
  private JButton btnStartClient;
  private JLabel statusLabel;

  /**
   * Creates a new StandaloneClientOpenFileDialogBox.
   */
  public StandaloneClientOpenFileDialogBox() {
    getContentPane().setLayout(new BorderLayout());
    JPanel innerPanel = new JPanel(new BorderLayout());
    UiUtils.setFrameProperties(this, "Select Database File");

    JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

    JLabel lblDatabaseLocation = new JLabel("Database Location:");
    formPanel.add(lblDatabaseLocation);

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
    this.databaseLocationTextField.setPreferredSize(new Dimension(250,
        this.databaseLocationTextField.getPreferredSize().height));
    this.databaseLocationTextField.setDisabledTextColor(Color.BLACK);
    formPanel.add(this.databaseLocationTextField);

    this.btnOpenFile = new JButton("Open File...");
    this.btnOpenFile.setMnemonic(KeyEvent.VK_O);
    this.btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        openFileDialogBox();
      }

    });

    formPanel.add(this.btnOpenFile);
    innerPanel.add(formPanel, BorderLayout.CENTER);

    this.btnStartClient = new JButton("Start Client");
    this.btnStartClient.setMnemonic(KeyEvent.VK_S);
    this.btnStartClient.setEnabled(false);
    this.btnStartClient.setPreferredSize(this.btnOpenFile.getPreferredSize());
    this.btnStartClient.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        startClient();
      }
    });

    JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    panel.add(this.btnStartClient);
    innerPanel.add(panel, BorderLayout.SOUTH);

    getContentPane().add(innerPanel, BorderLayout.CENTER);

    JPanel westPanel = new JPanel();
    westPanel.setOpaque(false);
    JLabel padding = new JLabel();
    this.statusLabel = new JLabel();
    westPanel.add(padding);
    westPanel.add(this.statusLabel);

    StatusBar statusBar = new StatusBar();
    statusBar.addComponent(westPanel, BorderLayout.WEST);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

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

    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.statusLabel.setText("Initializing database...");
    this.statusLabel.setIcon(UiUtils.getInitIcon());
    this.databaseLocationTextField.setEnabled(false);
    this.btnOpenFile.setEnabled(false);
    this.btnStartClient.setEnabled(false);
    ThreadUtils.SHARED_THREAD_POOL.execute(new InitializeDatabaseTask());
  }

  /**
   * A task that will initialize the database and then display the main client window once the
   * database is initialized.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class InitializeDatabaseTask implements Runnable {

    /** {@inheritDoc} */
    @Override
    public void run() {
      String location = databaseLocationTextField.getText();
      try {
        final DbManager databaseManager = new DbManagerImpl(location);
        databaseManager.addEntries();
        SwingUtilities.invokeLater(new Runnable() {

          /** {@inheritDoc} */
          @Override
          public void run() {
            ClientMain clientMain = new ClientMain(databaseManager);
            clientMain.setStandaloneStatus();
            UiUtils.closeFrame(StandaloneClientOpenFileDialogBox.this);
          }

        });
      }
      catch (final IOException e) {
        LOGGER.log(Level.SEVERE, "Failed to initialize database: " + e.getMessage());
        SwingUtilities.invokeLater(new Runnable() {

          /** {@inheritDoc} */
          @Override
          public void run() {
            databaseLocationTextField.setEnabled(true);
            btnOpenFile.setEnabled(true);
            btnStartClient.setEnabled(true);
            statusLabel.setText("Database Initialization Error");
            statusLabel.setIcon(UiUtils.getOfflineIcon());
            String msg = "There was a problem trying to initialize the database: " + e;
            JOptionPane.showMessageDialog(StandaloneClientOpenFileDialogBox.this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
          }

        });
      }
    }

  }

}
