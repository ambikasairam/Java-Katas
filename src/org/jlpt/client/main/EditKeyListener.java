package org.jlpt.client.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.jlpt.common.datamodel.JapaneseEntry;
import org.jlpt.common.utils.Validator;

/**
 * An action that will enable the OK button once the user updates the text in any one of the three
 * text fields.
 * 
 * @author BJ Peter DeLaCruz
 */
public class EditKeyListener implements KeyListener {

  private final JlptEntryDialogBox dialogBox;
  private final JapaneseEntry oldEntry;

  /**
   * Creates a new EditKeyListener.
   * 
   * @param dialogBox The dialog box that contains the text fields that are using this listener.
   * @param oldEntry The old JLPT entry.
   */
  public EditKeyListener(JlptEntryDialogBox dialogBox, JapaneseEntry oldEntry) {
    Validator.checkNull(dialogBox);
    Validator.checkNull(oldEntry);

    this.dialogBox = dialogBox;
    this.oldEntry = oldEntry;
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
    String jword = this.dialogBox.getJwordText();
    String reading = this.dialogBox.getReadingText();
    String engMeaning = this.dialogBox.getEngMeaningText();
    JapaneseEntry entry = new JapaneseEntry(jword, reading, engMeaning);
    if (entry.equals(this.oldEntry)) {
      this.dialogBox.setOkButtonEnabled(false);
      return;
    }
    this.dialogBox.setOkButtonEnabled(true);
  }

  /** {@inheritDoc} */
  @Override
  public void keyTyped(KeyEvent event) {
    // Do nothing.
  }

}
