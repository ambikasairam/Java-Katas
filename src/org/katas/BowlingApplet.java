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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (ClassNotFoundException e1) {
      e1.printStackTrace();
      return;
    }
    catch (InstantiationException e1) {
      e1.printStackTrace();
      return;
    }
    catch (IllegalAccessException e1) {
      e1.printStackTrace();
      return;
    }
    catch (UnsupportedLookAndFeelException e1) {
      e1.printStackTrace();
      return;
    }
    JPanel panel1 = new JPanel();
    JLabel label1 = new JLabel("Bowling Score Calculator");
    label1.setFont(new Font("Arial", Font.BOLD, 16));
    panel1.add(label1);
    panel1.setLayout(new FlowLayout(FlowLayout.CENTER));

    JPanel panel2 = new JPanel();

    StringBuffer buffer = new StringBuffer(500);
    String info = "This program will calculate the final score for a bowling record.\n";
    info += "Simply enter a 10-frame bowling record in the textbox below.\n";
    info += "Here are some examples:\n\n";
    buffer.append(info);

    String example = "XXXXXXXXXXXX (All strikes)\n\n5/5/5/5/5/5/5/5/5/5/5 (All spares)\n\n";
    example += "9-9-9-9-9-9-9-9-9-9- (Missed the last pin in all frames)\n\n";
    example += "X5-5/5-5/5-5/5-5/54 (One strike, four spares, four misses)\n\n";
    buffer.append(example);

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
