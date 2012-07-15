package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.db.DbManager;
import org.jlpt.common.ui.CloseAction;
import org.jlpt.common.utils.Validator;

/**
 * A dialog box in which users can add or modify a JLPT entry. An entry consists of a Japanese word,
 * its reading, and its meaning in English.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class JlptEntryDialogBox extends JFrame {

  private final DbManager databaseManager;
  private final ClientMain client;
  private final JTextField jwordTextField;
  private final JTextField readingTextField;
  private final JTextField engTextField;
  private final JButton okButton;

  /**
   * Creates a new JlptEntryDialogBox. All of the UI components are added here.
   * 
   * @param databaseManager The database manager.
   * @param client The client application.
   */
  public JlptEntryDialogBox(DbManager databaseManager, ClientMain client) {
    super("Add Entry");
    Validator.checkNull(databaseManager);
    Validator.checkNull(client);

    this.databaseManager = databaseManager;
    this.client = client;

    setIconImage(new ImageIcon(JlptEntryDialogBox.class.getResource("jpn-flag.png")).getImage());
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
    this.okButton = new JButton();
    this.okButton.setEnabled(false);
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new CloseAction(this));
    buttonPanel.add(this.okButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    setSize(400, 150);
    setResizable(false);
  }

  /**
   * Sets the values of the text fields.
   * 
   * @param entry The entry that contains the values used to populate the text fields.
   */
  public void setTextFields(JapaneseEntry entry) {
    Validator.checkNull(entry);

    this.jwordTextField.setText(entry.getJword());
    this.readingTextField.setText(entry.getReading());
    this.engTextField.setText(entry.getEnglishMeaning());
  }

  /**
   * Removes the previous actions and then sets the given action for the OK button.
   * 
   * @param action The action for the OK button.
   */
  public void setOkButtonAction(AbstractAction action) {
    Validator.checkNull(action);

    for (ActionListener listener : this.okButton.getActionListeners()) {
      this.okButton.removeActionListener(listener);
    }
    this.okButton.addActionListener(action);
  }

  /**
   * Sets the text for the OK button.
   * 
   * @param text The text for the OK button.
   */
  public void setOkButtonText(String text) {
    Validator.checkNotEmptyString(text);

    this.okButton.setText(text);
  }

  /** @return The Japanese word entered in the Kana/Kanji text field. */
  public String getJwordText() {
    return this.jwordTextField.getText();
  }

  /** @return The reading entered in the Reading text field. */
  public String getReadingText() {
    return this.readingTextField.getText();
  }

  /** @return The English meaning entered in the English Meaning text field. */
  public String getEngMeaningText() {
    return this.engTextField.getText();
  }

  /** @return The database manager. */
  public DbManager getDbManager() {
    return this.databaseManager;
  }

  /** @return The main client frame. */
  public ClientMain getClientMainFrame() {
    return this.client;
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
