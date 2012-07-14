package org.jlpt.client.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jlpt.common.ui.CloseAction;

/**
 * A dialog box in which users can add a new JLPT entry to the database. An entry consists of a
 * Japanese word, its reading, and its meaning in English.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class AddEntryDialogBox extends JFrame {

  /**
   * Creates a new AddEntryDialogBox. All of the UI components are added here.
   */
  public AddEntryDialogBox() {
    super("Add Entry");

    setLayout(new BorderLayout());

    GridBagLayout grid = new GridBagLayout();
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.anchor = GridBagConstraints.EAST;

    JPanel panel = new JPanel(grid);

    JLabel kanaKanjiLabel = new JLabel("     Kana/Kanji: ");
    grid.setConstraints(kanaKanjiLabel, constraints);
    panel.add(kanaKanjiLabel);

    JTextField kanaKanjiTextField = new JTextField();
    int defaultHeight = kanaKanjiTextField.getPreferredSize().height;
    kanaKanjiTextField.setPreferredSize(new Dimension(250, defaultHeight));
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(kanaKanjiTextField, constraints);
    panel.add(kanaKanjiTextField);

    JLabel readingLabel = new JLabel("        Reading: ");
    constraints.weightx = 0.0;
    constraints.gridwidth = 1;
    grid.setConstraints(readingLabel, constraints);
    panel.add(readingLabel);

    JTextField readingTextField = new JTextField();
    readingTextField.setPreferredSize(new Dimension(250, defaultHeight));
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(readingTextField, constraints);
    panel.add(readingTextField);

    JLabel engLabel = new JLabel("English Meaning: ");
    constraints.weightx = 0.0;
    constraints.gridwidth = 1;
    grid.setConstraints(engLabel, constraints);
    panel.add(engLabel);

    JTextField engTextField = new JTextField();
    engTextField.setPreferredSize(new Dimension(250, defaultHeight));
    constraints.gridwidth = GridBagConstraints.REMAINDER;
    grid.setConstraints(engTextField, constraints);
    panel.add(engTextField);

    add(panel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JButton okButton = new JButton("OK");
    okButton.setEnabled(false);
    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new CloseAction(this));
    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    setSize(400, 150);
    setResizable(false);
  }

}
