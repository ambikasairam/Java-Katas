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
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.event.MouseInputAdapter;
import org.jlpt.client.table.ExportAction;
import org.jlpt.client.table.JlptTable;
import org.jlpt.client.table.JlptTableModel;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
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

  private static final Logger LOGGER = Logger.getGlobal();

  private final DbManager databaseManager;
  private JlptTable table;
  private JFrame frame;
  private JButton editButton;
  private JButton removeButton;
  private JapaneseEntry selectedEntry;
  private JLabel statusLabel;
  private JMenuItem editSelectedEntryMenuItem;
  private JMenuItem removeSelectedEntryMenuItem;
  private JTextField searchField;
  private JButton clearResultsButton;
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
    this.dateFormat = new SimpleDateFormat("MM-dd-YYYY 'at' HH:mm:ss z", Locale.US);

    JlptTableModel model = null;
    try {
      model = new JlptTableModel(databaseManager.getEntries());
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
      return;
    }
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
    try {
      updateTable(this.databaseManager.getEntries());
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

  /**
   * Updates the table with a list of entries.
   * 
   * @param entries The list of entries used to populate the table.
   */
  public void updateTable(List<JapaneseEntry> entries) {
    Validator.checkNull(entries);

    this.table.setModel(new JlptTableModel(entries));

    this.selectedEntry = this.table.selectEntry(this.selectedEntry);
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

    UiUtils.setFrameProperties(frame, "JLPT Study Client (ALPHA version)");

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

    this.searchField = new JTextField();
    this.searchField.addFocusListener(new FocusListener() {

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
    this.searchField.addKeyListener(new SearchFieldKeyListener(searchButton));
    Dimension dimension = new Dimension(250, this.searchField.getPreferredSize().height);
    this.searchField.setPreferredSize(dimension);
    searchPanel.add(this.searchField);

    this.clearResultsButton = new JButton("Clear Results");
    this.clearResultsButton.setMnemonic(KeyEvent.VK_C);
    this.clearResultsButton.addActionListener(new ClearResultsButtonAction());
    this.clearResultsButton.setEnabled(false);

    searchButton.addActionListener(new SearchButtonAction());
    searchPanel.add(searchButton);
    searchPanel.add(this.clearResultsButton);
  }

  /**
   * A key listener for the search field that will either execute a search using the given regular
   * expression pattern or clear the results from the screen when the Enter key is pressed and
   * depending on the status of the Search and Clear Results buttons.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class SearchFieldKeyListener extends KeyAdapter {

    private final JButton searchButton;

    /**
     * Creates a new SearchFieldKeyListener.
     * 
     * @param searchButton The Search button.
     */
    public SearchFieldKeyListener(JButton searchButton) {
      Validator.checkNull(searchButton);
      this.searchButton = searchButton;
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent event) {
      searchButton.setEnabled(!searchField.getText().isEmpty());
      if (event.getKeyChar() == KeyEvent.VK_ENTER) {
        if (searchButton.isEnabled()) {
          executeSearch();
        }
        else if (clearResultsButton.isEnabled()) {
          clearResults();
        }
      }
    }
  }

  /**
   * An action that will execute a search using the given regular expression pattern when the user
   * clicks on the Search button.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class SearchButtonAction implements ActionListener {
    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      executeSearch();
    }
  }

  /**
   * An action that will clear the results from the screen when the user clicks on the Clear Results
   * button.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class ClearResultsButtonAction implements ActionListener {
    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      clearResults();
    }
  }

  /**
   * Clears the results in the table.
   */
  private void clearResults() {
    statusLabel.setText("");
    updateTable();
    clearResultsButton.setEnabled(false);
  }

  /**
   * Executes a search against the database.
   */
  private void executeSearch() {
    List<JapaneseEntry> results;
    try {
      results = databaseManager.find(this.searchField.getText());
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, e.getMessage());

      String msg = "A problem was encountered while trying to find entries in the database: ";
      msg += "\n\n" + e.getMessage() + "\n\n";
      JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    String msg = "<html>&nbsp;&nbsp;&nbsp;Found " + results.size() + " entries that matched ";
    msg += "<b>" + this.searchField.getText() + "</b>.</html>";
    statusLabel.setText(msg);
    updateTable(results);
    this.clearResultsButton.setEnabled(true);
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
