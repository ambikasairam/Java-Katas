package org.jlpt.client.main;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;

/**
 * A dialog box that allows a user to select either client mode or server mode for this
 * application.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class ApplicationLauncher extends JFrame {

  /**
   * Creates a new ApplicationLauncher.
   */
  public ApplicationLauncher() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
      return;
    }
    setTitle("JLPT Study (ALPHA version)");
    setIconImage(new ImageIcon(ClientMain.class.getResource("jpn-flag.png")).getImage());
    getContentPane().setLayout(null);

    JRadioButton rdbtnStandaloneClient = new JRadioButton("Standalone Client (no networking)");
    rdbtnStandaloneClient.setBounds(30, 49, 215, 23);
    getContentPane().add(rdbtnStandaloneClient);

    JRadioButton rdbtnNetworkedClient = new JRadioButton("Networked Client");
    rdbtnNetworkedClient.setBounds(30, 75, 107, 23);
    getContentPane().add(rdbtnNetworkedClient);

    JRadioButton rdbtnServer = new JRadioButton("Server");
    rdbtnServer.setBounds(30, 102, 57, 23);
    getContentPane().add(rdbtnServer);

    JButton btnLaunch = new JButton("Launch");
    btnLaunch.setBounds(156, 128, 67, 23);
    getContentPane().add(btnLaunch);

    String msg = "<html>JLPT Study can run in client mode (with or without networking)<br>";
    msg += " or server mode. Please select one.</html>";
    JLabel lblNewLabel = new JLabel(msg);
    lblNewLabel.setBounds(30, 11, 344, 31);
    getContentPane().add(lblNewLabel);

    setSize(new Dimension(400, 200));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Starts the application.
   * @param args None.
   */
  public static void main(String... args) {
    new ApplicationLauncher().setVisible(true);
  }
}
