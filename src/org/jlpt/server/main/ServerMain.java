package org.jlpt.server.main;

import java.awt.BorderLayout;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.ui.StatusBar;
import org.jlpt.common.ui.UiUtils;

/**
 * The main server window for JLPT Study. The user can load the database file and set the port
 * number for the server in this window.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class ServerMain extends JFrame {

  private static final Logger LOGGER = Logger.getGlobal();

  private JTextField databaseLocationTextField;
  private JButton btnOpenFile;
  private JTextField portTextField;
  private JButton btnStartServer;
  private JButton btnStopServer;
  private DbManager dbManager;
  private ServerDbManager serverDbManager;
  private JLabel statusLabel;

  /**
   * Creates and displays the main server window for JLPT Study on the user's screen.
   */
  public ServerMain() {
    getContentPane().setLayout(new BorderLayout());
    UiUtils.setFrameProperties(this, "JLPT Study Server (ALPHA version)");

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(null);
    getContentPane().add(centerPanel, BorderLayout.CENTER);

    int width = 100;

    this.btnStopServer = new JButton("Stop Server");
    this.btnStopServer.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        try {
          serverDbManager.shutdown();
          toggleComponents(true);
          setOfflineStatus();
        }
        catch (IOException e) {
          LOGGER.log(Level.SEVERE, e.getMessage());
        }
      }

    });
    this.btnStopServer.setEnabled(false);
    this.btnStopServer.setBounds(434, 45, width, 23);
    centerPanel.add(this.btnStopServer);

    this.btnStartServer = new JButton("Start Server");
    this.btnStartServer.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        startServer();
      }

    });
    this.btnStartServer.setEnabled(false);
    this.btnStartServer.setBounds(324, 45, width, 23);
    centerPanel.add(this.btnStartServer);

    this.btnOpenFile = new JButton("Open File...");
    this.btnOpenFile.setBounds(434, 13, width, 23);
    this.btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        openFileDialogBox();
      }

    });
    centerPanel.add(this.btnOpenFile);

    this.databaseLocationTextField = new JTextField();
    this.databaseLocationTextField.addKeyListener(new CustomKeyAdapter());
    this.databaseLocationTextField.setColumns(10);
    this.databaseLocationTextField.setBounds(117, 14, 307, 20);
    centerPanel.add(this.databaseLocationTextField);

    JLabel databaseLocationLabel = new JLabel("Database Location:");
    databaseLocationLabel.setBounds(10, 17, 97, 14);
    centerPanel.add(databaseLocationLabel);

    JLabel portLabel = new JLabel("Port:");
    portLabel.setBounds(79, 49, 28, 14);
    centerPanel.add(portLabel);

    this.portTextField = new JTextField() {
      @Override
      public void processKeyEvent(KeyEvent ev) {
        char c = ev.getKeyChar();
        if ((c >= 48 && c <= 57) || c < 32 || c > 126) {
          super.processKeyEvent(ev);
        }
      }
    };
    this.portTextField.addKeyListener(new CustomKeyAdapter());
    this.portTextField.setColumns(10);
    this.portTextField.setBounds(117, 46, 55, 20);
    centerPanel.add(this.portTextField);
    StatusBar statusBar = new StatusBar();
    this.statusLabel = new JLabel();
    setOfflineStatus();
    statusBar.addComponent(this.statusLabel);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(560, 130);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Sets the label to offline status.
   */
  private void setOfflineStatus() {
    this.statusLabel.setText("Server is offline.");
    this.statusLabel.setIcon(UiUtils.getOfflineIcon());
  }

  /**
   * Sets the label to online status.
   */
  private void setOnlineStatus() {
    this.statusLabel.setText("Server is online.");
    this.statusLabel.setIcon(UiUtils.getOnlineIcon());
  }

  /**
   * Toggles the state of the components in the window.
   * 
   * @param isEnabled True to enable the components (except the Stop Server button), false otherwise
   * (false to disable the Start Server button).
   */
  private void toggleComponents(boolean isEnabled) {
    this.databaseLocationTextField.setEnabled(isEnabled);
    this.btnOpenFile.setEnabled(isEnabled);
    this.portTextField.setEnabled(isEnabled);
    this.btnStartServer.setEnabled(isEnabled);
    this.btnStopServer.setEnabled(!isEnabled);
  }

  /**
   * Displays the Open File dialog box.
   */
  private void openFileDialogBox() {
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Database file (*.db)", "db");
    chooser.setFileFilter(filter);
    int returnValue = chooser.showOpenDialog(ServerMain.this);
    if (returnValue == JFileChooser.APPROVE_OPTION) {
      this.databaseLocationTextField.setText(chooser.getSelectedFile().getAbsolutePath());
      if (!portTextField.getText().isEmpty()) {
        this.btnStartServer.setEnabled(true);
      }
    }
  }

  /**
   * Starts the server.
   */
  private void startServer() {
    File file = new File(this.databaseLocationTextField.getText());
    if (!file.exists()) {
      String msg = "The selected file does not exist. Please try again.";
      JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    try {
      this.dbManager = new DbManagerImpl(this.databaseLocationTextField.getText());
      int port = Integer.parseInt(this.portTextField.getText());
      this.serverDbManager = new ServerDbManager(this.dbManager, port);
      this.serverDbManager.start();
      toggleComponents(false);
      setOnlineStatus();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * A key adapter that will enable/disable the Start Server button and, depending on the state of
   * the Start Server button, either display the Open File dialog box or start the server.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class CustomKeyAdapter extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent event) {
      boolean flag = !databaseLocationTextField.getText().isEmpty();
      flag = flag && !portTextField.getText().isEmpty();
      btnStartServer.setEnabled(flag);

      if (event.getKeyChar() == KeyEvent.VK_ENTER) {
        if (btnStartServer.isEnabled()) {
          startServer();
        }
        else {
          openFileDialogBox();
        }
      }
    }

  }

}
