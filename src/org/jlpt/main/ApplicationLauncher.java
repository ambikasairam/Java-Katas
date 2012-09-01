package org.jlpt.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jlpt.client.main.NetworkedClientConfigDialogBox;
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

  private static final Logger LOGGER = Logger.getGlobal();

  /**
   * Creates a new ApplicationLauncher.
   */
  public ApplicationLauncher() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      return;
    }
    UiUtils.setFrameProperties(this, "Select Mode");
    JPanel innerPanel = new JPanel(new GridBagLayout());

    setLayout(new BorderLayout());

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
    GridBagConstraints constraints = new GridBagConstraints();
    String msg = "<html>JLPT Study can run in client mode with or without networking<br>";
    msg += " or server mode. Please select one.</html>";
    JLabel lblNewLabel = new JLabel(msg);
    constraints.fill = GridBagConstraints.HORIZONTAL;
    innerPanel.add(lblNewLabel, constraints);
    constraints.gridy = 1;
    innerPanel.add(rdbtnStandaloneClient, constraints);

    final JRadioButton rdbtnNetworkedClient = new JRadioButton("Networked Client");
    rdbtnNetworkedClient.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        if (rdbtnNetworkedClient.hasFocus()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new NetworkedClientConfigDialogBox();
        }
      }

    });
    rdbtnNetworkedClient.addFocusListener(new FocusAdapter() {

      @Override
      public void focusGained(FocusEvent event) {
        rdbtnNetworkedClient.setSelected(true);
      }

    });
    rdbtnNetworkedClient.setMnemonic(KeyEvent.VK_N);

    constraints.gridy = 2;
    innerPanel.add(rdbtnNetworkedClient, constraints);

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
    constraints.gridy = 3;
    innerPanel.add(rdbtnServer, constraints);

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
        else if (rdbtnNetworkedClient.isSelected()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new NetworkedClientConfigDialogBox();
        }
        else if (rdbtnServer.isSelected()) {
          setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
          UiUtils.closeFrame(ApplicationLauncher.this);
          new ServerMain();
        }
      }
    });
    constraints.gridy = 4;
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    buttonsPanel.add(btnLaunch);
    innerPanel.add(buttonsPanel, constraints);

    add(innerPanel, BorderLayout.CENTER);
    setPreferredSize(new Dimension(innerPanel.getPreferredSize().width + 50,
        innerPanel.getPreferredSize().height + 50));

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    UiUtils.setEscKey(this);
    setLocationRelativeTo(null);
    pack();
    setVisible(true);
  }

  /**
   * Starts the application.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    Logger.getGlobal().setLevel(Level.INFO);
    new ApplicationLauncher();
  }
}
