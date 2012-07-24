package org.jlpt.server.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
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

  private JTextField databaseLocationTextField;
  private JTextField portTextField;

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

    JButton btnStopServer = new JButton("Stop Server");
    btnStopServer.setEnabled(false);
    btnStopServer.setBounds(434, 45, width, 23);
    centerPanel.add(btnStopServer);

    final JButton btnStartServer = new JButton("Start Server");
    btnStartServer.setEnabled(false);
    btnStartServer.setBounds(324, 45, width, 23);
    centerPanel.add(btnStartServer);

    JButton btnOpenFile = new JButton("Open File...");
    btnOpenFile.setBounds(434, 13, width, 23);
    btnOpenFile.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Database file (*.db)", "db");
        chooser.setFileFilter(filter);
        int returnValue = chooser.showOpenDialog(ServerMain.this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          databaseLocationTextField.setText(chooser.getSelectedFile().getAbsolutePath());
          if (!portTextField.getText().isEmpty()) {
            btnStartServer.setEnabled(true);
          }
        }
      }

    });
    centerPanel.add(btnOpenFile);

    this.databaseLocationTextField = new JTextField();
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
    this.portTextField.setColumns(10);
    this.portTextField.setBounds(117, 46, 55, 20);
    centerPanel.add(this.portTextField);
    StatusBar statusBar = new StatusBar();
    getContentPane().add(statusBar, BorderLayout.SOUTH);

    setSize(560, 140);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    setVisible(true);
  }

}
