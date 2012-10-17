package org.jlpt.server.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
    UiUtils.setFrameProperties(this, "JLPT Study Server (Beta 1)");

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
    JPanel centerPanel = new JPanel(new GridBagLayout());

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
    buttonPanel.add(this.btnStopServer);

    this.btnStartServer = new JButton("Start Server");
    this.btnStartServer.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        startServer();
      }

    });
    this.btnStartServer.setEnabled(false);
    buttonPanel.add(this.btnStartServer);

    this.btnOpenFile = new JButton("Open File...");
    this.btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        openFileDialogBox();
      }

    });

    this.databaseLocationTextField = new JTextField();
    this.databaseLocationTextField.setDisabledTextColor(Color.BLACK);
    this.databaseLocationTextField.addKeyListener(new CustomKeyAdapter());
    this.databaseLocationTextField.setPreferredSize(new Dimension(250,
        this.databaseLocationTextField.getPreferredSize().height));

    JPanel dbLocationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    JLabel dbLocationLabel = new JLabel("Database Location:");
    dbLocationPanel.add(dbLocationLabel);
    dbLocationPanel.add(this.databaseLocationTextField);
    dbLocationPanel.add(this.btnOpenFile);

    JPanel portPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    JLabel portLabel = new JLabel("Port:");
    portLabel.setPreferredSize(dbLocationLabel.getPreferredSize());
    portPanel.add(portLabel);

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
    this.portTextField.setDisabledTextColor(Color.BLACK);
    this.portTextField.setColumns(5);
    this.portTextField.setMaximumSize(new Dimension(50,
        this.portTextField.getPreferredSize().height));
    portPanel.add(this.portTextField);

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.fill = GridBagConstraints.HORIZONTAL;
    centerPanel.add(dbLocationPanel, constraints);
    constraints.gridy = 1;
    centerPanel.add(portPanel, constraints);
    constraints.gridy = 2;
    centerPanel.add(buttonPanel, constraints);
    getContentPane().add(centerPanel, BorderLayout.CENTER);

    StatusBar statusBar = new StatusBar();

    JPanel westPanel = new JPanel();
    westPanel.setOpaque(false);
    JLabel padding = new JLabel();
    this.statusLabel = new JLabel();
    westPanel.add(padding);
    westPanel.add(this.statusLabel);

    setOfflineStatus();
    statusBar.addComponent(westPanel, BorderLayout.WEST);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(545, 140);
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
      String location = this.databaseLocationTextField.getText();
      this.dbManager = new DbManagerImpl(location);
      int port = Integer.parseInt(this.portTextField.getText());
      this.serverDbManager = new ServerDbManager(this.dbManager, port);
      this.serverDbManager.start();
      toggleComponents(false);
      setOnlineStatus();
      String msg = "Successfully started server.\n   Database Location: " + location;
      msg += "\n   Port: " + port;
      LOGGER.log(Level.INFO, msg);
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
