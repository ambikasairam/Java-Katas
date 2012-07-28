package org.jlpt.common.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import org.jlpt.common.utils.Validator;

/**
 * Contains utility methods for modifying UI components.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class UiUtils {

  private static final String JPN_FLAG_ICON_URL = "images/jpn-flag.png";
  private static final String OFFLINE_ICON_URL = "images/offline.png";
  private static final String ONLINE_ICON_URL = "images/online.png";
  private static final String CONNECTING_ICON_URL = "images/connecting.png";
  private static final String STANDALONE_ICON_URL = "images/standalone.png";

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

  /**
   * Sets the ESC key to close the frame that is currently in focus.
   * 
   * @param frame The frame that will be closed when the ESC key is closed.
   */
  public static void setEscKey(final JFrame frame) {
    KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
    @SuppressWarnings("serial")
    Action escapeAction = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
      }
    };
    frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        .put(escapeKeyStroke, "ESCAPE");
    frame.getRootPane().getActionMap().put("ESCAPE", escapeAction);
  }

  /**
   * Closes the given frame.
   * 
   * @param frame The frame to close.
   */
  public static void closeFrame(JFrame frame) {
    Validator.checkNull(frame);

    WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
    frame.dispatchEvent(windowClosing);
  }

  /**
   * Sets the properties of a frame such as its title and icon image.
   * 
   * @param frame The frame whose properties are to be set.
   * @param title The title of the frame.
   */
  public static void setFrameProperties(JFrame frame, String title) {
    Validator.checkNull(frame);
    Validator.checkNotEmptyString(title);

    frame.setTitle(title);
    frame.setIconImage(getJapaneseFlagIcon());
    frame.setResizable(false);
  }

  /** @return The Japanese flag icon. */
  public static Image getJapaneseFlagIcon() {
    return new ImageIcon(UiUtils.class.getResource(JPN_FLAG_ICON_URL)).getImage();
  }

  /** @return The offline icon. */
  public static ImageIcon getOfflineIcon() {
    return new ImageIcon(UiUtils.class.getResource(OFFLINE_ICON_URL));
  }

  /** @return The online icon. */
  public static ImageIcon getOnlineIcon() {
    return new ImageIcon(UiUtils.class.getResource(ONLINE_ICON_URL));
  }

  /** @return The connecting icon. */
  public static ImageIcon getConnectingIcon() {
    return new ImageIcon(UiUtils.class.getResource(CONNECTING_ICON_URL));
  }

  /** @return The standalone mode icon. */
  public static ImageIcon getStandaloneModeIcon() {
    return new ImageIcon(UiUtils.class.getResource(STANDALONE_ICON_URL));
  }

}
