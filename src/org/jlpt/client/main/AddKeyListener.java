package org.jlpt.client.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jlpt.common.utils.Validator;

/**
 * A key listener that will enable the OK button once the user inputs text in all three text
 * fields.
 * 
 * @author BJ Peter DeLaCruz
 */
public class AddKeyListener implements KeyListener {

  private final JlptEntryDialogBox dialogBox;

  /**
   * Creates a new AddKeyListener.
   * 
   * @param dialogBox The dialog box that contains the text fields that are using this listener.
   */
  public AddKeyListener(JlptEntryDialogBox dialogBox) {
    Validator.checkNull(dialogBox);

    this.dialogBox = dialogBox;
  }

  /** {@inheritDoc} */
  @Override
  public void keyPressed(KeyEvent event) {
    // Do nothing.
  }

  /** {@inheritDoc} */
  @Override
  public void keyReleased(KeyEvent event) {
    if (this.dialogBox.getJwordText().isEmpty()) {
      this.dialogBox.setOkButtonEnabled(false);
      return;
    }
    if (this.dialogBox.getReadingText().isEmpty()) {
      this.dialogBox.setOkButtonEnabled(false);
      return;
    }
    if (this.dialogBox.getEngMeaningText().isEmpty()) {
      this.dialogBox.setOkButtonEnabled(false);
      return;
    }
    this.dialogBox.setOkButtonEnabled(true);
    if (event.getKeyChar() == KeyEvent.VK_ENTER) {
      this.dialogBox.clickOkButton();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void keyTyped(KeyEvent event) {
    // Do nothing.
  }

}
