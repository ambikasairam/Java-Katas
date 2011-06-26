package org.katas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * An Applet version of the command-line program Bowling in the org.katas package.
 * 
 * @author BJ Peter DeLaCruz
 * @version 1.0
 */
public class BowlingApplet extends JApplet {

  /**
   * Used for serialization.
   */
  private static final long serialVersionUID = 1L;

  /**
   * An Applet version of the command-line program Bowling in the org.katas package.
   */
  @Override
  public void init() {
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel("Bowling Score Calculator");
    label1.setFont(new Font("Arial", Font.BOLD, 16));
    panel1.add(label1);
    panel1.setLayout(new FlowLayout(FlowLayout.CENTER));

    JPanel panel2 = new JPanel();

    StringBuffer buffer = new StringBuffer();
    String msg = "This program will calculate the final score for a bowling record.\n";
    buffer.append(msg);
    msg = "Simply enter a 10-frame bowling record in the textbox below.\n";
    buffer.append(msg);
    msg = "Here are some examples:\n\n";
    buffer.append(msg);
    msg = "XXXXXXXXXXXX (All strikes)\n\n";
    buffer.append(msg);
    msg = "5/5/5/5/5/5/5/5/5/5/5 (All spares)\n\n";
    buffer.append(msg);
    msg = "9-9-9-9-9-9-9-9-9-9- (Missed the last pin in all frames)\n\n";
    buffer.append(msg);
    msg = "X5-5/5-5/5-5/5-5/55 (One strike, four spares, four misses)\n\n";
    buffer.append(msg);
    JTextArea instructions = new JTextArea(buffer.toString());
    instructions.setEditable(false);

    final JTextField textField = new JTextField("", 21);
    JButton submit = new JButton("Calculate Score");

    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        final JDialog dlg = new JDialog();
        Bowling bowling = new Bowling();
        JLabel results = null;
        int score = 0;
        try {
          score = bowling.getBowlingScore(textField.getText());
          results = new JLabel("Your final score is " + score + ".");
        }
        catch (IllegalArgumentException e) {
          results = new JLabel("Invalid bowling record. Please try again.");
        }
        JButton ok = new JButton("OK");
        dlg.add(results);
        ok.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent actionEvent) {
            dlg.dispose();
          }
        });
        dlg.add(ok);
        dlg.setLayout(new FlowLayout(FlowLayout.CENTER));
        dlg.setSize(360, 75);
        dlg.setVisible(true);
      }
    };
    submit.addActionListener(actionListener);

    panel2.add(instructions);
    panel2.add(textField);
    panel2.add(submit);

    this.setLayout(new BorderLayout());
    this.add(panel1, BorderLayout.NORTH);
    this.add(panel2, BorderLayout.CENTER);
  }

}
