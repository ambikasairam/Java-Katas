package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.ui.StatusBar;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.common.utils.ThreadUtils;

/**
 * A dialog box in which the user inputs the URL and port of the server to which to connect.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class NetworkedClientConfigDialogBox extends JFrame {

  private static final Logger LOGGER = Logger.getGlobal();
  private static final String CONNECT = "Connect";
  private static final String DISCONNECT = "Disconnect";

  private final JTextField serverNameTextField;
  private final JTextField portTextField;
  private final JButton btnConnect;
  private final JButton btnStartClient;
  private final JLabel statusLabel;
  private DbManager clientDbManager;

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
    this.serverNameTextField.addKeyListener(new CustomKeyAdapter());
    this.serverNameTextField.setBounds(88, 14, 200, 20);
    this.serverNameTextField.setDisabledTextColor(Color.BLACK);
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
    this.portTextField.setDisabledTextColor(Color.BLACK);
    centerPanel.add(this.portTextField);

    this.btnStartClient = new JButton("Start Client");
    this.btnStartClient.setMnemonic(KeyEvent.VK_S);
    this.btnStartClient.setEnabled(false);
    this.btnStartClient.addActionListener(new StartClientButtonAction());
    centerPanel.add(this.btnStartClient);

    Dimension dimension = this.btnStartClient.getPreferredSize();
    this.btnStartClient.setBounds(302, 45, dimension.width, dimension.height);

    this.btnConnect = new JButton(CONNECT);
    this.btnConnect.addActionListener(new ConnectDisconnectAction());
    this.btnConnect.setMnemonic(KeyEvent.VK_S);
    this.btnConnect.setEnabled(false);
    this.btnConnect.setBounds(302, 13, dimension.width, dimension.height);
    centerPanel.add(this.btnConnect);

    JPanel westPanel = new JPanel();
    westPanel.setOpaque(false);
    JLabel padding = new JLabel();
    this.statusLabel = new JLabel();
    westPanel.add(padding);
    westPanel.add(this.statusLabel);

    setOfflineStatus();
    StatusBar statusBar = new StatusBar();
    statusBar.addComponent(westPanel, BorderLayout.WEST);
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
      if (event.getKeyChar() == KeyEvent.VK_ENTER && btnStartClient.isEnabled()) {
        startClient();
        return;
      }

      boolean flag = !serverNameTextField.getText().isEmpty();
      flag = flag && !portTextField.getText().isEmpty();
      btnConnect.setEnabled(flag);

      if (event.getKeyChar() == KeyEvent.VK_ENTER && btnConnect.isEnabled()) {
        connectToServer();
      }
    }

  }

  /**
   * Connects to the server and enables the Start Client button when a connection to the server has
   * been established.
   */
  private void connectToServer() {
    setConnectingStatus();
    ThreadUtils.SHARED_THREAD_POOL.execute(new ConnectTask());
  }

  /**
   * An action that will either connect to or disconnect from the server.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class ConnectDisconnectAction implements ActionListener {

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent arg0) {
      switch (btnConnect.getText()) {
      case CONNECT:
        connectToServer();
        break;
      case DISCONNECT:
        try {
          if (((ClientDbManager) clientDbManager).close()) {
            setOfflineStatus();
            LOGGER.log(Level.INFO, "Disconnected from server.");
            return;
          }
          throw new IllegalStateException("The socket should have been closed.");
        }
        catch (IOException e) {
          LOGGER.log(Level.SEVERE, e.getMessage());
        }
        break;
      default:
        throw new IllegalArgumentException("Unsupported action: " + btnConnect.getText());
      }
    }

  }

  /**
   * Attempts to connect to the server when the user clicks on the Connect button.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class ConnectTask implements Runnable {

    /** {@inheritDoc} */
    @Override
    public void run() {
      String serverName = serverNameTextField.getText();
      int port = Integer.parseInt(portTextField.getText());

      try {
        serverNameTextField.setEnabled(false);
        portTextField.setEditable(false);
        btnConnect.setEnabled(false);

        clientDbManager = new ClientDbManager(serverName, port);
        clientDbManager.addEntries();

        String msg = "Successfully connected to server.\n   Server URL: " + serverName;
        msg += "\n   Port: " + port;
        LOGGER.log(Level.INFO,  msg);
        SwingUtilities.invokeLater(new Runnable() {

          /** {@inheritDoc} */
          @Override
          public void run() {
            setOnlineStatus();
          }

        });
      }
      catch (final IOException e) {
        String errorMsg = "Unable to connect to server.\n   Server URL: " + serverName;
        errorMsg += "\n   Port: " + port + "\n   Reason: " + e;
        LOGGER.log(Level.SEVERE, errorMsg);
        SwingUtilities.invokeLater(new Runnable() {

          /** {@inheritDoc} */
          @Override
          public void run() {
            btnConnect.setEnabled(true);
            setOfflineStatus();
            String msg = "Unable to connect to server. Reason:\n\n" + e;
            JOptionPane.showMessageDialog(NetworkedClientConfigDialogBox.this, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
          }

        });
      }
    }

  }

  /**
   * An action that will launch the main client window when the Start Client button is clicked.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class StartClientButtonAction implements ActionListener {
    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      startClient();
    }
  }

  /**
   * Starts the client application.
   */
  private void startClient() {
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    ClientDbManager dbManager = (ClientDbManager) this.clientDbManager;
    ClientMain clientMain = new ClientMain(dbManager);
    dbManager.setServerStatusListener(clientMain);
    dbManager.startCheckServerStatusTask();
    UiUtils.closeFrame(NetworkedClientConfigDialogBox.this);
  }

  /**
   * Sets the label to offline status.
   */
  private void setOfflineStatus() {
    this.statusLabel.setText("Not connected to server.");
    this.statusLabel.setIcon(UiUtils.getOfflineIcon());
    this.btnConnect.setText(CONNECT);
    this.btnStartClient.setEnabled(false);
    this.serverNameTextField.setEnabled(true);
    this.portTextField.setEditable(true);
  }

  /**
   * Sets the label to online status.
   */
  private void setOnlineStatus() {
    this.statusLabel.setText("Connected to server.");
    this.statusLabel.setIcon(UiUtils.getOnlineIcon());
    this.btnConnect.setText(DISCONNECT);
    this.btnConnect.setEnabled(true);
    this.btnStartClient.setEnabled(true);
    this.serverNameTextField.setEnabled(false);
    this.portTextField.setEditable(false);
  }

  /**
   * Sets the label to online status.
   */
  private void setConnectingStatus() {
    this.statusLabel.setText("Connecting to server...");
    this.statusLabel.setIcon(UiUtils.getConnectingIcon());
  }

}
