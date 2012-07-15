package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.db.EntryAlreadyExistsException;
import org.jlpt.common.ui.CloseAction;
import org.utils.Validator;

/**
 * A dialog box in which users can add a new JLPT entry to the database. An entry consists of a
 * Japanese word, its reading, and its meaning in English.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class AddEntryDialogBox extends JFrame {

  private final DbManager databaseManager;
  private final ClientMain client;
  private final JTextField jwordTextField;
  private final JTextField readingTextField;
  private final JTextField engTextField;
  private final JButton okButton;

  /**
   * Creates a new AddEntryDialogBox. All of the UI components are added here.
   * 
   * @param databaseManager The database manager.
   * @param client The client application.
   */
  public AddEntryDialogBox(DbManager databaseManager, ClientMain client) {
    super("Add Entry");
    Validator.checkNull(databaseManager);
    Validator.checkNull(client);

    this.databaseManager = databaseManager;
    this.client = client;

    setIconImage(new ImageIcon(AddEntryDialogBox.class.getResource("jpn-flag.png")).getImage());
    setLayout(new BorderLayout());

    GridBagLayout grid = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.EAST;

    JPanel panel = new JPanel(grid);

    JLabel jwordLabel = new JLabel("     Kana/Kanji: ");
    grid.setConstraints(jwordLabel, constraints);
    panel.add(jwordLabel);

    this.jwordTextField = new JTextField();
    int defaultHeight = this.jwordTextField.getPreferredSize().height;
    this.jwordTextField.setPreferredSize(new Dimension(250, defaultHeight));
    this.jwordTextField.addKeyListener(new KeyListenerImpl());
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(this.jwordTextField, constraints);
    panel.add(this.jwordTextField);

    JLabel readingLabel = new JLabel("        Reading: ");
    constraints.weightx = 0.0;
    constraints.gridwidth = 1;
    grid.setConstraints(readingLabel, constraints);
    panel.add(readingLabel);

    this.readingTextField = new JTextField();
    this.readingTextField.setPreferredSize(new Dimension(250, defaultHeight));
    this.readingTextField.addKeyListener(new KeyListenerImpl());
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(this.readingTextField, constraints);
    panel.add(this.readingTextField);

    JLabel engLabel = new JLabel("English Meaning: ");
    constraints.weightx = 0.0;
    constraints.gridwidth = 1;
    grid.setConstraints(engLabel, constraints);
    panel.add(engLabel);

    this.engTextField = new JTextField();
    this.engTextField.setPreferredSize(new Dimension(250, defaultHeight));
    this.engTextField.addKeyListener(new KeyListenerImpl());
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(this.engTextField, constraints);
    panel.add(this.engTextField);

    add(panel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    this.okButton = new JButton("OK");
    this.okButton.setEnabled(false);
    this.okButton.addActionListener(new AddEntryAction());
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new CloseAction(this));
    buttonPanel.add(this.okButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    setSize(400, 150);
    setResizable(false);
  }

  /**
   * An action that will an entry into the database when the user clicks on the OK button.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class AddEntryAction implements ActionListener {

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      String jword = jwordTextField.getText();
      String reading = readingTextField.getText();
      String englishMeaning = engTextField.getText();
      JapaneseEntry entry = new JapaneseEntry(jword, reading, englishMeaning);
      try {
        databaseManager.addEntry(entry);
      }
      catch (EntryAlreadyExistsException e) {
        // TODO: Add logger. Show popup message. Then return.
        
        return;
      }
      try {
        databaseManager.save();
      }
      catch (IOException e) {
        // TODO: Add logger.
        System.err.println(e);
      }
      client.updateTable();
      WindowEvent windowClosing =
          new WindowEvent(AddEntryDialogBox.this, WindowEvent.WINDOW_CLOSING);
      AddEntryDialogBox.this.dispatchEvent(windowClosing);
    }

  }

  /**
   * A key listener that will enable the OK button once the user inputs text in all three text
   * fields.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class KeyListenerImpl implements KeyListener {

    /** {@inheritDoc} */
    @Override
    public void keyPressed(KeyEvent event) {
      // Do nothing.
    }

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent event) {
      if (jwordTextField.getText().isEmpty()) {
        okButton.setEnabled(false);
        return;
      }
      if (readingTextField.getText().isEmpty()) {
        okButton.setEnabled(false);
        return;
      }
      if (engTextField.getText().isEmpty()) {
        okButton.setEnabled(false);
        return;
      }
      okButton.setEnabled(true);
    }

    /** {@inheritDoc} */
    @Override
    public void keyTyped(KeyEvent event) {
      // Do nothing.
    }

  }

}
