package org.jlpt.common.ui;

import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import org.jlpt.common.utils.Validator;

/**
 * Contains utility methods for modifying UI components.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class UiUtils {

  /** Do not instantiate this class. */
  private UiUtils() {
    // Empty constructor.
  }

  /**
   * Centers a component in the center and on top of its parent.
   * 
   * @param parent The parent of the component to center.
   * @param child The component to center.
   */
  public static void centerComponentOnParent(JFrame parent, JFrame child) {
    Validator.checkNull(parent);
    Validator.checkNull(child);

    Point topLeft = parent.getLocationOnScreen();
    Dimension parentSize = parent.getSize();

    Dimension childSize = child.getSize();

    int x, y;
    if (parentSize.width > childSize.width) {
      x = ((parentSize.width - childSize.width) / 2) + topLeft.x;
    }
    else {
      x = topLeft.x;
    }

    if (parentSize.height > childSize.height) {
      y = ((parentSize.height - childSize.height) / 2) + topLeft.y;
    }
    else {
      y = topLeft.y;
    }

    child.setLocation(x, y);
  }

}