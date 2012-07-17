package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputAdapter;
import org.jlpt.client.table.ExportAction;
import org.jlpt.client.table.JlptTable;
import org.jlpt.client.table.JlptTableModel;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.DbManagerImpl;
import org.jlpt.common.db.EntryDoesNotExistException;
import org.jlpt.common.db.InvalidRegExPatternException;
import org.jlpt.common.ui.CloseAction;
import org.jlpt.common.ui.StatusBar;
import org.jlpt.common.ui.UiUtils;
import org.jlpt.common.utils.Validator;

/**
 * The main UI for the client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ClientMain {

  private final DbManager databaseManager;
  private JlptTable table;
  private JFrame frame;
  private JButton editButton;
  private JButton removeButton;
  private JapaneseEntry selectedEntry;
  private JLabel statusLabel;
  private JMenuItem editSelectedEntryMenuItem;
  private JMenuItem removeSelectedEntryMenuItem;

  /**
   * Creates a new ClientMain instance. Creates the client UI and displays it to the user's screen.
   * 
   * @param databaseManager The database manager.
   */
  public ClientMain(final DbManager databaseManager) {
    Validator.checkNull(databaseManager);

    this.databaseManager = databaseManager;

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
      return;
    }

    JlptTableModel model = new JlptTableModel(databaseManager.getEntries());
    this.table = new JlptTable(model);

    addPopupMenuItems();

    this.table.addMouseListener(new MouseInputAdapter() {

      @Override
      public void mouseClicked(MouseEvent event) {
        editButton.setEnabled(true);
        removeButton.setEnabled(true);
        editSelectedEntryMenuItem.setEnabled(true);
        removeSelectedEntryMenuItem.setEnabled(true);
        int row = table.rowAtPoint(new Point(event.getX(), event.getY()));
        selectedEntry = table.getEntry(row);
      }

    });

    this.frame = new JFrame();

    JScrollPane scrollPane =
        new JScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBackground(Color.WHITE);

    JMenuBar menuBar = new JMenuBar();
    addMenuItems(this.frame, menuBar, this.table);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    addButtons(this.frame, buttonPanel);

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    addSearchFields(searchPanel);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(buttonPanel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(searchPanel, BorderLayout.SOUTH);

    setProperties(this.frame);

    this.frame.add(panel, BorderLayout.CENTER);
    addStatusBar();

    this.frame.setJMenuBar(menuBar);
    // frame.pack();
    this.frame.setVisible(true);
  }

  /**
   * Adds menu items to the right-click popup menu.
   */
  private void addPopupMenuItems() {
    JMenuItem addMenuItem = new JMenuItem("Add New Entry");
    addMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        UiUtils.displayAddEntryDialogBox(databaseManager, ClientMain.this);
      }

    });
    this.table.getPopupMenu().add(addMenuItem);

    JMenuItem editMenuItem = new JMenuItem("Edit Selected Entry");
    editMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        int row = table.rowAtPoint(table.getPopupMenu().getPoint());
        UiUtils.displayEditEntryDialogBox(databaseManager, ClientMain.this, table.getEntry(row));
      }

    });
    this.table.getPopupMenu().add(editMenuItem);

    JMenuItem removeMenuItem = new JMenuItem("Remove Selected Entry");
    removeMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        displayRemoveEntryConfirmDialogBox("Remove Entry");
      }

    });
    this.table.getPopupMenu().add(removeMenuItem);
  }

  /**
   * Updates the table with the most recent entries in the database.
   */
  public void updateTable() {
    updateTable(this.databaseManager.getEntries());
  }

  /**
   * Updates the table with a list of entries.
   * 
   * @param entries The list of entries used to populate the table.
   */
  private void updateTable(List<JapaneseEntry> entries) {
    Validator.checkNull(entries);

    this.table.setModel(new JlptTableModel(entries));
    this.frame.invalidate();
    this.frame.validate();
    this.frame.repaint();
  }

  /**
   * Sets the properties for the client frame.
   * 
   * @param frame The client frame.
   */
  private void setProperties(JFrame frame) {
    Validator.checkNull(frame);

    frame.setTitle("JLPT Study (ALPHA version)");
    frame.setIconImage(new ImageIcon(ClientMain.class.getResource("jpn-flag.png")).getImage());
    frame.setLayout(new BorderLayout());

    // Make the frame half the height and width of the monitor.
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int height = screenSize.height;
    int width = screenSize.width;
    frame.setSize(width / 2, height / 2);
    frame.setResizable(false);

    // Center on screen.
    frame.setLocationRelativeTo(null);
    UiUtils.setEscKey(frame);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Adds buttons to the panel above the table.
   * 
   * @param frame The main client frame.
   * @param buttonPanel The panel to which to add buttons.
   */
  private void addButtons(final JFrame frame, JPanel buttonPanel) {
    Validator.checkNull(frame);
    Validator.checkNull(buttonPanel);

    JButton addButton = new JButton("Add New Entry");
    addButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        UiUtils.displayAddEntryDialogBox(databaseManager, ClientMain.this);
      }

    });
    buttonPanel.add(addButton);
    this.editButton = new JButton("Edit Selected Entry");
    this.editButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        UiUtils.displayEditEntryDialogBox(databaseManager, ClientMain.this, selectedEntry);
      }

    });
    buttonPanel.add(this.editButton);
    this.removeButton = new JButton("Remove Selected Entry");
    this.removeButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        displayRemoveEntryConfirmDialogBox("Remove Selected Entry");
      }

    });
    buttonPanel.add(this.removeButton);
    this.editButton.setEnabled(false);
    this.removeButton.setEnabled(false);
  }

  /**
   * Adds a text field and button to the search panel.
   * 
   * @param searchPanel The search panel to which to add a text field and button.
   */
  private void addSearchFields(JPanel searchPanel) {
    Validator.checkNull(searchPanel);

    final JButton searchButton = new JButton("Search");
    searchButton.setEnabled(false);

    final JTextField searchField = new JTextField();
    searchField.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent arg0) {
        String msg = "<html>&nbsp;&nbsp;&nbsp;Enter the regular expression ";
        msg += "pattern you want to use. For example, <b>[ab]+out</b> and <b>^&#x3042</b> ";
        msg += "are valid patterns.</html>";
        statusLabel.setText(msg);
      }

      @Override
      public void focusLost(FocusEvent arg0) {
        // Do nothing.
      }

    });
    searchField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent event) {
        searchButton.setEnabled(!searchField.getText().isEmpty());
      }

    });
    searchField.setPreferredSize(new Dimension(250, searchField.getPreferredSize().height));
    searchPanel.add(searchField);

    final JButton clearResultsButton = new JButton("Clear Results");
    clearResultsButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent arg0) {
        statusLabel.setText("");
        updateTable();
        clearResultsButton.setEnabled(false);
      }

    });
    clearResultsButton.setEnabled(false);

    searchButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        List<JapaneseEntry> results;
        try {
          results = databaseManager.find(searchField.getText());
        }
        catch (InvalidRegExPatternException e) {
          // TODO: Add logger.
          String msg = "A problem was encountered while trying to find entries in the database: ";
          msg += "\n\n" + e.getMessage() + "\n\n";
          JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
        String msg = "<html>&nbsp;&nbsp;&nbsp;Found " + results.size() + " entries that matched ";
        msg += "<b>" + searchField.getText() + "</b>.</html>";
        statusLabel.setText(msg);
        updateTable(results);
        clearResultsButton.setEnabled(true);
      }

    });
    searchPanel.add(searchButton);
    searchPanel.add(clearResultsButton);
  }

  /**
   * Adds menus and menu items to the menu bar.
   * 
   * @param frame The frame for the client application.
   * @param menuBar The menu bar to which to add menus.
   * @param table The table that contains Japanese words and their English meanings.
   */
  private void addMenuItems(JFrame frame, JMenuBar menuBar, JTable table) {
    Validator.checkNull(frame);
    Validator.checkNull(menuBar);
    Validator.checkNull(table);

    JMenu optionsMenu = new JMenu("Options");
    JMenuItem addEntryMenuItem = new JMenuItem("Add New Entry");
    addEntryMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        UiUtils.displayAddEntryDialogBox(databaseManager, ClientMain.this);
      }

    });
    optionsMenu.add(addEntryMenuItem);

    this.editSelectedEntryMenuItem = new JMenuItem("Edit Selected Entry");
    this.editSelectedEntryMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        UiUtils.displayEditEntryDialogBox(databaseManager, ClientMain.this, selectedEntry);
      }

    });
    this.editSelectedEntryMenuItem.setEnabled(false);
    optionsMenu.add(this.editSelectedEntryMenuItem);

    this.removeSelectedEntryMenuItem = new JMenuItem("Remove Selected Entry");
    this.removeSelectedEntryMenuItem.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
        displayRemoveEntryConfirmDialogBox("Remove Selected Entry");
      }

    });
    this.removeSelectedEntryMenuItem.setEnabled(false);
    optionsMenu.add(this.removeSelectedEntryMenuItem);

    optionsMenu.add(new JSeparator());
    optionsMenu.add(new ExportAction(table, optionsMenu));
    optionsMenu.add(new JSeparator());
    optionsMenu.add(new CloseAction(frame));
    menuBar.add(optionsMenu);

    JMenu helpMenu = new JMenu("Help");
    helpMenu.add(new JMenuItem("About"));
    helpMenu.add(new JMenuItem("User Guide"));
    menuBar.add(helpMenu);
  }

  /**
   * Adds a status bar to the given frame.
   */
  private void addStatusBar() {
    StatusBar statusBar = new StatusBar();
    this.statusLabel = new JLabel("   Current status: Normal");
    statusBar.add(this.statusLabel);
    this.frame.add(statusBar, BorderLayout.SOUTH);
  }

  /** @return The frame for the client application. */
  public JFrame getClientMainFrame() {
    return this.frame;
  }

  /**
   * Displays the Remove Entry confirm dialog box.
   * 
   * @param title The title of the dialog box.
   */
  private void displayRemoveEntryConfirmDialogBox(String title) {
    Validator.checkNotEmptyString(title);

    String msg = "Are you sure you want to delete the following entry from the database?\n\n";
    msg += this.selectedEntry.getEntryAsString("   ") + "\n\n";
    int option = JOptionPane.showConfirmDialog(this.frame, msg, title, JOptionPane.YES_NO_OPTION);
    if (option == JOptionPane.NO_OPTION) {
      return;
    }
    try {
      this.databaseManager.removeEntry(this.selectedEntry);
    }
    catch (EntryDoesNotExistException e) {
      // TODO: Add logger.
      System.err.println("Unable to remove selected entry from database: " + this.selectedEntry);
      return;
    }
    msg = "   Successfully removed " + this.selectedEntry.getJword() + " from the database.";
    this.statusLabel.setText(msg);
    updateTable();
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
