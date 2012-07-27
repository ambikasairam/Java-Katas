package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jlpt.common.ui.StatusBar;
import org.jlpt.common.ui.UiUtils;

/**
 * A dialog box in which the user inputs the URL and port of the server to which to connect.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class NetworkedClientConfigDialogBox extends JFrame {

  private static final Logger LOGGER = Logger.getGlobal();

  private final JTextField portTextField;
  private final JButton btnStartClient;
  private final JTextField serverNameTextField;
  private final JLabel statusLabel;

  /**
   * Creates a new NetworkedClientConfigDialogBox.
   */
  public NetworkedClientConfigDialogBox() {
    getContentPane().setLayout(new BorderLayout());
    UiUtils.setFrameProperties(this, "Connect to Server");

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(null);
    getContentPane().add(centerPanel, BorderLayout.CENTER);

    JLabel serverUrlLabel = new JLabel("Server URL:");
    serverUrlLabel.setBounds(20, 14, 58, 20);
    centerPanel.add(serverUrlLabel);

    this.serverNameTextField = new JTextField();
    this.serverNameTextField.setBounds(88, 14, 200, 20);
    this.serverNameTextField.setColumns(50);
    centerPanel.add(this.serverNameTextField);

    JLabel portLabel = new JLabel("Port:");
    portLabel.setBounds(54, 48, 24, 14);
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
    this.portTextField.setBounds(88, 45, 55, 20);
    centerPanel.add(this.portTextField);

    this.btnStartClient = new JButton("Start Client");
    this.btnStartClient.setMnemonic(KeyEvent.VK_S);
    this.btnStartClient.setEnabled(false);
    centerPanel.add(this.btnStartClient);

    Dimension dimension = this.btnStartClient.getPreferredSize();
    this.btnStartClient.setBounds(302, 45, dimension.width, dimension.height);

    StatusBar statusBar = new StatusBar();
    this.statusLabel = new JLabel();
    setOfflineStatus();
    statusBar.addComponent(this.statusLabel);
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(415, 130);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * A key adapter that will enable/disable the Start Client button and, depending on the state of
   * the Start Server button, either display the Open File dialog box or start the server.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class CustomKeyAdapter extends KeyAdapter {

    @Override
    public void keyReleased(KeyEvent event) {
      boolean flag = !serverNameTextField.getText().isEmpty();
      flag = flag && !portTextField.getText().isEmpty();
      btnStartClient.setEnabled(flag);

      if (event.getKeyChar() == KeyEvent.VK_ENTER && btnStartClient.isEnabled()) {
        startClient();
      }
    }

  }

  /**
   * Starts the client application.
   */
  private void startClient() {
    try {
      setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
      // TODO: Handle different types of delimiters.
      // TODO: Add code to open ClientMain here.
      UiUtils.closeFrame(NetworkedClientConfigDialogBox.this);
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to create database manager: " + e.getMessage());
    }
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

}
