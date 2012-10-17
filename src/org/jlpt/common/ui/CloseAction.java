package org.jlpt.common.ui;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import org.jlpt.common.utils.Validator;

/**
 * An action that will close the current frame.
 * 
 * @author BJ Peter DeLaCruz
 */
@SuppressWarnings("serial")
public class CloseAction extends AbstractAction {

  private final JFrame frame;

  /**
   * Creates a new CloseAction.
   * @param frame The frame to close.
   */
  public CloseAction(JFrame frame) {
    super("Exit");
    Validator.checkNull(frame);

    this.frame = frame;
  }

  /** {@inheritDoc} */
  @Override
  public void actionPerformed(ActionEvent event) {
    WindowEvent windowClosing = new WindowEvent(this.frame, WindowEvent.WINDOW_CLOSING);
    this.frame.dispatchEvent(windowClosing);
  }

}
