package org.katas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * The user interface for the <a href="http://uva.onlinejudge.org/external/12/1210.pdf"
 * target="_blank">Sum of Consecutive Prime Numbers kata</a>.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ConsecutivePrimesCalculator extends JFrame {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

  static {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Unable to set look and feel: " + e.toString());
      System.exit(-1);
    }
  }

  private final JPanel panel = new JPanel(new GridLayout(4, 3));
  private final JTextField textField = new JTextField() {

    private static final long serialVersionUID = 1L;

    /** {@inheritDoc} */
    @Override
    public void processKeyEvent(KeyEvent event) {
      char c = event.getKeyChar();
      try {
        // Ignore all non-printable characters. Just check the printable ones.
        if (c > 31 && c < 127) {
          Integer.parseInt(c + "");
        }
        super.processKeyEvent(event);
      }
      catch (NumberFormatException nfe) {
        // Do nothing. Character entered is not a number, so ignore it.
        return;
      }
    }

  };
  private final JButton enterButton = new JButton("Enter");
  private final JPanel emptyPanel = new JPanel();
  private final JLabel resultsLabel = new JLabel(" ");

  private final ConsecutivePrimes engine = new ConsecutivePrimes();

  /**
   * Creates and displays the user interface, which looks like a normal calculator.
   */
  public ConsecutivePrimesCalculator() {
    super("Consecutive Primes");
    setLayout(new BorderLayout());

    JButton[] numberButtons = new JButton[10];
    for (int index = 0; index <= 9; index++) {
      numberButtons[index] = new JButton(index + "");
      numberButtons[index].addActionListener(new NumberButtonAction());
      if (index > 0) {
        panel.add(numberButtons[index]);
      }
    }
    panel.add(emptyPanel);
    panel.add(numberButtons[0]);
    enterButton.addActionListener(new EnterButtonAction());
    panel.add(enterButton);

    textField.addKeyListener(new EnterKeyListener());
    add(textField, BorderLayout.NORTH);
    add(panel, BorderLayout.CENTER);
    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
    southPanel.add(resultsLabel);
    add(southPanel, BorderLayout.SOUTH);

    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  /**
   * A listener that will update the text field when the user presses on a button.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class NumberButtonAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent event) {
      textField.setText(textField.getText() + ((JButton) event.getSource()).getText());
      resultsLabel.setText(" ");
    }

  }

  /**
   * A listener that will find the number of representations for the number that the user entered
   * when the Enter button is pressed.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class EnterButtonAction implements ActionListener {

    /** {@inheritDoc} */
    @Override
    public void actionPerformed(ActionEvent event) {
      findSums();
    }

  }

  /**
   * A listener that will find the number of representations for the number that the user entered
   * when the Enter key is entered.
   * 
   * @author BJ Peter DeLaCruz
   */
  private class EnterKeyListener extends KeyAdapter {

    /** {@inheritDoc} */
    @Override
    public void keyReleased(KeyEvent event) {
      if (event.getKeyCode() == KeyEvent.VK_ENTER) {
        findSums();
      }
    }

  }

  /**
   * Finds the number of representations for the number that was entered.
   */
  private void findSums() {
    if (textField.getText().isEmpty()) {
      return;
    }
    int number = Integer.parseInt(textField.getText());
    if (number <= 1) {
      String text = "<html>Found <b>0</b> representations.</html>";
      textField.setText("");
      resultsLabel.setText(text);
      return;
    }
    int count = engine.getCount(number);
    textField.setText("");
    String text = "<html>Found <b>" + count + "</b> representations.</html>";
    if (count == 1) {
      text = text.replace("representations", "representation");
    }
    resultsLabel.setText(text);
    LOGGER.log(Level.INFO, number + ": " + engine.getResults(number));
  }

  /**
   * The main entry point of this application. Displays a numeric pad in which the user can enter a
   * number.
   * 
   * @param args None.
   */
  public static void main(String... args) {
    new ConsecutivePrimesCalculator();
  }

}
