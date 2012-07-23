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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
  private final DateFormat dateFormat;

  /**
   * Creates a new ClientMain instance. Creates the main client window and displays it on the user's
   * screen.
   * 
   * @param databaseManager The database manager.
   */
  public ClientMain(final DbManager databaseManager) {
    Validator.checkNull(databaseManager);

    this.databaseManager = databaseManager;
    this.dateFormat = new SimpleDateFormat("MM-dd-YYYY 'at' HH:mm:ss z");

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
    JMenuItem refreshMenuItem = new JMenuItem("Refresh Table");
    refreshMenuItem
        .addActionListener(ClientUtils.getRefreshTableAction(this.databaseManager, this));
    this.table.getPopupMenu().add(refreshMenuItem);
    this.table.getPopupMenu().add(new JSeparator());

    JMenuItem addMenuItem = new JMenuItem("Add New Entry");
    addMenuItem.addActionListener(ClientUtils.getAddNewEntryAction(this.databaseManager, this));
    this.table.getPopupMenu().add(addMenuItem);

    JMenuItem editMenuItem = new JMenuItem("Edit Selected Entry");
    editMenuItem.addActionListener(ClientUtils.getEditSelectedEntryAction(this.databaseManager,
        this));
    this.table.getPopupMenu().add(editMenuItem);

    JMenuItem removeMenuItem = new JMenuItem("Remove Selected Entry");
    removeMenuItem.addActionListener(ClientUtils.getRemoveSelectedEntryAction(this.databaseManager,
        this));
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
  public void updateTable(List<JapaneseEntry> entries) {
    Validator.checkNull(entries);

    this.table.setModel(new JlptTableModel(entries));

    this.table.setSelectedEntry(this.selectedEntry);
    this.statusLabel.setText("   Last updated on " + dateFormat.format(new Date()) + ".");

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

    UiUtils.setFrameProperties(frame);

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

    JButton refreshButton = new JButton("Refresh Table");
    refreshButton.setMnemonic(KeyEvent.VK_R);
    refreshButton.addActionListener(ClientUtils.getRefreshTableAction(this.databaseManager, this));
    buttonPanel.add(refreshButton);

    JButton addButton = new JButton("Add New Entry");
    addButton.setMnemonic(KeyEvent.VK_A);
    addButton.addActionListener(ClientUtils.getAddNewEntryAction(this.databaseManager, this));
    buttonPanel.add(addButton);

    this.editButton = new JButton("Edit Selected Entry");
    this.editButton.setMnemonic(KeyEvent.VK_E);
    this.editButton.addActionListener(ClientUtils.getEditSelectedEntryAction(this.databaseManager,
        this));
    buttonPanel.add(this.editButton);

    this.removeButton = new JButton("Remove Selected Entry");
    this.removeButton.setMnemonic(KeyEvent.VK_D);
    this.removeButton.addActionListener(ClientUtils.getRemoveSelectedEntryAction(
        this.databaseManager, this));
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
    searchButton.setMnemonic(KeyEvent.VK_F);
    searchButton.setEnabled(false);

    final JTextField searchField = new JTextField();
    searchField.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent event) {
        String msg = "<html>&nbsp;&nbsp;&nbsp;Enter the regular expression ";
        msg += "pattern you want to use. For example, <b>[ab]+out</b> and <b>^&#x3042</b> ";
        msg += "are valid patterns.</html>";
        statusLabel.setText(msg);
      }

      @Override
      public void focusLost(FocusEvent event) {
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
    clearResultsButton.setMnemonic(KeyEvent.VK_C);
    clearResultsButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent event) {
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
    JMenuItem refreshMenuItem = new JMenuItem("Refresh Table");
    refreshMenuItem
        .addActionListener(ClientUtils.getRefreshTableAction(this.databaseManager, this));
    optionsMenu.add(refreshMenuItem);
    optionsMenu.add(new JSeparator());

    JMenuItem addEntryMenuItem = new JMenuItem("Add New Entry");
    addEntryMenuItem
        .addActionListener(ClientUtils.getAddNewEntryAction(this.databaseManager, this));
    optionsMenu.add(addEntryMenuItem);

    this.editSelectedEntryMenuItem = new JMenuItem("Edit Selected Entry");
    this.editSelectedEntryMenuItem.addActionListener(ClientUtils.getEditSelectedEntryAction(
        this.databaseManager, this));
    this.editSelectedEntryMenuItem.setEnabled(false);
    optionsMenu.add(this.editSelectedEntryMenuItem);

    this.removeSelectedEntryMenuItem = new JMenuItem("Remove Selected Entry");
    this.removeSelectedEntryMenuItem.addActionListener(ClientUtils.getRemoveSelectedEntryAction(
        this.databaseManager, this));
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

  /** @return The table that contains all of the entries. */
  public JlptTable getTable() {
    return this.table;
  }

  /** @return The status label. */
  public JLabel getStatusLabel() {
    return this.statusLabel;
  }

  /** @return The selected entry in the table. */
  public JapaneseEntry getSelectedEntry() {
    return this.selectedEntry;
  }

}
