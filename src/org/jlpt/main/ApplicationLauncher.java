package org.jlpt.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jlpt.client.main.StandaloneClientOpenFileDialogBox;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.server.main.ServerMain;

/**
 * A dialog box that allows a user to select either client mode or server mode for this application.
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
    UiUtils.setFrameProperties(this, "Select Mode");

    // Use absolute positioning.
    getContentPane().setLayout(null);

    final JRadioButton rdbtnStandaloneClient =
        new JRadioButton("Standalone Client (no networking)");
    rdbtnStandaloneClient.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        if (rdbtnStandaloneClient.hasFocus()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new StandaloneClientOpenFileDialogBox();
        }
      }

    });
    rdbtnStandaloneClient.addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent event) {
        rdbtnStandaloneClient.setSelected(true);
      }

    });
    rdbtnStandaloneClient.setSelected(true);
    rdbtnStandaloneClient.setMnemonic(KeyEvent.VK_C);
    rdbtnStandaloneClient.setBounds(30, 49, 215, 23);
    getContentPane().add(rdbtnStandaloneClient);

    final JRadioButton rdbtnNetworkedClient = new JRadioButton("Networked Client");
    rdbtnNetworkedClient.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        if (rdbtnNetworkedClient.hasFocus()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          // TODO: Add code here.
        }
      }

    });
    rdbtnNetworkedClient.addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent event) {
        rdbtnNetworkedClient.setSelected(true);
      }

    });
    rdbtnNetworkedClient.setEnabled(false);
    rdbtnNetworkedClient.setMnemonic(KeyEvent.VK_N);
    rdbtnNetworkedClient.setBounds(30, 75, 107, 23);
    getContentPane().add(rdbtnNetworkedClient);

    final JRadioButton rdbtnServer = new JRadioButton("Server");
    rdbtnServer.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        if (rdbtnServer.hasFocus()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new ServerMain();
        }
      }

    });
    rdbtnServer.addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent event) {
        rdbtnServer.setSelected(true);
      }

    });
    rdbtnServer.setMnemonic(KeyEvent.VK_S);
    rdbtnServer.setBounds(30, 102, 57, 23);
    getContentPane().add(rdbtnServer);

    ButtonGroup group = new ButtonGroup();
    group.add(rdbtnStandaloneClient);
    group.add(rdbtnNetworkedClient);
    group.add(rdbtnServer);

    JButton btnLaunch = new JButton("Launch");
    btnLaunch.setMnemonic(KeyEvent.VK_L);
    btnLaunch.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        if (rdbtnStandaloneClient.isSelected()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new StandaloneClientOpenFileDialogBox();
        }
        else if (rdbtnServer.isSelected()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new ServerMain();
        }
      }
    });
    int width = btnLaunch.getPreferredSize().width;
    int height = btnLaunch.getPreferredSize().height;
    btnLaunch.setBounds(267, 118, width, height);
    getContentPane().add(btnLaunch);

    String msg = "<html>JLPT Study can run in client mode with or without networking<br>";
    msg += " or server mode. Please select one.</html>";
    JLabel lblNewLabel = new JLabel(msg);
    lblNewLabel.setBounds(30, 11, 344, 31);
    getContentPane().add(lblNewLabel);

    setSize(new Dimension(360, 190));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * Starts the application.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    new ApplicationLauncher();
  }
}
