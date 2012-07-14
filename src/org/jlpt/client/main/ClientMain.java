package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.jlpt.client.table.ExportAction;
import org.jlpt.client.table.JlptTable;
import org.jlpt.client.table.JlptTableModel;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.ui.StatusBar;
import org.utils.Validator;

/**
 * The main UI for the client.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class ClientMain {

  /**
   * Creates a new ClientMain instance. Creates the client UI and displays it to the user's screen.
   * 
   * @param databaseManager The database manager.
   */
  public ClientMain(DbManagerImpl databaseManager) {
    Validator.checkNull(databaseManager);

    JFrame frame = new JFrame();
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
      return;
    }
    JlptTableModel model = new JlptTableModel(databaseManager.getEntries());
    JlptTable table = new JlptTable(model);
    JScrollPane scrollPane =
        new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JMenuBar menuBar = new JMenuBar();
    addMenuItems(frame, menuBar, table);

    JPanel panel = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    addButtons(buttonPanel);
    addSearchFields(searchPanel);
    panel.add(buttonPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(searchPanel, BorderLayout.SOUTH);

    setProperties(frame);

    frame.add(panel, BorderLayout.CENTER);
    addStatusBar(frame);

    frame.setJMenuBar(menuBar);
    // frame.pack();
    frame.setVisible(true);
  }

  /**
   * Sets the properties for the client frame.
   * 
   * @param frame The client frame.
   */
  private void setProperties(JFrame frame) {
    frame.setTitle("JLPT Study (ALPHA version)");
    frame.setIconImage(new ImageIcon(ClientMain.class.getResource("jpn-flag.png")).getImage());
    frame.setLayout(new BorderLayout());

    // Make the frame half the height and width of the monitor.
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height;
    int width = screenSize.width;
    frame.setSize(width / 2, height / 2);

    // Center on screen.
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Adds buttons to the panel above the table.
   * 
   * @param buttonPanel The panel to which to add buttons.
   */
  private void addButtons(JPanel buttonPanel) {
    JButton addEntryButton = new JButton("Add Entry");
    JButton removeEntryButton = new JButton("Remove Selected Entry");
    JButton updateEntryButton = new JButton("Update Selected Entry");
    buttonPanel.add(addEntryButton);
    buttonPanel.add(removeEntryButton);
    buttonPanel.add(updateEntryButton);
    removeEntryButton.setEnabled(false);
    updateEntryButton.setEnabled(false);
  }

  /**
   * Adds a text field and button to the search panel.
   * 
   * @param searchPanel The search panel to which to add a text field and button.
   */
  private void addSearchFields(JPanel searchPanel) {
    JTextField searchField = new JTextField();
    int defaultHeight = searchField.getPreferredSize().height;
    searchField.setPreferredSize(new Dimension(250, defaultHeight));
    searchPanel.add(searchField);
    JButton searchButton = new JButton("Search");
    searchPanel.add(searchButton);
  }

  /**
   * Adds menus and menu items to the menu bar.
   * 
   * @param frame The frame for the client application.
   * @param menuBar The menu bar to which to add menus.
   * @param table The table that contains Japanese words and their English meanings.
   */
  private void addMenuItems(final JFrame frame, JMenuBar menuBar, JTable table) {
    Validator.checkNull(menuBar);

    JMenu optionsMenu = new JMenu("Options");
    optionsMenu.add(new ExportAction(table, optionsMenu));
    optionsMenu.add(new JSeparator());
    optionsMenu.add(new AbstractAction("Exit") {

      @Override
      public void actionPerformed(ActionEvent event) {
        WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
        frame.dispatchEvent(windowClosing);
      }

    });
    menuBar.add(optionsMenu);
  }

  /**
   * Adds a status bar to the given frame.
   * 
   * @param frame The frame to which to add the status bar.
   */
  private void addStatusBar(JFrame frame) {
    StatusBar statusBar = new StatusBar();
    JLabel label = new JLabel("   Current status: Normal");
    statusBar.add(label);
    frame.add(statusBar, BorderLayout.SOUTH);
  }

  /**
   * Tests this class.
   * 
   * @param args The file name and delimiter.
   * @throws IOException If there are problems reading in from the database file.
   */
  public static void main(String... args) throws IOException {
    if (args.length != 2) {
      System.err.println("Need file name and delimiter.");
      return;
    }
    new ClientMain(new DbManagerImpl(args[0], args[1]));
  }

}
